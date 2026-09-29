package no.nordicsemi.nrf.matter.platform

import android.content.Context
import com.skydoves.cloudy.cloudy
import org.koin.mp.KoinPlatform

fun androidAppEnvironment(): AppEnvironment = AppEnvironment(
    platformType = PlatformType.ANDROID,
    appVersion = getAppVersion(),
    blur = { cloudy() },
)

private fun getAppVersion(): String {
    val version = runCatching {
        val context = KoinPlatform.getKoin().get<Context>()
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    }.getOrNull()

    return version ?: "Unknown"
}
