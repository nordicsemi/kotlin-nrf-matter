//
//  AttributeWriter.swift
//  ios-matter
//
//  Created by Sylwester Zielinski on 13/08/2026.
//

import Matter

/// Writes attribute values to a Matter device over the local (on-device) controller.
class AttributeWriter {

    private let baseDevice: MTRBaseDevice

    /// Creates a writer for the device with the given node ID.
    ///
    /// - Parameter deviceId: The Matter node ID of the target device.
    /// - Throws: An error if the local controller cannot be obtained.
    init(deviceId: NSNumber) throws {
        let controller = try LocalControllerProvider(logTag: "AttributeWriter").getController()
        baseDevice = MTRBaseDevice(nodeID: deviceId, controller: controller)
    }

    /// Writes a single attribute on the device.
    ///
    /// The write is not timed, so attributes that require a timed write reject it.
    ///
    /// - Parameters:
    ///   - endpoint: The endpoint ID hosting the attribute.
    ///   - cluster: The cluster ID the attribute belongs to.
    ///   - attribute: The attribute ID to write.
    ///   - value: The new value. Its ``MatterValue/type`` has to match the attribute's type, as
    ///     the device rejects a value encoded as the wrong Matter type.
    /// - Throws: An error if the value cannot be encoded or if the write fails.
    func writeAttribute(endpoint: NSNumber, cluster: NSNumber, attribute: NSNumber, value: MatterValue) async throws {
        SwiftLogger.debug("Writing attribute: \(attribute)")

        let response: [[String: Any]]? = try await baseDevice.writeAttribute(
            withEndpointID: endpoint,
            clusterID: cluster,
            attributeID: attribute,
            value: try value.mtrDictionary(),
            timedWriteTimeout: nil,
            queue: DispatchQueue.global()
        )

        // A write that the device rejects is reported as a per path error inside the response
        // rather than as a failure of the interaction itself.
        if let error = response?.compactMap({ $0[MTRErrorKey] as? Error }).first {
            SwiftLogger.debug("Write rejected: \(error)")
            throw error
        }

        SwiftLogger.debug("Attribute written successfully.")
    }
}
