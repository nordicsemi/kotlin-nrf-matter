//
//  MyMatterSupport.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 23/02/2026.
//

import Matter
import MatterSupport
import SharedCode

/// Commissions a new Matter device into the local fabric.
///
/// A new device can be commissioned over Wi-Fi or Thread. Commissioning a Thread device
/// requires a Thread Border Router available on the local network and Thread network
/// credentials stored on the phone. Once commissioned, the device is added to the local fabric
/// and managed by the phone.
@objc public class LocalMatterCommissioner: NSObject {

    @objc public override init() {}

    /// Commissions a new Matter device into the local fabric using Apple's MatterSupport
    /// add-device flow.
    ///
    /// During the process, control moves to the system app extension, which provides the UI
    /// for scanning the QR code and choosing among available Thread networks. The local fabric
    /// is shared between the main app and the app extension via App Groups.
    ///
    /// After successful commissioning, the descriptor clusters for all available endpoints are
    /// read and the resulting device metadata is returned.
    ///
    /// - Parameter deviceId: The Matter node ID to assign to the newly commissioned device.
    /// - Returns: The discovered `SwiftDevice` on success.
    /// - Throws: A `SwiftCommissioningError` describing the failure (commissioning cancelled or
    ///   failed, or post-commissioning cluster discovery failed).
    @objc public func startIosCommissioning(deviceId: String) async throws -> SwiftDevice {
        let homes = [MatterAddDeviceRequest.Home(displayName: "Nordic Home")]
        let topology = MatterAddDeviceRequest.Topology(ecosystemName: "Nordic Ecosystem", homes: homes)

        let request = MatterAddDeviceRequest(topology: topology, shouldScanNetworks: true)

        let storage = SharedStorage(suitName: SharedConsts.sharedStorage)
        let _ = storage.removeStorageData(forKey: SharedConsts.resultKey)
        storage.storeString(key: SharedConsts.matterEnvStorageKey, value: MatterEnv.local.rawValue)
        storage.storeNumber(key: SharedConsts.nodeIdKey, value: deviceId.toMatterNodeId())

        do {
            try await request.perform()
        } catch {
            let error = error as NSError
            throw SwiftCommissioningError(
                stage: 0, // COMMISSIONING
                errorCode: NSNumber(value: error.code),
                displayMessage: error.localizedDescription,
                fabricId: 1
            )
        }

        let result = storage.getBool(key: SharedConsts.resultKey) ?? false
        guard result else {
            throw SwiftCommissioningError(
                stage: 0, // COMMISSIONING
                errorCode: nil,
                displayMessage: "Cancelled.",
                fabricId: 1
            )
        }

        let descriptorCluster = LocalMatterClusterDiscovery(nodeId: deviceId.toMatterNodeId())

        do {
            return try await descriptorCluster.discoverClusters()
        } catch {
            let error = error as NSError
            throw SwiftCommissioningError(
                stage: descriptorCluster.stage.rawValue,
                errorCode: NSNumber(value: error.code),
                displayMessage: error.localizedDescription,
                fabricId: 1
            )
        }
    }
}
