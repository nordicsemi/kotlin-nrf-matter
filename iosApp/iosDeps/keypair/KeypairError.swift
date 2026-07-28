//
//  KeypairError.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 05/03/2026.
//

/// Errors that can occur while creating or retrieving the signing keypair.
public enum KeypairError: Swift.Error {
    /// Generating a new private key failed.
    case generatePrivateKeyFailed
    /// Private key generation succeeded but unexpectedly returned no key.
    case generatePrivateKeyReturnedNil
}
