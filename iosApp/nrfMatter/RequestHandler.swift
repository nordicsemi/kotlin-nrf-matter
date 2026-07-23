//
//  RequestHandler.swift
//  nrfMatter
//
//  Created by Sylwester Zielinski on 24/02/2026.
//

import MatterSupport
import Matter

/// Entry point class for the Matter "Add Device" app extension.
///
/// The system extension scans a commissioning QR code and delegates the add-device flow to a
/// ``RequestHandlerProtocol`` implementation; currently only ``LocalRequestHandler`` is used, which
/// adds a device to a Matter fabric that already exists on the phone.
///
/// The extension communicates with the app using a callback-based approach: consuming the payload
/// read from the QR code, providing a list of rooms and homes the user may add their device to, and
/// selecting the WiFi or Thread network the device will operate on.
final class RequestHandler: MatterAddDeviceExtensionRequestHandler {
    
    private let handler: RequestHandlerProtocol = {
        let storage = SharedStorage(suitName: SharedConsts.sharedStorage)
        let value = storage.getString(key: SharedConsts.matterEnvStorageKey)
        let env = MatterEnv(rawValue: value!)
        
        return switch env {
        case .local:
            LocalRequestHandler()
        default:
            fatalError("Invalid environment")
        }
    }()

    /// Returns the list of rooms available in the given home for placing a newly added device.
    ///
    /// - Parameter home: The home to fetch rooms for, or `nil` if no home was selected.
    /// - Returns: The rooms the device can be assigned to, as reported by the active handler.
    override func rooms(in home: MatterAddDeviceRequest.Home?) async -> [MatterAddDeviceRequest.Room] {
        SharedLogger.info("Received request to fetch rooms in home: \(String(describing: home?.displayName)).")

        return await handler.rooms(in: home)
    }

    /// Commissions the device described by the onboarding payload into the given home.
    ///
    /// - Parameters:
    ///   - home: The home the device is being added to, or `nil` if no home was selected.
    ///   - onboardingPayload: The Matter onboarding payload read from the commissioning QR code.
    ///   - commissioningID: The unique identifier for this commissioning attempt.
    /// - Throws: An error if commissioning fails.
    override func commissionDevice(in home: MatterAddDeviceRequest.Home?, onboardingPayload: String, commissioningID: UUID) async throws {
        SharedLogger.info("Commissioning device in home '\(String(describing: home?.displayName))' with payload: \(onboardingPayload).")

        try await handler.commissionDevice(in: home, onboardingPayload: onboardingPayload, commissioningID: commissioningID)
    }

    /// Finishes configuring a newly added device with its chosen name and room, and records the result in shared storage.
    ///
    /// - Parameters:
    ///   - name: The display name chosen for the device.
    ///   - room: The room the device was placed in, or `nil` if no room was selected.
    override func configureDevice(named name: String, in room: MatterAddDeviceRequest.Room?) async {
        SharedLogger.info("Configuring device '\(name)' in room: \(String(describing: room?.displayName))")

        await handler.configureDevice(named: name, in: room)

        let storage = SharedStorage(suitName: SharedConsts.sharedStorage)
        storage.storeBool(key: SharedConsts.resultKey, value: true)
    }

    /// Validates the device credential presented during commissioning.
    ///
    /// - Parameter deviceCredential: The credential to validate.
    /// - Throws: An error if the credential is invalid.
    override func validateDeviceCredential(_ deviceCredential: MatterAddDeviceExtensionRequestHandler.DeviceCredential) async throws {
        SharedLogger.info("Validating device credential")

        try await handler.validateDeviceCredential(deviceCredential)
    }

    /// Selects a WiFi network for the device to join, from the networks found during scanning.
    ///
    /// - Parameter wifiScanResults: The WiFi networks discovered during scanning.
    /// - Returns: The network association the device should use.
    /// - Throws: An error if no suitable network can be selected.
    override func selectWiFiNetwork(from wifiScanResults: [MatterAddDeviceExtensionRequestHandler.WiFiScanResult]) async throws -> MatterAddDeviceExtensionRequestHandler.WiFiNetworkAssociation {
        SharedLogger.info("Selecting WiFi network from \(wifiScanResults.count) scan results")

        return try await handler.selectWiFiNetwork(from: wifiScanResults)
    }

    /// Selects a Thread network for the device to join, from the networks found during scanning.
    ///
    /// - Parameter threadScanResults: The Thread networks discovered during scanning.
    /// - Returns: The network association the device should use.
    /// - Throws: An error if no suitable network can be selected.
    override func selectThreadNetwork(from threadScanResults: [MatterAddDeviceExtensionRequestHandler.ThreadScanResult]) async throws -> MatterAddDeviceExtensionRequestHandler.ThreadNetworkAssociation {
        SharedLogger.info("Selecting Thread network from \(threadScanResults.count) scan results")
        
        threadScanResults.forEach { item in
            SharedLogger.debug("Detected thread network: \(item.networkName)")
        }

        return try await handler.selectThreadNetwork(from: threadScanResults)
    }
}
