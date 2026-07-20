//
//  LocalMatterDoorController.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 25/03/2026.
//

import ComposeApp
import Matter
import SharedCode
import OSLog

/// Controls a door-lock type Matter device in the local fabric.
class LocalMatterDoorController : MatterDoorLockController {

    /// Locks or unlocks the door.
    ///
    /// - Parameters:
    ///   - deviceId: The Matter node ID of the target device.
    ///   - isLocked: `true` to lock the door, `false` to unlock it.
    ///   - endpoint: The endpoint hosting the Door Lock cluster.
    /// - Throws: An error if the local controller cannot be obtained or the command fails.
    func lockUnlockDoor(deviceId: DeviceId, isLocked: Bool, endpoint: Int32) async throws {
        SharedLogger.debug(#function)
        let baseDevice = try BaseDeviceProvider.shared(deviceId: deviceId.nsNumber())

        let cluster = MTRBaseClusterDoorLock(device: baseDevice, endpointID: endpoint as NSNumber, queue: DispatchQueue.global())
        if (isLocked) {
            try await cluster?.lockDoor()
        } else {
            try await cluster?.unlockDoor()
        }
    }
    
    /// Subscribes to door lock state changes reported by the device.
    ///
    /// - Parameters:
    ///   - deviceId: The Matter node ID of the target device.
    ///   - endpoint: The endpoint hosting the Door Lock cluster.
    /// - Returns: A flow emitting the current lock state.
    /// - Throws: An error if the local controller cannot be obtained.
    func observeLockState(deviceId: DeviceId, endpoint: Int32) async throws -> any Kotlinx_coroutines_coreFlow {
        SharedLogger.debug(#function)
        let baseDevice = try BaseDeviceProvider.shared(deviceId: deviceId.nsNumber())

        let cluster = MTRBaseClusterDoorLock(device: baseDevice, endpointID: endpoint as NSNumber, queue: DispatchQueue.global())
        
        let flowWrapper = IosFlowWrapper<LockDeviceState>()
        cluster!.subscribeAttributeLockState(with: MTRSubscribeParams.defaultParams, subscriptionEstablished: { }, reportHandler: { result, error in
            if let result {
                SharedLogger.debug("Received door lock state: \(result)")
                flowWrapper.emit(value: LockDeviceState.companion.create(value: result.int32Value))
            }
            if let error {
                SharedLogger.debug("Received door lock error: \(error)")
            }
        })
        return flowWrapper.flow
    }
}
