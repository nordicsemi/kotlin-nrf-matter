package no.nordicsemi.nrf.matter.platform

actual val currentType: PlatformType = PlatformType.WEB

actual fun getAppVersion(): String = "web-demo"
