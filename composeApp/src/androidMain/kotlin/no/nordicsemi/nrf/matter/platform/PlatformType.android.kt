package no.nordicsemi.nrf.matter.platform

import android.content.Context
import org.koin.mp.KoinPlatform

actual val currentType: PlatformType = PlatformType.ANDROID

actual fun getAppVersion(): String {
    val version = runCatching {
        val context = KoinPlatform.getKoin().get<Context>()
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    }.getOrNull()

    return version ?: "Unknown"
}
