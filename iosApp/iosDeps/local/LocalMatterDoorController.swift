//
//  LocalMatterDoorController.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 25/03/2026.
//

import Matter
import OSLog

/// Controls a door-lock type Matter device in the local fabric.
@objc public class LocalMatterDoorController: NSObject {

    @objc public override init() {}

    /// Locks or unlocks the door.
    ///
    /// - Parameters:
    ///   - deviceId: The Matter node ID of the target device.
    ///   - isLocked: `true` to lock the door, `false` to unlock it.
    ///   - endpoint: The endpoint hosting the Door Lock cluster.
    /// - Throws: An error if the local controller cannot be obtained or the command fails.
    @objc public func lockUnlockDoor(deviceId: String, isLocked: Bool, endpoint: Int32) async throws {
        SharedLogger.debug(#function)
        let controller = try LocalControllerProvider(logTag: "LocalControllerProvider").getController()
        let baseDevice = MTRBaseDevice(nodeID: deviceId.toMatterNodeId(), controller: controller)

        let cluster = MTRBaseClusterDoorLock(device: baseDevice, endpointID: endpoint as NSNumber, queue: DispatchQueue.global())
        if isLocked {
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
    ///   - onValue: Called with the raw `LockDeviceState` value as reported by the cluster.
    /// - Throws: An error if the local controller cannot be obtained.
    @objc public func observeLockState(deviceId: String, endpoint: Int32, onValue: @escaping (Int32) -> Void) async throws {
        SharedLogger.debug(#function)
        let controller = try LocalControllerProvider(logTag: "LocalControllerProvider").getController()
        let baseDevice = MTRBaseDevice(nodeID: deviceId.toMatterNodeId(), controller: controller)

        let cluster = MTRBaseClusterDoorLock(device: baseDevice, endpointID: endpoint as NSNumber, queue: DispatchQueue.global())

        cluster!.subscribeAttributeLockState(with: MTRSubscribeParams.defaultParams, subscriptionEstablished: { }, reportHandler: { result, error in
            if let result {
                SharedLogger.debug("Received door lock state: \(result)")
                onValue(result.int32Value)
            }
            if let error {
                SharedLogger.debug("Received door lock error: \(error)")
            }
        })
    }
}
