package no.nordicsemi.nrf.matter.commission

import androidx.compose.runtime.Composable
import no.nordicsemi.nrf.matter.api.Fabric
import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.model.DeviceId

interface CommissioningTask {

    fun startCommissioning()
}

@Composable
expect fun rememberCommissioningTask(
    fabric: Fabric = NordicMatters.defaultFabric,
    onSuccess: suspend (DeviceId) -> Unit,
    onError: (CommissioningException) -> Unit,
): CommissioningTask
