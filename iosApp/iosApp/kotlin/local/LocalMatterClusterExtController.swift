//
//  LocalMatterClusterExtController.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 30/04/2026.
//

import Matter
import ComposeApp
import SharedCode
import OSLog

/**
 * A helper class for communication with the cluster extension.
 * The extension defines additional field and command for generating a ranom number to Basic Information Cluster defined by standard.
 * The flow requires first to send a "generate number" command and latter for reading the new value from the attribute.
 */
class LocalMatterClusterExtController : MatterClusterExtensionController {
    
    private let endpointId: NSNumber = 0 //todo: hardcoded
    private let clusterId: NSNumber = 0x28 //todo: hardcoded
    private let attributeId: NSNumber = 0x17 //todo: hardcoded
    private let commandId: NSNumber = 0x00 //todo: hardcoded
    private let eventId: NSNumber = 0x4 //todo: hardcoded
    
    /**
     * Reads value from a "random number" attibute.
     */
    func getRandomNumber(deviceId: DeviceId) async throws -> KotlinInt {
        let attributeReader = try AttributeReader(deviceId: deviceId.nsNumber())
        let result: Int32 = try await attributeReader.readAttribute(endpoint: endpointId, cluster: clusterId, attribute: attributeId)
        return KotlinInt(int: result)
    }

    /**
     * Sends a "generate random number" command which is defined as an extension to Basic Information Cluster defined by standard.
     */
    func generateRandomNumber(deviceId: DeviceId) async throws -> KotlinInt {
        SharedLogger.debug("Generating random number...")
        let commandExecutor = try CommandExecutor(deviceId: deviceId.nsNumber())
        
        try await commandExecutor.executeCommand(
            endpoint: endpointId,
            cluster: clusterId,
            command: commandId,
            type: MTRBooleanValueType,
            value: true
        )
        
        SharedLogger.debug("Generating random number command succeeded.")
        
        return try await getRandomNumber(deviceId: deviceId)
    }
    
    func subscribeToRandomNumber(deviceId: DeviceId, endpoint: Int32, onUpdate: @escaping (KotlinUInt) -> Void) async throws {
        SharedLogger.debug("Observing random number...")
        let eventSubscriber = try EventSubscriber(deviceId: deviceId.nsNumber())
        
        eventSubscriber.subscribe(endpoint: endpointId, cluster: clusterId, event: eventId) { result in
            onUpdate(KotlinUInt(value: result))
        }
    }
}
