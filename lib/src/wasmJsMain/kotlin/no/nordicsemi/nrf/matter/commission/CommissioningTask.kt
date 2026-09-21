package no.nordicsemi.nrf.matter.commission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.api.Fabric
import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.cluster.WebMatterClient
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.webdemo.WebDemoAction
import no.nordicsemi.nrf.matter.webdemo.WebDemoEvents
import no.nordicsemi.nrf.matter.webdemo.WebDeviceCatalog
import no.nordicsemi.nrf.matter.webdemo.seedDevice
import kotlin.random.Random

/**
 * Web/demo actual: simulates BLE discovery + provisioning with a short delay, then either seeds
 * the next demo device (see [WebDeviceCatalog]) and calls [onSuccess], or -- with a small,
 * fixed chance -- calls [onError] so `CommissioningErrorScreen` stays reachable through normal
 * use, matching `commissioning.md`'s "If commissioning fails" section.
 */
@Composable
actual fun rememberCommissioningTask(
    fabric: Fabric,
    onSuccess: suspend (DeviceId) -> Unit,
    onError: (CommissioningException) -> Unit,
): CommissioningTask {
    val scope = rememberCoroutineScope()
    val currentOnSuccess by rememberUpdatedState(onSuccess)
    val currentOnError by rememberUpdatedState(onError)

    return remember(fabric) {
        WebCommissioningTask(
            fabric = fabric,
            scope = scope,
            onSuccess = { currentOnSuccess(it) },
            onError = { currentOnError(it) },
        )
    }
}

private class WebCommissioningTask(
    private val fabric: Fabric,
    private val scope: CoroutineScope,
    private val onSuccess: suspend (DeviceId) -> Unit,
    private val onError: (CommissioningException) -> Unit,
) : CommissioningTask {

    private var isRunning = false

    override fun startCommissioning() {
        if (isRunning) return
        isRunning = true

        scope.launch {
            WebDemoEvents.publish(WebDemoAction.CommissioningStarted)
            delay(1200)

            if (Random.nextInt(100) < FAILURE_CHANCE_PERCENT) {
                isRunning = false
                WebDemoEvents.publish(WebDemoAction.CommissioningFailed)
                onError(
                    CommissioningException(
                        deviceId = null,
                        stage = Stage.COMMISSIONING,
                        errorCode = null,
                        displayMessage = "Simulated commissioning failure (demo)",
                    )
                )
                return@launch
            }

            val deviceId = fabric.nextDeviceId()
            val profile = WebDeviceCatalog.next()
            val client = NordicMatters.matterDependencies.platformDependencies.matterClient as WebMatterClient
            client.seedDevice(deviceId, profile)

            isRunning = false
            WebDemoEvents.publish(WebDemoAction.CommissioningSucceeded(deviceId))
            onSuccess(deviceId)
        }
    }

    private companion object {
        const val FAILURE_CHANCE_PERCENT = 15
    }
}
