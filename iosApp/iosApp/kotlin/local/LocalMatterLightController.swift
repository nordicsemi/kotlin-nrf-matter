//
//  LocalMatterLightController.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 06/03/2026.
//

import ComposeApp
import Matter
import SharedCode
import OSLog

/// Controls a light type Matter device in the local fabric.
class LocalMatterLightController : MatterLightController {
    
    /// Turns the light on or off.
    ///
    /// - Parameters:
    ///   - deviceId: The Matter node ID of the target device.
    ///   - isOn: `true` to turn the light on, `false` to turn it off.
    ///   - endpoint: The endpoint hosting the On/Off cluster.
    /// - Throws: An error if the local controller cannot be obtained or the command fails.
    func setDeviceOnOff(deviceId: DeviceId, isOn: Bool, endpoint: Int32) async throws {
        SharedLogger.debug("Set device on/off = \(isOn)")
        let baseDevice = try BaseDeviceProvider.shared(deviceId: deviceId.nsNumber())

        let cluster = MTRBaseClusterOnOff(device: baseDevice, endpointID: endpoint as NSNumber, queue: DispatchQueue.global())
        SharedLogger.debug("Cluster created: \(String(describing: cluster))")
        if (isOn) {
            try await cluster?.on()
        } else {
            try await cluster?.off()
        }
    }
    
    /// Sets the brightness level via the Level Control cluster.
    ///
    /// - Parameters:
    ///   - deviceId: The Matter node ID of the target device.
    ///   - level: The target level, as defined by the Level Control cluster.
    ///   - endpoint: The endpoint hosting the Level Control cluster.
    /// - Throws: An error if the local controller cannot be obtained or the command fails.
    func setBrightnessLevel(deviceId: DeviceId, brightnessLevel: Int32, endpoint: Int32) async throws {
        SharedLogger.debug("Set brightess level: \(brightnessLevel)")

        let baseDevice = try BaseDeviceProvider.shared(deviceId: deviceId.nsNumber())

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
    /// - Returns: A flow emitting `true` when the light is on, `false` when it is off.
    /// - Throws: An error if the local controller cannot be obtained.
    func observeLightState(deviceId: DeviceId, endpoint: Int32) async throws -> any Kotlinx_coroutines_coreFlow {
        SharedLogger.debug("subscribeToLedChanges")
        let baseDevice = try BaseDeviceProvider.shared(deviceId: deviceId.nsNumber())

        let cluster = MTRBaseClusterOnOff(device: baseDevice, endpointID: endpoint as NSNumber, queue: DispatchQueue.global())
        SharedLogger.info("Cluster: \(String(describing: cluster))")
        
        let flowWrapper = IosFlowWrapper<KotlinBoolean>()
        cluster?.subscribeAttributeOnOff(with: MTRSubscribeParams.defaultParams, subscriptionEstablished: { }, reportHandler: { result, error in
            if let result {
                SharedLogger.info("Received led state: \(result)")
                flowWrapper.emit(value: KotlinBoolean(bool: result.boolValue))
            }
            if let error {
                SharedLogger.debug("Received led on error: \(error)")
            }
        })
        return flowWrapper.flow
    }
    
    /// Subscribes to brightness level changes reported by the device.
    ///
    /// Raw level values from the Level Control cluster are normalized to a `0...1` range
    /// before being delivered.
    ///
    /// - Parameters:
    ///   - deviceId: The Matter node ID of the target device.
    ///   - endpoint: The endpoint hosting the Level Control cluster.
    /// - Returns: A flow emitting brightness normalized to `0...1`.
    /// - Throws: An error if the local controller cannot be obtained.
    func observeBrightnessState(deviceId: DeviceId, endpoint: Int32) async throws -> any Kotlinx_coroutines_coreFlow {
        SharedLogger.debug("subscribeToLightLevelChanges")
        let baseDevice = try BaseDeviceProvider.shared(deviceId: deviceId.nsNumber())

        let cluster = MTRBaseClusterLevelControl(device: baseDevice, endpointID: endpoint as NSNumber, queue: DispatchQueue.global())
        SharedLogger.info("Cluster: \(String(describing: cluster))")
        
        let flowWrapper = IosFlowWrapper<KotlinFloat>()
        cluster?.subscribeAttributeCurrentLevel(with: MTRSubscribeParams.defaultParams, subscriptionEstablished: { }, reportHandler: { result, error in
            if let result {
                SharedLogger.info("Received light level: \(result)")
                let rawLevel = result.intValue
                let percent = max(0, min(1, (Float(rawLevel) - 1) / 253))
                SharedLogger.info("Calculated percent: \(percent)")
                flowWrapper.emit(value: KotlinFloat(float: percent))
            }
            if let error {
                SharedLogger.debug("Received light level error: \(error)")
            }
        })
        return flowWrapper.flow
    }
}
