//
//  RequestHandler.swift
//  nrfMatter
//
//  Created by Sylwester Zielinski on 24/02/2026.
//

import MatterSupport
import Matter
import shared

/// Entry point class for the Matter "Add Device" app extension.
///
/// The system extension provides the UI that scans the commissioning QR code, then calls back into
/// these overrides: consuming the payload read from the QR code, providing a list of rooms and homes
/// the user may add their device to, and selecting the WiFi or Thread network the device will
/// operate on.
///
/// Every step is delegated straight to `NordicMatters`/`Fabric`, the same shared Kotlin API
/// (reached through the `shared` framework) the rest of the app uses — this target holds no
/// commissioning logic of its own. The extension-specific pieces (driving `ios-matter`'s
/// `MatterCommissioner`, and reading/writing `SharedStorage`) live as iOS-only extension functions
/// on those two types, declared in `NordicMattersAppExtension.kt`, right next to the types they
/// extend rather than in a private wrapper class.
///
/// The extension runs in its own process, so it exchanges data with the main app through
/// `SharedStorage` (`UserDefaults` over an app group): the app writes the node ID to commission
/// before starting the flow, and ``configureDevice(named:in:)`` writes back the success flag the app
/// reads once the extension closes.
final class RequestHandler: MatterAddDeviceExtensionRequestHandler {

    /// The fabric devices are commissioned onto. Fetching it also installs Kotlin-side logging for
    /// this process — every process running Kotlin code has to do that once for itself.
    private let fabric: Fabric = {
        NordicMatters.shared.initializeAppExtension()
        return NordicMatters.shared.defaultFabric
    }()

    /// The device commissioned in ``commissionDevice(in:onboardingPayload:commissioningID:)``,
    /// carried over to ``configureDevice(named:in:)``.
    private var commissionedDeviceId: DeviceId?

    /// Returns the list of rooms available in the given home for placing a newly added device.
    ///
    /// - Parameter home: The home to fetch rooms for. Ignored — the room list is a fixed set.
    /// - Returns: The rooms the device can be assigned to.
    override func rooms(in home: MatterAddDeviceRequest.Home?) async -> [MatterAddDeviceRequest.Room] {
        return NordicMatters.shared.appExtensionRooms().map { MatterAddDeviceRequest.Room(displayName: $0) }
    }

    /// Commissions the device described by the onboarding payload into the given home.
    ///
    /// - Parameters:
    ///   - home: The home the device is being added to, or `nil` if no home was selected.
    ///   - onboardingPayload: The Matter onboarding payload read from the commissioning QR code.
    ///   - commissioningID: The unique identifier for this commissioning attempt.
    /// - Throws: An error if commissioning fails.
    override func commissionDevice(in home: MatterAddDeviceRequest.Home?, onboardingPayload: String, commissioningID: UUID) async throws {
        commissionedDeviceId = try await fabric.commissionAppExtensionDevice(payload: onboardingPayload)
    }

    /// Finishes configuring a newly added device with its chosen name and room: registers it with
    /// `NordicMatters` under that name, and records the result in shared storage.
    ///
    /// - Parameters:
    ///   - name: The display name chosen for the device.
    ///   - room: The room the device was placed in, or `nil` if no room was selected. Not passed on
    ///     — `NordicMatters` has no notion of rooms yet.
    ///
    /// The call is discarded with `try?` because this override cannot throw: the Kotlin side
    /// already swallows and logs registration failures, so the only error left to reach here is
    /// cancellation, and there is nothing to report it to.
    override func configureDevice(named name: String, in room: MatterAddDeviceRequest.Room?) async {
        guard let deviceId = commissionedDeviceId else {
            return
        }
        try? await fabric.configureAppExtensionDevice(deviceId: deviceId, name: name)
    }

    /// Accepts the device credential presented during commissioning without validating it.
    ///
    /// Returning without throwing tells the system every credential is acceptable, which is what
    /// this app wants: it commissions development kits and simulated devices whose certificates are
    /// not signed by a production attestation authority. ``MatterAttestationDelegate`` takes the
    /// same stance for the attestation step.
    ///
    /// - Parameter deviceCredential: The credential presented by the device. Ignored.
    override func validateDeviceCredential(_ deviceCredential: MatterAddDeviceExtensionRequestHandler.DeviceCredential) async throws {
    }

    /// Selects a WiFi network for the device to join.
    ///
    /// Always defers to the network the phone is already on, so the scan results are unused.
    ///
    /// - Parameter wifiScanResults: The WiFi networks discovered during scanning. Ignored.
    /// - Returns: `.defaultSystemNetwork`.
    override func selectWiFiNetwork(from wifiScanResults: [MatterAddDeviceExtensionRequestHandler.WiFiScanResult]) async throws -> MatterAddDeviceExtensionRequestHandler.WiFiNetworkAssociation {

        return .defaultSystemNetwork
    }

    /// Selects a Thread network for the device to join, from the networks found during scanning.
    ///
    /// Logs every network found, then picks the first one by extended PAN ID.
    /// `.defaultSystemNetwork` is not usable here, so there is no way to defer the choice to the
    /// system.
    ///
    /// - Parameter threadScanResults: The Thread networks discovered during scanning. Must not be
    ///   empty — the first entry is read unconditionally.
    /// - Returns: An association naming the first scanned network's extended PAN ID.
    override func selectThreadNetwork(from threadScanResults: [MatterAddDeviceExtensionRequestHandler.ThreadScanResult]) async throws -> MatterAddDeviceExtensionRequestHandler.ThreadNetworkAssociation {

        let networkNames = threadScanResults.map { $0.networkName }
        NordicMatters.shared.onAppExtensionThreadNetworksDetected(names: networkNames)

        let scanResult = threadScanResults[0] // .defaultSystemNetwork doesn't work. Selecting first.
        return MatterAddDeviceExtensionRequestHandler.ThreadNetworkAssociation.network(extendedPANID: scanResult.extendedPANID)
    }
}
