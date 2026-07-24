//
//  CommissioningError.swift
//  iosDeps
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
    /// An unspecified commissioning failure occurred.
    case unknown
}
