package no.nordicsemi.nrf.matter.docs.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import no.nordicsemi.nrf.matter.api.Fabric
import no.nordicsemi.nrf.matter.api.MatterPlatform
import no.nordicsemi.nrf.matter.binding.BindingDataSource
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.commission.CommissioningException
import no.nordicsemi.nrf.matter.commission.CommissioningTask
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.model.DeviceId

internal object WebMatterPlatform : MatterPlatform {

    private val webMatterClient = WebMatterClient()
    private val webBindingLogsProvider = WebBindingLogsProvider()

    override val devicesDataSource: DevicesDataSource = WebDevicesDataSource()
    override val deviceStateDataSource: DeviceStateDataSource = WebDeviceStateDataSource()
    override val bindingDataSource: BindingDataSource = WebBindingDataSource()
    override val matterClient: MatterClient = webMatterClient
    override val matterDecommissioner: MatterDecommissioner = WebMatterDecommissioner()
    override val bindingController: BindingController = WebBindingController(webBindingLogsProvider)
    override val bindingLogsProvider: BindingLogsProvider = webBindingLogsProvider
    override val ioDispatcher: CoroutineDispatcher = Dispatchers.Default

    override fun errorCodeOf(throwable: Throwable): Int? = null

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
                client = webMatterClient,
                scope = scope,
                onSuccess = { currentOnSuccess(it) },
                onError = { currentOnError(it) },
            )
        }
    }
}
