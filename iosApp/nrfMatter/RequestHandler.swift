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
/// The system extension scans a commissioning QR code and delegates the add-device flow to the
/// published library (`import shared`). All native Matter work — obtaining the local
/// `MTRDeviceController`, driving `setupCommissioningSession`, storage — happens inside the
/// library's Swift layer via ``MatterExtension``; this target compiles no `SharedCode`/`iosDeps`
/// source of its own and only passes primitives (the onboarding payload string) across.
final class RequestHandler: MatterAddDeviceExtensionRequestHandler {

    /// Returns the list of rooms available in the given home for placing a newly added device.
    ///
    /// - Parameter home: The home to fetch rooms for, or `nil` if no home was selected.
    /// - Returns: The rooms the device can be assigned to, as reported by the library.
    override func rooms(in home: MatterAddDeviceRequest.Home?) async -> [MatterAddDeviceRequest.Room] {
        MatterExtension.shared.log(message: "Received request to fetch rooms in home: \(String(describing: home?.displayName)).")

        return MatterExtension.shared.roomNames().map { MatterAddDeviceRequest.Room(displayName: $0) }
    }

    /// Commissions the device described by the onboarding payload into the given home.
    ///
    /// - Parameters:
    ///   - home: The home the device is being added to, or `nil` if no home was selected.
    ///   - onboardingPayload: The Matter onboarding payload read from the commissioning QR code.
    ///   - commissioningID: The unique identifier for this commissioning attempt.
    /// - Throws: An error if commissioning fails.
    override func commissionDevice(in home: MatterAddDeviceRequest.Home?, onboardingPayload: String, commissioningID: UUID) async throws {
        MatterExtension.shared.log(message: "Commissioning device in home '\(String(describing: home?.displayName))' with payload: \(onboardingPayload).")

        try await MatterExtension.shared.commissionDevice(payload: onboardingPayload)
    }

    /// Finishes configuring a newly added device with its chosen name and room, and records the result in shared storage.
    ///
    /// - Parameters:
    ///   - name: The display name chosen for the device.
    ///   - room: The room the device was placed in, or `nil` if no room was selected.
    override func configureDevice(named name: String, in room: MatterAddDeviceRequest.Room?) async {
        MatterExtension.shared.log(message: "Configuring device '\(name)' in room: \(String(describing: room?.displayName))")

        MatterExtension.shared.finishConfigure()
    }

    /// Validates the device credential presented during commissioning.
    ///
    /// - Parameter deviceCredential: The credential to validate.
    /// - Throws: An error if the credential is invalid.
    override func validateDeviceCredential(_ deviceCredential: MatterAddDeviceExtensionRequestHandler.DeviceCredential) async throws {
        MatterExtension.shared.log(message: "Validating device credential")
    }

    /// Selects a WiFi network for the device to join, from the networks found during scanning.
    ///
    /// - Parameter wifiScanResults: The WiFi networks discovered during scanning.
    /// - Returns: The network association the device should use.
    /// - Throws: An error if no suitable network can be selected.
    override func selectWiFiNetwork(from wifiScanResults: [MatterAddDeviceExtensionRequestHandler.WiFiScanResult]) async throws -> MatterAddDeviceExtensionRequestHandler.WiFiNetworkAssociation {
        MatterExtension.shared.log(message: "Selecting WiFi network from \(wifiScanResults.count) scan results")

        return .defaultSystemNetwork
    }

    /// Selects a Thread network for the device to join, from the networks found during scanning.
    ///
    /// - Parameter threadScanResults: The Thread networks discovered during scanning.
    /// - Returns: The network association the device should use.
    /// - Throws: An error if no suitable network can be selected.
    override func selectThreadNetwork(from threadScanResults: [MatterAddDeviceExtensionRequestHandler.ThreadScanResult]) async throws -> MatterAddDeviceExtensionRequestHandler.ThreadNetworkAssociation {
        MatterExtension.shared.log(message: "Selecting Thread network from \(threadScanResults.count) scan results")

        let scanResult = threadScanResults[0] // .defaultSystemNetwork doesn't work. Selecting first.
        return MatterAddDeviceExtensionRequestHandler.ThreadNetworkAssociation.network(extendedPANID: scanResult.extendedPANID)
    }
}
