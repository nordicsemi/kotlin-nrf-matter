//
//  CommandExecutor.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 23/04/2026.
//

import Matter

/// Invokes cluster commands on a Matter device over the local (on-device) controller.
class CommandExecutor {

    private let baseDevice: MTRBaseDevice

    /// Creates an executor for the device with the given node ID.
    ///
    /// - Parameter deviceId: The Matter node ID of the target device.
    /// - Throws: An error if the local controller cannot be obtained.
    init(deviceId: NSNumber) throws {
        let controller = try LocalControllerProvider(logTag: "AttributeWriter").getController()
        baseDevice = MTRBaseDevice(nodeID: deviceId, controller: controller)
    }

    /// Invokes a cluster command with a single structured field value.
    ///
    /// - Parameters:
    ///   - endpoint: The endpoint ID hosting the cluster.
    ///   - cluster: The cluster ID the command belongs to.
    ///   - command: The command ID to invoke.
    ///   - type: The MTR value type of the command field (e.g. `MTRBooleanValueType`).
    ///   - value: The value to send for the command field.
    /// - Throws: An error if the command invocation fails.
    func executeCommand(endpoint: NSNumber, cluster: NSNumber, command: NSNumber, type: String, value: Any) async throws {
        SharedLogger.debug("Executing command: \(command)")

        let fields: NSDictionary = [
            MTRTypeKey: MTRStructureValueType,
            MTRValueKey: [
                [
                    MTRContextTagKey: 0,
                    MTRDataKey: [
                        MTRTypeKey: type,
                        MTRValueKey: value
                    ]
                ]
            ]
        ]
        
        try await baseDevice.invokeCommand(withEndpointID: endpoint, clusterID: cluster, commandID: command, commandFields: fields, timedInvokeTimeout: nil, queue: DispatchQueue.global())
        
        SharedLogger.debug("Command executed successfully.")
    }
}
