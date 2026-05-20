//
//  NodeIdProvider.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 06/03/2026.
//

import Foundation

/**
 * A helper class for providing new node id for newly commissioned device.
 * This id needs to be unique so commssioning a new device with already existing
 * node id should fail.
 */
public class NodeIdProvider {
    public static let id: NSNumber = 112 //TODO
}
