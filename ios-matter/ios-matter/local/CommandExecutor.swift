//
//  CommandExecutor.swift
//  ios-matter
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

        _ = try await invoke(endpoint: endpoint, cluster: cluster, command: command, fields: fields)
    }

    /// Invokes a cluster command with at most a single field and returns its response.
    ///
    /// - Parameters:
    ///   - endpoint: The endpoint ID hosting the cluster.
    ///   - cluster: The cluster ID the command belongs to.
    ///   - command: The command ID to invoke.
    ///   - value: The command's single field, or `nil` for commands that take no fields, such as
    ///     the On/Off cluster's `On`.
    ///   - timedInvokeTimeoutMs: The timed invoke window in milliseconds, or `nil` to send an
    ///     untimed invoke.
    /// - Returns: The first field of the command response, or `nil` if the device answered with a
    ///   status and no data.
    /// - Throws: An error if the value cannot be encoded or if the command invocation fails.
    func executeCommand(endpoint: NSNumber, cluster: NSNumber, command: NSNumber, value: MatterValue?, timedInvokeTimeoutMs: NSNumber? = nil) async throws -> MatterValue? {
        var commandFields: [[String: Any]] = []

        if let value {
            commandFields = [[MTRContextTagKey: 0, MTRDataKey: try value.mtrDictionary()]]
        }

        let fields: NSDictionary = [
            MTRTypeKey: MTRStructureValueType,
            MTRValueKey: commandFields
        ]

        return try await invoke(
            endpoint: endpoint,
            cluster: cluster,
            command: command,
            fields: fields,
            timedInvokeTimeoutMs: timedInvokeTimeoutMs
        )
    }

    private func invoke(endpoint: NSNumber, cluster: NSNumber, command: NSNumber, fields: NSDictionary, timedInvokeTimeoutMs: NSNumber? = nil) async throws -> MatterValue? {
        SwiftLogger.debug("Executing command: \(command), timed invoke timeout: \(String(describing: timedInvokeTimeoutMs))")

        let response: [[String: Any]]? = try await baseDevice.invokeCommand(
            withEndpointID: endpoint,
            clusterID: cluster,
            commandID: command,
            commandFields: fields,
            timedInvokeTimeout: timedInvokeTimeoutMs,
            queue: DispatchQueue.global()
        )

        SwiftLogger.debug("Command executed successfully.")

        return response?.first?.readCommandResponseValue()
    }
}
