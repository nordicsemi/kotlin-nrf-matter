package no.nordicsemi.nrf.matter.ui.device

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job
import no.nordicsemi.nrf.matter.cluster.toClusters
import no.nordicsemi.nrf.matter.model.DeviceUiModel

class DevicePresenter(
    val device: DeviceUiModel,
    parent: CoroutineScope,
) {

    private val scope =
        CoroutineScope(parent.coroutineContext + SupervisorJob(parent.coroutineContext.job))

    val clusters: List<ClusterController> =
        device.device.toClusters().map { it.toController(scope) }

    fun cancel() {
        scope.cancel()
    }
}
