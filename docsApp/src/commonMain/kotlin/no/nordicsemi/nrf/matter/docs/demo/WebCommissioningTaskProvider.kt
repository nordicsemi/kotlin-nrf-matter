package no.nordicsemi.nrf.matter.docs.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import no.nordicsemi.nrf.matter.api.Fabric
import no.nordicsemi.nrf.matter.commission.CommissioningException
import no.nordicsemi.nrf.matter.commission.CommissioningTask
import no.nordicsemi.nrf.matter.commission.CommissioningTaskProvider
import no.nordicsemi.nrf.matter.model.DeviceId

internal class WebCommissioningTaskProvider(
    private val client: WebMatterClient,
) : CommissioningTaskProvider {

    @Composable
    override fun rememberCommissioningTask(
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
                client = client,
                scope = scope,
                onSuccess = { currentOnSuccess(it) },
                onError = { currentOnError(it) },
            )
        }
    }
}
