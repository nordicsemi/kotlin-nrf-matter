//
//  LocalMatterLightController.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 06/03/2026.
//

import Matter
import OSLog

/// Controls a light type Matter device in the local fabric.
@objc public class LocalMatterLightController: NSObject {

    @objc public override init() {}

    /// Turns the light on or off.
    ///
    /// - Parameters:
    ///   - deviceId: The Matter node ID of the target device.
    ///   - isOn: `true` to turn the light on, `false` to turn it off.
    ///   - endpoint: The endpoint hosting the On/Off cluster.
    /// - Throws: An error if the local controller cannot be obtained or the command fails.
    @objc public func setDeviceOnOff(deviceId: String, isOn: Bool, endpoint: Int32) async throws {
        SharedLogger.debug("Set device on/off = \(isOn)")
        let controller = try LocalControllerProvider(logTag: "LocalControllerProvider").getController()
        let baseDevice = MTRBaseDevice(nodeID: deviceId.toMatterNodeId(), controller: controller)

        let cluster = MTRBaseClusterOnOff(device: baseDevice, endpointID: endpoint as NSNumber, queue: DispatchQueue.global())
        SharedLogger.debug("Cluster created: \(String(describing: cluster))")
        if isOn {
            try await cluster?.on()
        } else {
            try await cluster?.off()
        }
    }

    /// Sets the brightness level via the Level Control cluster.
    ///
    /// - Parameters:
    ///   - deviceId: The Matter node ID of the target device.
    ///   - brightnessLevel: The target level, as defined by the Level Control cluster.
    ///   - endpoint: The endpoint hosting the Level Control cluster.
    /// - Throws: An error if the local controller cannot be obtained or the command fails.
    @objc public func setBrightnessLevel(deviceId: String, brightnessLevel: Int32, endpoint: Int32) async throws {
        SharedLogger.debug("Set brightess level: \(brightnessLevel)")

        let controller = try LocalControllerProvider(logTag: "LocalControllerProvider").getController()
        let baseDevice = MTRBaseDevice(nodeID: deviceId.toMatterNodeId(), controller: controller)

        let cluster = MTRBaseClusterLevelControl(
            device: baseDevice,
            endpointID: endpoint as NSNumber,
            queue: DispatchQueue.global()
        )

        SharedLogger.debug("Cluster created: \(String(describing: cluster))")

        let params = MTRLevelControlClusterMoveToLevelParams()
        params.level = NSNumber(value: brightnessLevel)
        try await cluster?.moveToLevel(with: params)
    }

    /// Subscribes to on/off state changes reported by the device.
    ///
    /// - Parameters:
    ///   - deviceId: The Matter node ID of the target device.
    ///   - endpoint: The endpoint hosting the On/Off cluster.
    ///   - onValue: Called with `true` when the light is on, `false` when it is off.
    /// - Throws: An error if the local controller cannot be obtained.
    @objc public func observeLightState(deviceId: String, endpoint: Int32, onValue: @escaping (Bool) -> Void) async throws {
        SharedLogger.debug("subscribeToLedChanges")
        let controller = try LocalControllerProvider(logTag: "LocalControllerProvider").getController()
        let baseDevice = MTRBaseDevice(nodeID: deviceId.toMatterNodeId(), controller: controller)

        let cluster = MTRBaseClusterOnOff(device: baseDevice, endpointID: endpoint as NSNumber, queue: DispatchQueue.global())
        SharedLogger.info("Cluster: \(String(describing: cluster))")

        cluster?.subscribeAttributeOnOff(with: MTRSubscribeParams.defaultParams, subscriptionEstablished: { }, reportHandler: { result, error in
            if let result {
                SharedLogger.info("Received led state: \(result)")
                onValue(result.boolValue)
            }
            if let error {
                SharedLogger.debug("Received led on error: \(error)")
            }
        })
    }

    /// Subscribes to brightness level changes reported by the device.
    ///
    /// Raw level values from the Level Control cluster are normalized to a `0...1` range
    /// before being delivered.
    ///
    /// - Parameters:
    ///   - deviceId: The Matter node ID of the target device.
    ///   - endpoint: The endpoint hosting the Level Control cluster.
    ///   - onValue: Called with brightness normalized to `0...1`.
    /// - Throws: An error if the local controller cannot be obtained.
    @objc public func observeBrightnessState(deviceId: String, endpoint: Int32, onValue: @escaping (Float) -> Void) async throws {
        SharedLogger.debug("subscribeToLightLevelChanges")
        let controller = try LocalControllerProvider(logTag: "LocalControllerProvider").getController()
        let baseDevice = MTRBaseDevice(nodeID: deviceId.toMatterNodeId(), controller: controller)

        let cluster = MTRBaseClusterLevelControl(device: baseDevice, endpointID: endpoint as NSNumber, queue: DispatchQueue.global())
        SharedLogger.info("Cluster: \(String(describing: cluster))")

        cluster?.subscribeAttributeCurrentLevel(with: MTRSubscribeParams.defaultParams, subscriptionEstablished: { }, reportHandler: { result, error in
            if let result {
                SharedLogger.info("Received light level: \(result)")
                let rawLevel = result.intValue
                let percent = max(0, min(1, (Float(rawLevel) - 1) / 253))
                SharedLogger.info("Calculated percent: \(percent)")
                onValue(percent)
            }
            if let error {
                SharedLogger.debug("Received light level error: \(error)")
            }
        })
    }
}
