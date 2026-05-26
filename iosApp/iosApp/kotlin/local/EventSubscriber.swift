//
//  EventSubscriber.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 26/05/2026.
//

import Matter
import SharedCode

class EventSubscriber {

    private let baseDevice: MTRBaseDevice
    
    init(deviceId: NSNumber) throws {
        let controller = try LocalControllerProvider(logTag: "EventSubscriber").getController()
        baseDevice = MTRBaseDevice(nodeID: deviceId, controller: controller)
    }
    
    func subscribe<T: AttributeParser>(endpoint: NSNumber, cluster: NSNumber, event: NSNumber, onUpdate: @escaping (T) -> Void) {
        baseDevice.subscribeToEvents(
            withEndpointID: endpoint,
            clusterID: cluster,
            eventID: event,
            params: nil,
            queue: DispatchQueue.global(),
            reportHandler: { result, error in
                SharedLogger.debug("Random number result - \(result)")
                SharedLogger.debug("Random number error - \(error)")
                if let error = error {
//                    continuation.yield(AttributeUpdate(value: nil, error: error))
                    return
                }
                
                if let result = result, let value = try? T.parse(value: result[0].readAny()) {
                    onUpdate(value)
//                    continuation.yield(AttributeUpdate(value: value, error: nil))
                }
            },
            subscriptionEstablished: {
                SharedLogger.debug("Random number - subscriptionEstablished")
            }
        )
    }
}
