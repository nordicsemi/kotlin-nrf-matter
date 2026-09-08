//
//  CommissioningError.swift
//  ios-matter
//
//  Created by Sylwester Zielinski on 12/06/2026.
//

/// Errors that can occur while commissioning a device locally.
///
enum CommissioningError: Error {
    
    /// The payload provided for commissioning is invalid.
    case invalidPayload
    /// The node ID required to commission the device could not be found in shared storage.
    case missingNodeId
    /// Scanning found no Thread network for the device to join.
    case noThreadNetwork
    /// An unspecified commissioning failure occurred.
    case unknown
}
