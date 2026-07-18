//
//  MatterDecommissioner.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 11/03/2026.
//

import SharedCode
import Matter

/// Decommissions a device, removing it from the local fabric.
@objc public class LocalMatterDecommissioner: NSObject {

    @objc public override init() {}

    /// Removes the operational credentials fabric from the device and forgets it locally.
    ///
    /// - Parameter deviceId: The Matter node ID of the device to decommission.
    /// - Throws: An error if reading or removing the fabric fails.
    @objc public func decommission(deviceId: String) async throws {
        SharedLogger.info("Decommission device: \(deviceId)")
        let controller = try! LocalControllerProvider(logTag: "LocalControllerProvider").getController()

        SharedLogger.debug("Erasing data on a remote device.")
        let baseDevice = MTRBaseDevice(nodeID: deviceId.toMatterNodeId(), controller: controller)
        let operationalCredentials = MTRBaseClusterOperationalCredentials(device: baseDevice, endpointID: 0, queue: .main)

        let fabrics = try await operationalCredentials!.readAttributeFabrics(with: nil)
        SharedLogger.debug("Stored fabrics: \(fabrics)")
        let theFabric = fabrics[0] as! MTROperationalCredentialsClusterFabricDescriptorStruct

        let params = MTROperationalCredentialsClusterRemoveFabricParams()
        params.fabricIndex = theFabric.fabricIndex

        try await operationalCredentials!.removeFabric(with: params)

        SharedLogger.debug("Removing device from local fabric.")
        controller.forgetDevice(withNodeID: deviceId.toMatterNodeId())
        SharedLogger.info("Decommission success")
    }
}
