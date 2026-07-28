//
//  KeypairInitializer.swift
//  iosApp
//
//  Created by Sylwester Zielinski on 12/06/2026.
//

/// Ensures the signing keypair stored in the keychain is not stale after a fresh app install.
public class KeypairInitializer {

    private static let isInitializedKey = "is_keypair_initilized"

    /// Clears any leftover keychain data on first launch after a fresh install.
    ///
    /// The keychain survives app deletion and reinstallation, so without this check a
    /// reinstalled app could pick up a stale private key that no longer matches its fabric.
    public static func initKeychain() {
        let helper = KeypairHelper(logTag: "KeypairInitializer")
        let storage = SharedStorage(suitName: SharedConsts.sharedStorage)
        
        if (storage.getBool(key: isInitializedKey) != true) {
            SharedLogger.debug("Detected fresh app install. Clearing stale keychain data.")
            helper.deletePrivateKey()
            storage.storeBool(key: isInitializedKey, value: true)
            SharedLogger.debug("Keychain cleared.")
        }
    }
}
