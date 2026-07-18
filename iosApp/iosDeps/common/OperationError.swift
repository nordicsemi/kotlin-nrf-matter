//
//  OperationError.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 22/04/2026.
//

/// Errors raised while reading and converting Matter attribute values.
enum OperationError: Error {
    /// The expected attribute entry was not present in the data.
    case missingAttribute
    /// The attribute value was present but had an unexpected type.
    case wrongType
    /// An unspecified error occurred.
    case unknown
}
