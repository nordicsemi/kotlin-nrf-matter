//
//  LocalRequestHandler.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 16/03/2026.
//

import MatterSupport
import Matter
import OSLog

/// `RequestHandlerProtocol` implementation that commissions a device into a Matter fabric that
/// already exists locally on the phone.
final class LocalRequestHandler: RequestHandlerProtocol {

    private let commissioner = MatterCommissioner()

    /// Returns a fixed list of rooms a device can be placed in.
    ///
    /// - Parameter home: Unused in the current implementation; rooms are not filtered by home.
    /// - Returns: A static set of common room names.
    func rooms(in home: MatterAddDeviceRequest.Home?) async -> [MatterAddDeviceRequest.Room] {
        let rooms: [String] = ["Living Room", "Bedroom", "Office", "Kitchen", "Dining Room"]
        return rooms.map { MatterAddDeviceRequest.Room(displayName: $0) }
    }

    /// Looks up the target node ID from shared storage and commissions the device described by the onboarding payload.
    ///
    /// - Parameters:
    ///   - home: Unused in the current implementation.
    ///   - onboardingPayload: The Matter onboarding payload read from the commissioning QR code.
    ///   - commissioningID: Unused in the current implementation.
    /// - Throws: ``CommissioningError/missingNodeId`` if no node ID is available in shared storage, or an error if commissioning fails.
    func commissionDevice(in home: MatterAddDeviceRequest.Home?, onboardingPayload: String, commissioningID: UUID) async throws {
        let storage = SharedStorage(suitName: SharedConsts.sharedStorage)
        guard let nodeId = storage.getNumber(key: SharedConsts.nodeIdKey) else {
            throw CommissioningError.missingNodeId
        }
        try await commissioner.commission(payload: onboardingPayload, nodeID: nodeId)
    }

    /// Releases the underlying Matter controller now that device configuration is complete.
    ///
    /// - Parameters:
    ///   - name: Unused in the current implementation.
    ///   - room: Unused in the current implementation.
    func configureDevice(named name: String, in room: MatterAddDeviceRequest.Room?) async {
        commissioner.release()
    }

    /// Validates the device credential presented during commissioning.
    ///
    /// - Parameter deviceCredential: Unused in the current implementation.
    /// - Throws: Never throws in the current implementation; all credentials are accepted.
    func validateDeviceCredential(_ deviceCredential: MatterAddDeviceExtensionRequestHandler.DeviceCredential) async throws {

    }

    /// Selects the WiFi network for the device to join.
    ///
    /// - Parameter wifiScanResults: Unused in the current implementation.
    /// - Returns: The system's default network association, regardless of the scan results.
    /// - Throws: Never throws in the current implementation.
    func selectWiFiNetwork(from wifiScanResults: [MatterAddDeviceExtensionRequestHandler.WiFiScanResult]) async throws -> MatterAddDeviceExtensionRequestHandler.WiFiNetworkAssociation {
        return .defaultSystemNetwork
    }

    /// Selects the Thread network for the device to join.
    ///
    /// - Parameter threadScanResults: The Thread networks discovered during scanning.
    /// - Returns: A network association built from the first scan result's extended PAN ID.
    /// - Throws: Never throws in the current implementation.
    func selectThreadNetwork(from threadScanResults: [MatterAddDeviceExtensionRequestHandler.ThreadScanResult]) async throws -> MatterAddDeviceExtensionRequestHandler.ThreadNetworkAssociation {
        let scanResult = threadScanResults[0] // .defaultSystemNetwork doesn't work. Selecting first.
        return MatterAddDeviceExtensionRequestHandler.ThreadNetworkAssociation.network(extendedPANID: scanResult.extendedPANID)
    }
}
