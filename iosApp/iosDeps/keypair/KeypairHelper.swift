//
//  KeypairHelper.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 05/03/2026.
//

import Security
import Foundation

/// A helper class for managing signing keys.
///
/// A signing key needs to be unique and persistent for a specific fabric, so it is
/// stored on the phone and retrieved when needed.
class KeypairHelper {
    
    private let logTag: String
    private let tag: Data
    
    /// Creates a helper that manages the signing keypair stored under a fixed application tag.
    ///
    /// - Parameter logTag: Tag used to prefix log messages emitted by this instance.
    init(logTag: String) {
        self.logTag = logTag
        let name = "com.nordicsemi.nrf.matter"
        tag = name.data(using: .utf8)!
    }

    /// Generates a new private key in the keychain, replacing any existing one with the same tag.
    ///
    /// - Returns: The newly generated private key.
    /// - Throws: `KeypairError.generatePrivateKeyFailed` if key generation fails, or
    ///   `KeypairError.generatePrivateKeyReturnedNil` if it unexpectedly returns no key.
    func generatePrivateKey() throws -> SecKey {
        SharedLogger.debug("\(self.logTag) - Generating new key.")
        
        let attributes: [String: Any] = [
            kSecAttrKeyType as String           : kSecAttrKeyTypeECSECPrimeRandom,
            kSecAttrKeySizeInBits as String     : 256,
            kSecPrivateKeyAttrs as String : [
                kSecAttrIsPermanent as String       : true,
                kSecAttrApplicationTag as String    : tag,
            ]
        ]

        var error: Unmanaged<CFError>? = nil

        let secKey = SecKeyCreateRandomKey(attributes as CFDictionary, &error)

        if error != nil {
            SharedLogger.debug("\(self.logTag) - Error during generation of a new key.")
            throw KeypairError.generatePrivateKeyFailed
        }

        guard let secKey = secKey else {
            SharedLogger.debug("\(self.logTag) - Error during generation of a new key.")
            throw KeypairError.generatePrivateKeyReturnedNil
        }
        
        SharedLogger.debug("\(self.logTag) - Returning newly generated key.")

        return secKey
    }
    
    /// Retrieves the previously generated private key from the keychain, if one exists.
    ///
    /// - Returns: The stored private key, or `nil` if no key is found.
    func getPrivateKey() -> SecKey? {
        SharedLogger.debug("\(self.logTag) - Getting private key.")

        let query: [String: Any] = [
            kSecClass as String                 : kSecClassKey,
            kSecAttrApplicationTag as String    : tag,
            kSecAttrKeyType as String           : kSecAttrKeyTypeECSECPrimeRandom,
            kSecReturnRef as String             : kCFBooleanTrue as Any,
            kSecMatchLimit as String: kSecMatchLimitOne
        ]
        
        var item: CFTypeRef?
        let status = SecItemCopyMatching(query as CFDictionary, &item)
        guard status == errSecSuccess else {
            SharedLogger.debug("\(self.logTag) - Private key not found.")
            return nil
        }
        SharedLogger.debug("\(self.logTag) - Private key found.")
        guard item != nil else {
            SharedLogger.debug("\(self.logTag) - Private key is nil. Deleting.")
            deletePrivateKey()
            return nil
        }
        return (item as! SecKey)
    }
    
    /// Deletes the stored private key from the keychain.
    func deletePrivateKey() {
        SharedLogger.debug("\(self.logTag) - Deleting private key.")
        
        let deleteQuery: [String: Any] = [
            kSecClass as String: kSecClassKey,
            kSecAttrApplicationTag as String: tag
        ]
        SecItemDelete(deleteQuery as CFDictionary)
    }
}
