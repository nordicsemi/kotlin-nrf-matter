//
//  AttributeSubscriber.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 23/04/2026.
//

import Matter

/// A single update delivered by an attribute subscription, carrying either a parsed value or an error.
struct AttributeUpdate<T : Sendable> : Sendable {
    /// The parsed attribute value, if the update succeeded.
    let value: T?
    /// The error reported for this update, if the read failed.
    let error: Error?
}

/// Subscribes to attribute value changes on a Matter device over the local (on-device) controller.
class AttributeSubscriber {

    private let baseDevice: MTRBaseDevice

    /// Creates a subscriber for the device with the given node ID.
    ///
    /// - Parameter deviceId: The Matter node ID of the target device.
    /// - Throws: An error if the local controller cannot be obtained.
    init(deviceId: NSNumber) throws {
        let controller = try LocalControllerProvider(logTag: "AttributeSubscriber").getController()
        baseDevice = MTRBaseDevice(nodeID: deviceId, controller: controller)
    }

    /// Subscribes to changes of a single attribute and parses each report into `T`.
    ///
    /// Reports that fail to parse, or that carry an error, are silently ignored; `onUpdate` is
    /// only called for values that parse successfully.
    ///
    /// - Parameters:
    ///   - endpoint: The endpoint ID hosting the attribute.
    ///   - cluster: The cluster ID the attribute belongs to.
    ///   - attribute: The attribute ID to subscribe to.
    ///   - onUpdate: Called on a background queue with each successfully parsed value.
    func subscribe<T: AttributeParser>(endpoint: NSNumber, cluster: NSNumber, attribute: NSNumber, onUpdate: @escaping (T) -> Void) {
        baseDevice.subscribeToAttributes(
            withEndpointID: endpoint,
            clusterID: cluster,
            attributeID: attribute,
            params: MTRSubscribeParams.defaultParams,
            queue: DispatchQueue.global(),
            reportHandler: { result, error in
                
                if let error = error {
//                    continuation.yield(AttributeUpdate(value: nil, error: error))
                    return
                }
                
                if let result = result, let value = try? T.parse(value: result[0].readAny()) {
                    onUpdate(value)
//                    continuation.yield(AttributeUpdate(value: value, error: nil))
                }
            }
        )
    }
}

extension MTRSubscribeParams {
    
    /// A subscription configuration with no minimum or maximum reporting interval.
    ///
    /// `maxInterval` is only a recommended value that can be changed by a Matter device.
    static var defaultParams: MTRSubscribeParams {
        let params = MTRSubscribeParams(minInterval: 0, maxInterval: 0)
        return params
    }
}
