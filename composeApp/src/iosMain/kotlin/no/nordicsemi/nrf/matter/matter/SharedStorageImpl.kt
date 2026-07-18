package no.nordicsemi.nrf.matter.matter

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSUserDefaults
import platform.Matter.MTRStorageProtocol
import platform.darwin.NSObject

/**
 * Mirrors `iosApp/SharedCode/SharedStorage.swift`'s `MTRStorage` conformance
 * (the plain string/number/bool convenience methods on the Swift side are not part of
 * the protocol and aren't needed here — only the 3 required `MTRStorage` methods are).
 */
@OptIn(ExperimentalForeignApi::class)
internal class SharedStorageImpl(suiteName: String) : NSObject(), MTRStorageProtocol {

    private val defaults: NSUserDefaults = NSUserDefaults(suiteName = suiteName)

    override fun storageDataForKey(key: String): NSData? = defaults.dataForKey(key)

    override fun setStorageData(value: NSData, forKey: String): Boolean {
        defaults.setObject(value, forKey)
        return true
    }

    override fun removeStorageDataForKey(key: String): Boolean {
        defaults.removeObjectForKey(key)
        return true
    }
}
