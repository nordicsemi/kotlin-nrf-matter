package no.nordicsemi.nrf.matter.api

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineDispatcher
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

interface MatterPlatform {
    val devicesDataSource: DevicesDataSource
    val deviceStateDataSource: DeviceStateDataSource
    val bindingDataSource: BindingDataSource
    val matterClient: MatterClient
    val matterDecommissioner: MatterDecommissioner
    val bindingController: BindingController
    val bindingLogsProvider: BindingLogsProvider
    val ioDispatcher: CoroutineDispatcher

    fun errorCodeOf(throwable: Throwable): Int?

    @Composable
    fun rememberCommissioningTask(
        fabric: Fabric,
        onSuccess: suspend (DeviceId) -> Unit,
        onError: (CommissioningException) -> Unit,
    ): CommissioningTask
}
