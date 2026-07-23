package no.nordicsemi.nrf.matter.platform

expect val currentType: PlatformType

enum class PlatformType {
    IOS, ANDROID, JVM
}
