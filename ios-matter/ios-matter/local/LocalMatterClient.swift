//
//  LocalMatterClient.swift
//  ios-matter
//
//  Created by Sylwester Zielinski on 13/08/2026.
//

import Matter

/// Cluster agnostic access to a Matter device in the local fabric.
///
/// Where the `LocalMatter*Controller` types expose one cluster each with its attributes and
/// commands spelled out, this client takes cluster, attribute, and command IDs as parameters, so
/// that a caller can drive any cluster. Values cross the bridge as ``MatterValue``, which carries
/// the Matter type the caller would otherwise have to know.
@objc public final class LocalMatterClient: NSObject {

    /// Reads a single attribute.
    ///
    /// - Parameters:
    ///   - deviceId: The Matter node ID of the target device.
    ///   - endpoint: The endpoint hosting the cluster.
    ///   - cluster: The cluster ID the attribute belongs to.
    ///   - attribute: The attribute ID to read.
    /// - Returns: The attribute value together with its Matter type.
    /// - Throws: An error if the local controller cannot be obtained or the read fails.
    @objc public func readAttribute(deviceId: NSNumber, endpoint: NSNumber, cluster: NSNumber, attribute: NSNumber) async throws -> MatterValue {
        SwiftLogger.debug("Read attribute \(attribute) of cluster \(cluster)")
        let reader = try AttributeReader(deviceId: deviceId)

        return try await reader.readValue(endpoint: endpoint, cluster: cluster, attribute: attribute)
    }

    /// Writes a single attribute.
    ///
    /// - Parameters:
    ///   - deviceId: The Matter node ID of the target device.
    ///   - endpoint: The endpoint hosting the cluster.
    ///   - cluster: The cluster ID the attribute belongs to.
    ///   - attribute: The attribute ID to write.
    ///   - value: The new value, tagged with the attribute's Matter type.
    /// - Throws: An error if the local controller cannot be obtained or the write fails.
    @objc public func writeAttribute(deviceId: NSNumber, endpoint: NSNumber, cluster: NSNumber, attribute: NSNumber, value: MatterValue) async throws {
        SwiftLogger.debug("Write attribute \(attribute) of cluster \(cluster)")
        let writer = try AttributeWriter(deviceId: deviceId)

        try await writer.writeAttribute(endpoint: endpoint, cluster: cluster, attribute: attribute, value: value)
    }

    /// Subscribes to a single attribute.
    ///
    /// The subscription is not cancellable and lives as long as the shared ``AttributeSubscriber``
    /// for this node.
    ///
    /// - Parameters:
    ///   - deviceId: The Matter node ID of the target device.
    ///   - endpoint: The endpoint hosting the cluster.
    ///   - cluster: The cluster ID the attribute belongs to.
    ///   - attribute: The attribute ID to observe.
    ///   - onUpdate: Called on a background queue with every reported value, including once with
    ///     the initial state when the subscription is established.
    /// - Throws: An error if the local controller cannot be obtained.
    @objc public func observeAttribute(deviceId: NSNumber, endpoint: NSNumber, cluster: NSNumber, attribute: NSNumber, onUpdate: @escaping (MatterValue) -> Void) async throws {
        SwiftLogger.debug("Observe attribute \(attribute) of cluster \(cluster)")
        let subscriber = try AttributeSubscriber.shared(deviceId: deviceId)

        subscriber.subscribeToValues(endpoint: endpoint, cluster: cluster, attribute: attribute) { value in
            SwiftLogger.debug("Received report for attribute \(attribute): \(String(describing: value.rawValue))")
            onUpdate(value)
        }
    }

    /// Invokes a cluster command with at most a single field.
    ///
    /// - Parameters:
    ///   - deviceId: The Matter node ID of the target device.
    ///   - endpoint: The endpoint hosting the cluster.
    ///   - cluster: The cluster ID the command belongs to.
    ///   - command: The command ID to invoke.
    ///   - value: The command's single field, or `nil` for commands that take no fields.
    ///   - timedInvokeTimeoutMs: The timed invoke window in milliseconds, or `nil` to send an
    ///     untimed invoke.
    /// - Returns: The first field of the command response, or `nil` if the device answered with a
    ///   status and no data.
    /// - Throws: An error if the local controller cannot be obtained or the command fails.
    @objc public func executeCommand(deviceId: NSNumber, endpoint: NSNumber, cluster: NSNumber, command: NSNumber, value: MatterValue?, timedInvokeTimeoutMs: NSNumber?) async throws -> MatterValue? {
        SwiftLogger.debug("Execute command \(command) of cluster \(cluster)")
        let executor = try CommandExecutor(deviceId: deviceId)

        return try await executor.executeCommand(
            endpoint: endpoint,
            cluster: cluster,
            command: command,
            value: value,
            timedInvokeTimeoutMs: timedInvokeTimeoutMs
        )
    }
}
