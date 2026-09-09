//
//  RequestHandler.swift
//  nrfMatter
//

import MatterSupport
import shared

final class RequestHandler: MatterAddDeviceExtensionRequestHandler {

    private let fabric: Fabric = {
        NordicMatters.shared.initializeAppExtension()
        return NordicMatters.shared.defaultFabric
    }()

    override func rooms(in home: MatterAddDeviceRequest.Home?) async -> [MatterAddDeviceRequest.Room] {
        return NordicMatters.shared.appExtensionRooms()
            .map { MatterAddDeviceRequest.Room(displayName: $0) }
    }

    override func commissionDevice(in home: MatterAddDeviceRequest.Home?, onboardingPayload: String, commissioningID: UUID) async throws {
        try await fabric.commissionAppExtensionDevice(payload: onboardingPayload)
    }

    override func configureDevice(named name: String, in room: MatterAddDeviceRequest.Room?) async {
        fabric.configureAppExtensionDevice(name: name)
    }

    override func validateDeviceCredential(_ deviceCredential: MatterAddDeviceExtensionRequestHandler.DeviceCredential) async throws {
    }

    override func selectWiFiNetwork(from wifiScanResults: [MatterAddDeviceExtensionRequestHandler.WiFiScanResult]) async throws -> MatterAddDeviceExtensionRequestHandler.WiFiNetworkAssociation {
        return .defaultSystemNetwork
    }

    override func selectThreadNetwork(from threadScanResults: [MatterAddDeviceExtensionRequestHandler.ThreadScanResult]) async throws -> MatterAddDeviceExtensionRequestHandler.ThreadNetworkAssociation {
        NordicMatters.shared.onAppExtensionThreadNetworksDetected(
            names: threadScanResults.map { $0.networkName }
        )

        return .network(extendedPANID: threadScanResults[0].extendedPANID)
    }
}
