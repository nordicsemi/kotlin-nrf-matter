package no.nordicsemi.nrf.matter.ui.device

import androidx.compose.runtime.Composable
import no.nordicsemi.nrf.matter.cluster.toClusters
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceUiModel

class DeviceViewModel(
    private val device: DeviceUiModel,
) {

    private val clusters = device.device.toClusters().map { it.toViewModel() }

    /** Releases the cluster view models, for a device that has left the list. */
    internal fun clear() {
        clusters.forEach { it.clear() }
    }

    @Composable
    fun Item(onDecommission: (DeviceId) -> Unit) {
        DeviceItem(
            device = device,
            clusters = clusters,
            onDecommission = onDecommission,
        )
    }
}
