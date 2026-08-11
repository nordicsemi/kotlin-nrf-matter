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

/// Namespace for identifiers of the dimmable light device type.
enum DimmableLightDeviceType {
    
    enum OnOffCluster {
        static let id: NSNumber = 0x0006
        enum Attribute {
            static let onOff: NSNumber = 0x0000
        }
    }

    enum LevelControlCluster {
        static let id: NSNumber = 0x0008
        enum Attribute {
            static let currentLevel: NSNumber = 0x0000
        }
    }
}

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
        let controller = try LocalControllerProvider(logTag: "LocalControllerProvider").getController()
        let baseDevice = MTRBaseDevice(nodeID: deviceId.nsNumber(), controller: controller)

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

        let controller = try LocalControllerProvider(logTag: "LocalControllerProvider").getController()
        let baseDevice = MTRBaseDevice(nodeID: deviceId.nsNumber(), controller: controller)

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
    /// - Returns: A flow emitting `OperationResultSuccess` with `true` when the light is on,
    ///   `false` when it is off, or `OperationResultError` if a report could not be read.
    /// - Throws: An error if the local controller cannot be obtained.
    func observeLightState(deviceId: DeviceId, endpoint: Int32) async throws -> any Kotlinx_coroutines_coreFlow {
        SharedLogger.debug("subscribeToLedChanges")
        let flowWrapper = IosFlowWrapper<OperationResult>()
        let observer = try AttributeSubscriber.shared(deviceId: deviceId.nsNumber())

        observer.subscribe(endpoint: endpoint as NSNumber, cluster: DimmableLightDeviceType.OnOffCluster.id, attribute: DimmableLightDeviceType.OnOffCluster.Attribute.onOff) { (result: Bool) in
            SharedLogger.info("Received led state: \(result)")
            flowWrapper.emit(value: OperationResultSuccess(data: KotlinBoolean(bool: result)))
        }

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
    /// - Returns: A flow emitting `OperationResultSuccess` with brightness normalized to `0...1`,
    ///   or `OperationResultError` if a report could not be read.
    /// - Throws: An error if the local controller cannot be obtained.
    func observeBrightnessState(deviceId: DeviceId, endpoint: Int32) async throws -> any Kotlinx_coroutines_coreFlow {
        SharedLogger.debug("subscribeToLightLevelChanges")
        let flowWrapper = IosFlowWrapper<OperationResult>()
        let observer = try AttributeSubscriber.shared(deviceId: deviceId.nsNumber())

        observer.subscribe(endpoint: endpoint as NSNumber, cluster: DimmableLightDeviceType.LevelControlCluster.id, attribute: DimmableLightDeviceType.LevelControlCluster.Attribute.currentLevel) { (rawLevel: Int) in
            SharedLogger.info("Received light level: \(rawLevel)")
            let percent = max(0, min(1, (Float(rawLevel) - 1) / 253))
            SharedLogger.info("Calculated percent: \(percent)")
            flowWrapper.emit(value: OperationResultSuccess(data: KotlinFloat(float: percent)))
        }

        return flowWrapper.flow
    }
}
