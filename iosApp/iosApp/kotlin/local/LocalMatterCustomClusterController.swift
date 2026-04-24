//
//  LocalMatterCustomClusterController.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 20/04/2026.
//

import Matter
import ComposeApp
import SharedCode
import OSLog

class LocalMatterCustomClusterController: MatterManufacturerCustomDataController {
    
    private let endpointId: NSNumber = 1 //todo: hardcoded
    private let clusterId: NSNumber = 0xFFF1FC01 //todo: hardcoded
    private let commandId: NSNumber = 0xFFF10000 //todo: hardcoded
    private let logger = Logger(subsystem: "nrf.matter", category: "LocalMatterCustomClusterController")
    
    func getData(deviceId: DeviceId, endpoint: Int32) async throws -> ManufacturerSpecificData {
        logger.debug("Getting c austom manufacturer data...")
        
        let attributeReader = try AttributeReader(deviceId: deviceId.nsNumber())
       
        let name: String = try await attributeReader.readAttribute(endpoint: endpointId, cluster: clusterId, attribute: 0xfff10000)
        let led: Bool = try await attributeReader.readAttribute(endpoint: endpointId, cluster: clusterId, attribute: 0xfff10001)
        let button: Bool = try await attributeReader.readAttribute(endpoint: endpointId, cluster: clusterId, attribute: 0xfff10002)
        
        let data = ManufacturerSpecificData(
            name: name,
            led: led,
            button: button
        )
        
        return data
    }

    func setLed(deviceId: DeviceId, isOn: Bool, endpoint: Int32) async throws {
        logger.debug("invoke setLed")
        let attributeWriter = try AttributeWriter(deviceId: deviceId.nsNumber())
        
        try await attributeWriter.executeCommand(
            endpoint: endpointId,
            cluster: clusterId,
            command: commandId,
            type: MTRUnsignedIntegerValueType,
            value: isOn ? 1 : 0
        )
    }
    
    func subscribeToButtonChanges(deviceId: DeviceId, endpoint: Int32, onUpdate: @escaping (KotlinBoolean) -> Void) async throws {
        let attributeSubscriber = try AttributeSubscriber(deviceId: deviceId.nsNumber())
        
        attributeSubscriber.subscribe(endpoint: endpointId, cluster: clusterId, attribute: 0xfff10002, onUpdate: { result in
            onUpdate(KotlinBoolean(bool: result))
        })
    }
}
