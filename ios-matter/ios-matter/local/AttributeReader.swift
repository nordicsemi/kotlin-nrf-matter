//
//  AttributeReader.swift
//  ios-matter
//
//  Created by Sylwester Zielinski on 23/04/2026.
//

import Matter

/// Reads attribute values from a Matter device over the local (on-device) controller.
class AttributeReader {

    private let baseDevice: MTRBaseDevice

    /// Creates a reader for the device with the given node ID.
    ///
    /// - Parameter deviceId: The Matter node ID of the target device.
    /// - Throws: An error if the local controller cannot be obtained.
    init(deviceId: NSNumber) throws {
        let controller = try LocalControllerProvider(logTag: "LocalMatterCustomClusterController").getController()
        baseDevice = MTRBaseDevice(nodeID: deviceId, controller: controller)
    }

    /// Reads a single attribute from the device and parses it into the requested type.
    ///
    /// - Parameters:
    ///   - endpoint: The endpoint ID hosting the attribute.
    ///   - cluster: The cluster ID the attribute belongs to.
    ///   - attribute: The attribute ID to read.
    /// - Returns: The attribute value parsed as `T`.
    /// - Throws: `OperationError.missingAttribute` if the read returns no data, or an error from
    ///   `T.parse` if the value has the wrong type.
    func readAttribute<T: AttributeParser>(endpoint: NSNumber, cluster: NSNumber, attribute: NSNumber) async throws -> T {
        let value = try await readValue(endpoint: endpoint, cluster: cluster, attribute: attribute)

        guard let rawValue = value.rawValue else {
            throw OperationError.missingAttribute
        }

        return try T.parse(value: rawValue)
    }

    /// Reads a single attribute from the device without interpreting its value.
    ///
    /// Used by cluster agnostic callers, which do not know the attribute's type up front and
    /// therefore have no ``AttributeParser`` to parse it with.
    ///
    /// - Parameters:
    ///   - endpoint: The endpoint ID hosting the attribute.
    ///   - cluster: The cluster ID the attribute belongs to.
    ///   - attribute: The attribute ID to read.
    /// - Returns: The attribute value together with its Matter type.
    /// - Throws: `OperationError.missingAttribute` if the read returns no data.
    func readValue(endpoint: NSNumber, cluster: NSNumber, attribute: NSNumber) async throws -> MatterValue {
        SwiftLogger.debug("readAttributes")

        let result = try? await baseDevice.readAttributes(
            withEndpointID: endpoint,
            clusterID: cluster,
            attributeID: attribute,
            params: nil,
            queue: DispatchQueue.global()
        )

        guard let report = result?.first else {
            throw OperationError.missingAttribute
        }

        SwiftLogger.debug("Attirbutes for endpoint: \(endpoint), cluster: \(cluster)")

        return try report.readMatterValue()
    }
    
    private func printAttributes(_ array: [[String: Any]]) {
        for (index, dict) in array.enumerated() {
            SwiftLogger.debug("Item nr \(index):")
            for (key, value) in dict {
                SwiftLogger.debug("\(key): \(value as! NSObject)")
            }
        }
    }
}
