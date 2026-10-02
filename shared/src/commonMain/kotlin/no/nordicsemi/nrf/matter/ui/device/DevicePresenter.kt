package no.nordicsemi.nrf.matter.ui.device

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job
import no.nordicsemi.nrf.matter.cluster.toClusters
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.Device

class DevicePresenter(
    val device: Device,
    parent: CoroutineScope,
) {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        NordicLogger.error(
            "Unhandled exception in DevicePresenter for device ${device.deviceId}",
            throwable,
        )
    }

    private val scope =
        CoroutineScope(parent.coroutineContext + SupervisorJob(parent.coroutineContext.job) + exceptionHandler)

    val clusters: List<ClusterController> =
        device.toClusters().mapNotNull { it.toController(scope) }

    fun cancel() {
        scope.cancel()
    }
}
