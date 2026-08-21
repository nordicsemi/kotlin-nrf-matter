package no.nordicsemi.nrf.matter.ui.device

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.cluster.toClusters
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceUiModel
import no.nordicsemi.nrf.matter.ui.MatterController

class DeviceViewModel(
    private val device: DeviceUiModel,
    client: MatterClient,
    scope: CoroutineScope,
) : MatterController {

    private val clusters = device.device.toClusters(client).map { it.toViewModel(scope) }

    @Composable
    override fun Item(onDecommission: (DeviceId) -> Unit) {
        DeviceItem(
            device = device,
            clusters = clusters,
            onDecommission = onDecommission,
        )
    }
}
