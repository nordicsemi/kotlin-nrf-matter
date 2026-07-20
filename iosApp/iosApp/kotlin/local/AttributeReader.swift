//
//  AttributeReader.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 23/04/2026.
//

import Matter
import SharedCode

/// Reads attribute values from a Matter device over the local (on-device) controller.
class AttributeReader {

    private let baseDevice: MTRBaseDevice

    /// Creates a reader for the device with the given node ID.
    ///
    /// - Parameter deviceId: The Matter node ID of the target device.
    /// - Throws: An error if the local controller cannot be obtained.
    init(deviceId: NSNumber) throws {
        baseDevice = try BaseDeviceProvider.shared(deviceId: deviceId)
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
        let result = try await readAttribute(endpoint: endpoint, cluster: cluster, attribute: attribute)
        return try T.parse(value: result)
    }
    
    private func readAttribute(endpoint: NSNumber, cluster: NSNumber, attribute: NSNumber) async throws -> Any {
        SharedLogger.debug("readAttributes")

        let result = try? await baseDevice.readAttributes(
            withEndpointID: endpoint,
            clusterID: cluster,
            attributeID: attribute,
            params: nil,
            queue: DispatchQueue.main
        )
        
        guard let result else {
            throw OperationError.missingAttribute
        }
        
        SharedLogger.debug("Attirbutes for endpoint: \(endpoint), cluster: \(cluster)")

        return try result[0].readAny()
    }
    
    private func printAttributes(_ array: [[String: Any]]) {
        for (index, dict) in array.enumerated() {
            SharedLogger.debug("Item nr \(index):")
            for (key, value) in dict {
                SharedLogger.debug("\(key): \(value as! NSObject)")
            }
        }
    }
}
