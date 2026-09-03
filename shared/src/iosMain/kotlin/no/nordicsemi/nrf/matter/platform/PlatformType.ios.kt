package no.nordicsemi.nrf.matter.platform

import platform.Foundation.NSBundle

actual val currentType: PlatformType = PlatformType.IOS

actual fun getAppVersion(): String {
    val version = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String
    return version ?: "Unknown"
}
