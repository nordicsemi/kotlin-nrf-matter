//
//  AttributeSubscriber.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 23/04/2026.
//

import Matter
import SharedCode
import OSLog

struct AttributeUpdate<T : Sendable> : Sendable {
    let value: T?
    let error: Error?
}

class AttributeSubscriber {
    
    private let logger = Logger(subsystem: "nrf.matter", category: "AttributeSubscriber")
    private let baseDevice: MTRBaseDevice
    
    init(deviceId: NSNumber) throws {
        let controller = try LocalControllerProvider(logTag: "AttributeSubscriber").getController()
        baseDevice = MTRBaseDevice(nodeID: deviceId, controller: controller)
    }
    
    func subscribe<T: AttributeParser>(endpoint: NSNumber, cluster: NSNumber, attribute: NSNumber, onUpdate: @escaping (T) -> Void) {
        baseDevice.subscribeToAttributes(
            withEndpointID: endpoint,
            clusterID: cluster,
            attributeID: attribute,
            params: nil,
            queue: DispatchQueue.global(),
            reportHandler: { result, error in
                
                if let error = error {
                    // todo
                    return
                }
                
                if let result = result, let value = try? T.parse(value: result[0].readAny()) {
                    onUpdate(value)
                }
            }
        )
    }
}
