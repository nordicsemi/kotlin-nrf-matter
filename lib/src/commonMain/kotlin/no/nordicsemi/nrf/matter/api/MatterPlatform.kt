package no.nordicsemi.nrf.matter.api

import kotlinx.coroutines.CoroutineDispatcher
import no.nordicsemi.nrf.matter.binding.BindingDataSource
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.commission.CommissioningTaskProvider
import no.nordicsemi.nrf.matter.commission.MatterErrorCodeMapper
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource

interface MatterPlatform {
    val devicesDataSource: DevicesDataSource
    val deviceStateDataSource: DeviceStateDataSource
    val bindingDataSource: BindingDataSource
    val matterClient: MatterClient
    val matterDecommissioner: MatterDecommissioner
    val bindingController: BindingController
    val bindingLogsProvider: BindingLogsProvider
    val ioDispatcher: CoroutineDispatcher

    val errorCodeMapper: MatterErrorCodeMapper
    val commissioningTaskProvider: CommissioningTaskProvider
}
