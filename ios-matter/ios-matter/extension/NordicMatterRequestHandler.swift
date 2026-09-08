//
//  NordicMatterRequestHandler.swift
//  ios-matter
//

import Matter
import MatterSupport

/// Ready-made principal class for an app's Matter "Add Device" extension.
///
/// An app embedding this library does not have to implement the extension itself. It needs an
/// app-extension target whose `Info.plist` carries
///
/// ```xml
/// <key>NSExtension</key>
/// <dict>
///     <key>NSExtensionPointIdentifier</key>
///     <string>com.apple.matter.support.extension.device-setup</string>
///     <key>NSExtensionPrincipalClass</key>
///     <string>NordicMatterRequestHandler</string>
/// </dict>
/// ```
///
/// and that links the Kotlin framework this library is published in - the `@objc` name above is
/// resolved through the Objective-C runtime at launch, so no Swift declaration of this type is
/// needed on the app's side. See `iosApp/nrfMatter` for a worked example.
///
/// ## Why this holds no Kotlin
///
/// The extension is a separate process from the app, and everything it produces has to travel back
/// over ``SharedStorage`` (`UserDefaults` on an app group) to be of any use. Registering the device
/// with a `Fabric` from here would write to the *extension's* container, which the app never reads,
/// and would spend two Matter reads doing it. So this class only pairs the device and hands the
/// name back; the app registers it, which it has to do anyway.
///
/// ## The handshake
///
/// | key | direction |
/// | --- | --- |
/// | ``SharedConsts/nodeIdKey`` | app writes the node ID to commission |
/// | ``SharedConsts/roomsKey`` | app writes the rooms to offer |
/// | ``SharedConsts/deviceNameKey`` | extension writes the name the user chose |
/// | ``SharedConsts/resultKey`` | extension writes success |
///
/// `LocalMatterCommissioner` owns the app's half.
///
/// ## Why this is `internal`
///
/// Nothing references this type at compile time - the extension point finds it by the `@objc` name
/// below - so it does not need to be `public`, and must not be. Swift emits a `public` class into
/// the generated Objective-C header as an `@interface` whose superclass lives in MatterSupport,
/// which is Swift-only and publishes no Objective-C header; cinterop then fails to parse it with
/// `cannot find interface declaration for 'MatterAddDeviceExtensionRequestHandler'`.
///
/// `internal` keeps it out of that header while still emitting the class and registering it with
/// the Objective-C runtime through `__objc_stublist`, which is all the extension point needs. The
/// class symbol ends up local rather than global, which does not matter: `-ObjC` in the
/// extension's `OTHER_LDFLAGS` force-loads this module out of the static framework, so the class
/// survives - verified in Debug and Release builds of `iosApp/nrfMatter`.
@objc(NordicMatterRequestHandler)
final class NordicMatterRequestHandler: MatterAddDeviceExtensionRequestHandler {

    private static let tag = "AddDeviceExtension"

    /// Rooms offered when the app wrote none.
    private static let defaultRooms = [
        "Living Room", "Bedroom", "Office", "Kitchen", "Dining Room",
    ]

    /// Static because it owns the process-wide `MTRDeviceController` through
    /// `LocalControllerProvider`, and the system is free to create more than one handler per
    /// process - two commissioners would fight over one controller.
    private static let commissioner = MatterCommissioner()

    private let storage = SharedStorage()

    /// Returns the rooms the app offered for placing a newly added device.
    ///
    /// - Parameter home: The home to fetch rooms for. Ignored - the list does not vary by home.
    /// - Returns: The rooms the app wrote to shared storage, or ``defaultRooms`` if it wrote none.
    override func rooms(
        in home: MatterAddDeviceRequest.Home?
    ) async -> [MatterAddDeviceRequest.Room] {
        let names = storage.getStringArray(key: SharedConsts.roomsKey) ?? Self.defaultRooms
        SwiftLogger.info(tag: Self.tag, "Offering \(names.count) rooms.")

        return names.map { MatterAddDeviceRequest.Room(displayName: $0) }
    }

    /// Commissions the device described by the onboarding payload onto the local fabric.
    ///
    /// - Parameters:
    ///   - home: The home the device is being added to. Ignored.
    ///   - onboardingPayload: The Matter onboarding payload read from the commissioning QR code.
    ///   - commissioningID: The unique identifier for this commissioning attempt. Ignored - the
    ///     node ID comes from shared storage so that the app chooses it.
    /// - Throws: `CommissioningError.missingNodeId` if the app wrote no node ID, or an error from
    ///   the commissioning session itself.
    override func commissionDevice(
        in home: MatterAddDeviceRequest.Home?,
        onboardingPayload: String,
        commissioningID: UUID
    ) async throws {
        guard let nodeID = storage.getNumber(key: SharedConsts.nodeIdKey) else {
            SwiftLogger.error(tag: Self.tag, "No node ID in shared storage.")
            throw CommissioningError.missingNodeId
        }

        SwiftLogger.info(tag: Self.tag, "Commissioning node \(nodeID).")
        try await Self.commissioner.commission(payload: onboardingPayload, nodeID: nodeID)
    }

    /// Records the name the user chose and reports success to the app.
    ///
    /// - Parameters:
    ///   - name: The display name chosen for the device. Handed to the app, which applies it when
    ///     it reads the device back.
    ///   - room: The room the device was placed in. Not passed on - the library has no notion of
    ///     rooms beyond offering the list.
    override func configureDevice(
        named name: String,
        in room: MatterAddDeviceRequest.Room?
    ) async {
        SwiftLogger.info(tag: Self.tag, "Device configured as \"\(name)\". Reporting success.")

        storage.storeString(key: SharedConsts.deviceNameKey, value: name)
        storage.storeBool(key: SharedConsts.resultKey, value: true)
        Self.commissioner.releaseCommissioner()
    }

    /// Accepts the device credential presented during commissioning without validating it.
    ///
    /// Returning without throwing tells the system every credential is acceptable, which is what
    /// this library wants: it commissions development kits and simulated devices whose
    /// certificates are not signed by a production attestation authority. `MatterAttestationDelegate`
    /// takes the same stance for the attestation step.
    ///
    /// - Parameter deviceCredential: The credential presented by the device. Ignored.
    override func validateDeviceCredential(
        _ deviceCredential: MatterAddDeviceExtensionRequestHandler.DeviceCredential
    ) async throws {
    }

    /// Selects a WiFi network for the device to join.
    ///
    /// Always defers to the network the phone is already on, so the scan results are unused.
    ///
    /// - Parameter wifiScanResults: The WiFi networks discovered during scanning. Ignored.
    /// - Returns: `.defaultSystemNetwork`.
    override func selectWiFiNetwork(
        from wifiScanResults: [MatterAddDeviceExtensionRequestHandler.WiFiScanResult]
    ) async throws -> MatterAddDeviceExtensionRequestHandler.WiFiNetworkAssociation {
        return .defaultSystemNetwork
    }

    /// Selects a Thread network for the device to join, from the networks found during scanning.
    ///
    /// Logs every network found, then picks the first one by extended PAN ID.
    /// `.defaultSystemNetwork` is not usable here, so there is no way to defer the choice to the
    /// system.
    ///
    /// - Parameter threadScanResults: The Thread networks discovered during scanning.
    /// - Returns: An association naming the first scanned network's extended PAN ID.
    /// - Throws: `CommissioningError.noThreadNetwork` if scanning found none.
    override func selectThreadNetwork(
        from threadScanResults: [MatterAddDeviceExtensionRequestHandler.ThreadScanResult]
    ) async throws -> MatterAddDeviceExtensionRequestHandler.ThreadNetworkAssociation {
        SwiftLogger.info(
            tag: Self.tag,
            "Selecting Thread network from \(threadScanResults.count) scan results."
        )
        for result in threadScanResults {
            SwiftLogger.info(tag: Self.tag, "Detected thread network: \(result.networkName).")
        }

        guard let first = threadScanResults.first else {
            SwiftLogger.error(tag: Self.tag, "No Thread networks found.")
            throw CommissioningError.noThreadNetwork
        }

        return .network(extendedPANID: first.extendedPANID)
    }
}
