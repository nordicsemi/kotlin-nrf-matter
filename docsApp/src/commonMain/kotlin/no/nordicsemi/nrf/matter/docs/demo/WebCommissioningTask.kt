package no.nordicsemi.nrf.matter.docs.demo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.api.Fabric
import no.nordicsemi.nrf.matter.commission.CommissioningException
import no.nordicsemi.nrf.matter.commission.CommissioningTask
import no.nordicsemi.nrf.matter.commission.Stage
import no.nordicsemi.nrf.matter.events.AppEvent
import no.nordicsemi.nrf.matter.events.AppEvents
import no.nordicsemi.nrf.matter.model.DeviceId
import kotlin.random.Random

internal class WebCommissioningTask(
    private val fabric: Fabric,
    private val client: WebMatterClient,
    private val scope: CoroutineScope,
    private val onSuccess: suspend (DeviceId) -> Unit,
    private val onError: (CommissioningException) -> Unit,
) : CommissioningTask {

    private var isRunning = false

    override fun startCommissioning() {
        if (isRunning) return
        isRunning = true

        scope.launch {
            AppEvents.emit(AppEvent.CommissioningStarted)
            AppEvents.awaitCommissioningAcknowledged()
            delay(1200)

            if (Random.nextInt(100) < FAILURE_CHANCE_PERCENT) {
                isRunning = false
                AppEvents.emit(AppEvent.CommissioningFailed)
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
            client.seedDevice(deviceId, WebDeviceCatalog.next())

            isRunning = false
            AppEvents.emit(AppEvent.CommissioningSucceeded(deviceId))
            onSuccess(deviceId)
        }
    }

    private companion object {
        const val FAILURE_CHANCE_PERCENT = 15
    }
}
