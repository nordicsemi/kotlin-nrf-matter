//
//  NordicMatterRequestHandler.swift
//  ios-matter
//

import Matter
import MatterSupport

@objc(NordicMatterRequestHandler)
final class NordicMatterRequestHandler: MatterAddDeviceExtensionRequestHandler {

    private static let tag = "AddDeviceExtension"

    private static let defaultRooms = [
        "Living Room", "Bedroom", "Office", "Kitchen", "Dining Room",
    ]

    private static let commissioner = MatterCommissioner()

    private let storage = SharedStorage()

    override func rooms(
        in home: MatterAddDeviceRequest.Home?
    ) async -> [MatterAddDeviceRequest.Room] {
        let names = storage.getStringArray(key: SharedConsts.roomsKey) ?? Self.defaultRooms
        SwiftLogger.info(tag: Self.tag, "Offering \(names.count) rooms.")

        return names.map { MatterAddDeviceRequest.Room(displayName: $0) }
    }

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

    override func configureDevice(
        named name: String,
        in room: MatterAddDeviceRequest.Room?
    ) async {
        SwiftLogger.info(tag: Self.tag, "Device configured as \"\(name)\". Reporting success.")

        storage.storeString(key: SharedConsts.deviceNameKey, value: name)
        storage.storeBool(key: SharedConsts.resultKey, value: true)
        Self.commissioner.releaseCommissioner()
    }

    override func validateDeviceCredential(
        _ deviceCredential: MatterAddDeviceExtensionRequestHandler.DeviceCredential
    ) async throws {
    }

    override func selectWiFiNetwork(
        from wifiScanResults: [MatterAddDeviceExtensionRequestHandler.WiFiScanResult]
    ) async throws -> MatterAddDeviceExtensionRequestHandler.WiFiNetworkAssociation {
        return .defaultSystemNetwork
    }

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
