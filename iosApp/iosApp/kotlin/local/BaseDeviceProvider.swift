//
//  BaseDeviceProvider.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 20/07/2026.
//

import Foundation
import Matter
import SharedCode

class BaseDeviceProvider {
    
    nonisolated(unsafe) private static var instances: [NSNumber: MTRBaseDevice] = [:]
    private static let cacheLock = NSLock()
    
    private init() { }

    static func shared(deviceId: NSNumber) throws -> MTRBaseDevice {
        cacheLock.lock()
        defer { cacheLock.unlock() }

        if let existing = instances[deviceId] {
            return existing
        }

        let controller = try LocalControllerProvider(logTag: "LocalControllerProvider").getController()
        let baseDevice = MTRBaseDevice(nodeID: deviceId, controller: controller)
        
        instances[deviceId] = baseDevice
        return baseDevice
    }
}
