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

/**
 * A helper class for communication with a manufacturer specific cluster defined in this example
 * A new cluster provides 2 functionalities:
 *  1. Turning on/off LED light.
 *  2. Observe button state changes.
 */
class LocalMatterCustomClusterController: MatterManufacturerCustomDataController {

    private let endpointId: NSNumber = 1 //todo: hardcoded
    private let clusterId: NSNumber = 0xFFF1FC01 //todo: hardcoded
    private let commandId: NSNumber = 0xFFF10000 //todo: hardcoded
    
    /**
     * Reads the custom attributes from the Matter device.
     * The new fields are: custom device name, state of LED light, state of button press.
     */
    func getData(deviceId: DeviceId, endpoint: Int32) async throws -> ManufacturerSpecificData {
        SharedLogger.debug("Getting custom manufacturer data...")
        
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

    /**
     * Sends a command for turning on/off LED light.
     */
    func setLed(deviceId: DeviceId, isOn: Bool, endpoint: Int32) async throws {
        SharedLogger.debug("invoke setLed")
        let commandExecutor = try CommandExecutor(deviceId: deviceId.nsNumber())
        
        try await commandExecutor.executeCommand(
            endpoint: endpointId,
            cluster: clusterId,
            command: commandId,
            type: MTRUnsignedIntegerValueType,
            value: 2 //change current value
        )
    }
    
    /**
     * Subscribe to button state changes.
     */
    func subscribeToButtonChanges(deviceId: DeviceId, endpoint: Int32, onUpdate: @escaping (KotlinBoolean) -> Void) async throws {
        let attributeSubscriber = try? AttributeSubscriber(deviceId: deviceId.nsNumber())
        
        attributeSubscriber?.subscribe(endpoint: endpointId, cluster: clusterId, attribute: 0xfff10002, onUpdate: { result in
            onUpdate(KotlinBoolean(bool: result))
        })
    }
}
