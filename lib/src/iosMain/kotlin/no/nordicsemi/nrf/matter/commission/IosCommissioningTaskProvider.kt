package no.nordicsemi.nrf.matter.commission

import androidx.compose.runtime.Composable
import no.nordicsemi.nrf.matter.MatterCommissioner
import no.nordicsemi.nrf.matter.api.Fabric
import no.nordicsemi.nrf.matter.model.DeviceId

internal class IosCommissioningTaskProvider(
    private val matterCommissioner: MatterCommissioner,
) : CommissioningTaskProvider {

    @Composable
    override fun rememberCommissioningTask(
        fabric: Fabric,
        onSuccess: suspend (DeviceId) -> Unit,
        onError: (CommissioningException) -> Unit,
    ): CommissioningTask = rememberPlatformCommissioningTask(matterCommissioner, fabric, onSuccess, onError)
}
