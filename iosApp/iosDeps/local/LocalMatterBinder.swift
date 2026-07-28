//
//  LocalMatterBinder.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 25/03/2026.
//

import Matter

/// Binds Matter device clusters directly to each other for device-to-device
/// control, bypassing the controller.
@objc public class LocalMatterBinder: NSObject {

    @objc public override init() {}

    /// Binds a source device and a target device together.
    ///
    /// The source device will send commands directly to the target without going through a
    /// controller. The set of supported commands is defined by the cluster, which is declared
    /// as a client cluster on the source and as a server cluster on the target.
    ///
    /// - Parameters:
    ///   - sourceNodeId: The node ID of the device that will send commands.
    ///   - sourceEndpoint: The endpoint on the source device that hosts the client cluster.
    ///   - targetNodeId: The node ID of the device that will receive commands.
    ///   - targetEndpoint: The endpoint on the target device that hosts the server cluster.
    ///   - clusterId: The cluster ID used for the binding.
    /// - Throws: An error if granting access on target or creating the binding on source fails.
    @objc public func bind(sourceNodeId: String, sourceEndpoint: Int32, targetNodeId: String, targetEndpoint: Int32, clusterId: Int64) async throws {
        SharedLogger.info("Binding clusters...")
        SharedLogger.debug("Source node id: \(sourceNodeId)")
        SharedLogger.debug("Target node it: \(targetNodeId)")

        let source = sourceNodeId.toMatterNodeId()
        let target = targetNodeId.toMatterNodeId()
        let sourceEnd = sourceEndpoint as NSNumber
        let targetEnd = targetEndpoint as NSNumber
        let cluster = clusterId as NSNumber

        let controller = try LocalControllerProvider(logTag: "LocalMatterBinder").getController()
        SharedLogger.info("Granting access to source.")
        try await grantAccessToSource(targetDeviceID: target, sourceNodeID: source, clusterID: cluster, controller: controller)
        SharedLogger.info("Preparing binding.")
        try await bindSwitchToBulb(sourceDeviceID: source, sourceEndpoint: sourceEnd, targetNodeID: target, targetEndpoint: targetEnd, clusterID: cluster, controller: controller)
        SharedLogger.info("Binding successful.")
    }

    /// Grants a source device access to a target device's cluster via an ACL entry.
    ///
    /// Existing ACL entries are preserved so the commissioner can continue to control the
    /// target; the new entry is only appended if an equivalent one does not already exist.
    ///
    /// Privilege levels:
    /// - View: for reading attributes.
    /// - Operate: for sending commands.
    /// - Manage: for bindings and subscriptions, without changing privileges, ACL settings, etc.
    /// - Administer: all permissions, available to the commissioner.
    ///
    /// Auth mode:
    /// - CASE: for secure one-to-one communication.
    /// - Group: less secure but suitable for one-to-many communication.
    ///
    /// - Parameters:
    ///   - targetDeviceID: The node ID of the device whose ACL is being modified.
    ///   - sourceNodeID: The node ID being granted access.
    ///   - clusterID: The cluster ID the access grant applies to.
    ///   - controller: The Matter controller used to reach the target device.
    private func grantAccessToSource(targetDeviceID: NSNumber, sourceNodeID: NSNumber, clusterID: NSNumber, controller: MTRDeviceController) async throws {
        let targetDevice = MTRBaseDevice(nodeID: targetDeviceID, controller: controller)
        guard let aclCluster = MTRBaseClusterAccessControl(device: targetDevice, endpointID: 0, queue: .main) else { return }

        let target = MTRAccessControlClusterAccessControlTargetStruct()
        target.cluster = clusterID
        target.endpoint = nil
        target.deviceType = nil

        let newEntry = MTRAccessControlClusterAccessControlEntryStruct()
        newEntry.privilege = NSNumber(value: 3) // Operate
        newEntry.authMode = NSNumber(value: 2)  // CASE (Certificate-based)
        newEntry.subjects = [sourceNodeID]
        newEntry.targets = [target]

        SharedLogger.info("Reading attribute ACL...")
        var currentACLs = try await aclCluster.readAttributeACL(with: nil) as? [MTRAccessControlClusterAccessControlEntryStruct] ?? []
        SharedLogger.info("Amending ACL records...")
        let entryExists = currentACLs.contains { entry in
            (entry.subjects as? [NSNumber])?.contains(sourceNodeID) == true && entry.privilege == newEntry.privilege
        }

        if !entryExists {
            SharedLogger.info("Storing new ACL record on the target device...")
            currentACLs.append(newEntry)
            try await aclCluster.writeAttributeACL(withValue: currentACLs)
            SharedLogger.debug("Access granted successfully to node \(sourceNodeID)")
        } else {
            SharedLogger.debug("ACL entry already exists for node \(sourceNodeID). Skipping write.")
        }
    }

    /// Creates a binding so the source device can send commands directly to the target device.
    ///
    /// The set of supported commands is defined by the cluster. The source must support those
    /// commands and should declare that capability in its descriptor cluster for the endpoint as a
    /// client cluster. The target must implement the cluster and should declare it in its own
    /// descriptor cluster for the endpoint as a server cluster.
    ///
    /// The new binding entry is appended to the source's existing binding list so previously
    /// defined bindings are preserved.
    ///
    /// - Parameters:
    ///   - sourceDeviceID: The node ID of the device that will send commands.
    ///   - sourceEndpoint: The endpoint on the source device that hosts the client cluster.
    ///   - targetNodeID: The node ID of the device that will receive commands.
    ///   - targetEndpoint: The endpoint on the target device that hosts the server cluster.
    ///   - clusterID: The cluster ID used for the binding.
    ///   - controller: The Matter controller used to reach the source device.
    private func bindSwitchToBulb(sourceDeviceID: NSNumber, sourceEndpoint: NSNumber, targetNodeID: NSNumber, targetEndpoint: NSNumber, clusterID: NSNumber, controller: MTRDeviceController) async throws {
        let sourceDevice = MTRBaseDevice(nodeID: sourceDeviceID, controller: controller)
        guard let bindingCluster = MTRBaseClusterBinding(device: sourceDevice, endpointID: sourceEndpoint, queue: .main) else { return }

        SharedLogger.debug("Preparing a new binding record.")
        let bindingEntry = MTRBindingClusterTargetStruct()
        bindingEntry.node = targetNodeID
        bindingEntry.endpoint = targetEndpoint
        bindingEntry.cluster = clusterID

        SharedLogger.debug("Storing record on a source device.")
        var bindings = try await bindingCluster.readAttributeBinding(with: nil)
        bindings.append(bindingEntry)
        try await bindingCluster.writeAttributeBinding(withValue: bindings)

        SharedLogger.debug("Binding created successfully on source.")
    }
}
