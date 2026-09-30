package no.nordicsemi.nrf.matter.platform

import com.skydoves.cloudy.cloudy
import platform.Foundation.NSBundle

fun iosAppEnvironment(): AppEnvironment = AppEnvironment(
    platformType = PlatformType.IOS,
    appVersion = getAppVersion(),
    blur = { cloudy() },
)

private fun getAppVersion(): String {
    val version = NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String
    return version ?: "Unknown"
}
