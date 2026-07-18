//
//  MyMatterSupport.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 23/02/2026.
//

import ComposeApp
import MatterSupport
import nrfMatter
import SharedCode

/// Commissions a new Matter device into the local fabric.
///
/// A new device can be commissioned over Wi-Fi or Thread. Commissioning a Thread device
/// requires a Thread Border Router available on the local network and Thread network
/// credentials stored on the phone. Once commissioned, the device is added to the local fabric
/// and managed by the phone.
class LocalMatterCommissioner : MatterCommissioner {

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
    /// - Returns: An `OperationResultSuccess` containing the discovered `Device` on success, or
    ///   an `OperationResultError` describing the failure.
    /// - Throws: An error if the local controller needed for post-commissioning cluster
    ///   discovery cannot be obtained.
    func startIosCommissioning(deviceId: DeviceId) async throws -> any OperationResult {
        let homes = [MatterAddDeviceRequest.Home(displayName: "Nordic Home")]
        let topology = MatterAddDeviceRequest.Topology(ecosystemName: "Nordic Ecosystem", homes: homes)
        
        let request = MatterAddDeviceRequest(topology: topology, shouldScanNetworks: true)
        
        let storage = SharedStorage(suitName: SharedConsts.sharedStorage)
        let _ = storage.removeStorageData(forKey: SharedConsts.resultKey)
        storage.storeString(key: SharedConsts.matterEnvStorageKey, value: MatterEnv.local.rawValue)
        storage.storeNumber(key: SharedConsts.nodeIdKey, value: deviceId.nsNumber())
        
        do {
            try await request.perform()
        } catch {
            let error = error as NSError
            return OperationResultError(t: CommissioningException(
                deviceId: deviceId,
                stage: Stage.commissioning,
                errorCode: KotlinInt(int: Int32(error.code)),
                displayMessage: error.localizedDescription,
                fabricId: 1
            ))
        }
        
        let result = storage.getBool(key: SharedConsts.resultKey) ?? false
        guard result else {
            return OperationResultError(t: CommissioningException(
                deviceId: deviceId,
                stage: Stage.commissioning,
                errorCode: nil,
                displayMessage: "Cancelled.",
                fabricId: 1
            ))
        }
        
        let discovery = IosClusterDiscovery()

        do {
            let device = try await discovery.discoverClusters(deviceId: deviceId)
            return OperationResultSuccess(data: device)
        } catch {
            let error = error as NSError
            return OperationResultError(t: CommissioningException(
                deviceId: deviceId,
                stage: discovery.stage,
                errorCode: KotlinInt(int: Int32(error.code)),
                displayMessage: error.localizedDescription,
                fabricId: 1
            ))
        }
    }
}
