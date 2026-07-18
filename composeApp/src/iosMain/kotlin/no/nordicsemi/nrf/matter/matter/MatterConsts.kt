package no.nordicsemi.nrf.matter.matter

/**
 * Mirrors `iosApp/SharedCode/SharedConsts.swift` / `KeypairHelper.swift`'s `tag`.
 * Values must stay byte-for-byte identical to those Swift constants: the `nrfMatter`
 * app extension signs NOCs using the same identity, and `KeypairInitializer.swift`
 * wipes the Keychain item by this same tag on reinstall.
 */
internal object MatterConsts {
    const val LOCAL_STORAGE_GROUP = "group.nordicsemi.nrf.matter.local"
    const val IPK_KEY = "MatterIPK"
    const val KEYPAIR_TAG = "com.nordicsemi.nrf.matter"
    const val FABRIC_ID = 1L
    const val VENDOR_ID = 0xFFF1
}
