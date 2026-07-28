//
//  MatterKeypair.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 05/03/2026.
//

import Matter
import os.log

/// Class used for NOC signing.
///
/// It holds a private and public key pair that must remain the same for a specific fabric.
/// Generated keys are stored securely in the keychain on the phone.
public class MatterKeypair: NSObject, MTRKeypair {

    private let privateKey: SecKey
    private let _publicKey: SecKey
    private let logTag: String

    /// Loads the existing signing keypair from the keychain, generating a new one if none exists.
    public override init() {
        self.logTag = ""
        let helper = KeypairHelper(logTag: self.logTag)
        let existingKey = helper.getPrivateKey()
        let privateKey = existingKey != nil ? existingKey : (try! helper.generatePrivateKey())
        self._publicKey = SecKeyCopyPublicKey(privateKey!)!
        self.privateKey = privateKey!
        super.init()
    }

    /// Signs a message with the private key using ECDSA over SHA256, in DER encoding.
    ///
    /// - Parameter message: The message data to sign.
    /// - Returns: The DER-encoded signature, or empty `Data` if signing fails.
    public func signMessageECDSA_DER(_ message: Data) -> Data {
        var error: Unmanaged<CFError>? = nil
        let signedMessage = SecKeyCreateSignature(
            privateKey,
            .ecdsaSignatureMessageX962SHA256,
            message as CFData,
            &error
        )

        if error != nil {
            return Data()
        }

        guard let signedMessage = signedMessage else {
            return Data()
        }
        
        return signedMessage as Data
    }

    /// Returns the public key matching the stored private key.
    ///
    /// - Returns: A retained, autoreleased reference to the public key.
    public func publicKey() -> Unmanaged<SecKey> {
        return Unmanaged.passRetained(_publicKey).autorelease()
    }
}
