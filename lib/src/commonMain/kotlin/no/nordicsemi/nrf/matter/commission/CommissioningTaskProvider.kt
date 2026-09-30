package no.nordicsemi.nrf.matter.commission

import androidx.compose.runtime.Composable
import no.nordicsemi.nrf.matter.api.Fabric
import no.nordicsemi.nrf.matter.model.DeviceId

interface CommissioningTaskProvider {

    @Composable
    fun rememberCommissioningTask(
        fabric: Fabric,
        onSuccess: suspend (DeviceId) -> Unit,
        onError: (CommissioningException) -> Unit,
    ): CommissioningTask
}
