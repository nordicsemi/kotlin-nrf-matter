package no.nordicsemi.nrf.matter.docs.demo

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import no.nordicsemi.nrf.matter.api.MatterPlatformDependencies
import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.binding.BindingDataSource
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.commission.CommissioningTaskProvider
import no.nordicsemi.nrf.matter.commission.MatterErrorCodeMapper
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource

internal object WebMatterPlatformDependencies : MatterPlatformDependencies {

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

    override val errorCodeMapper = MatterErrorCodeMapper { null }
    override val commissioningTaskProvider: CommissioningTaskProvider = WebCommissioningTaskProvider(webMatterClient)
}

fun NordicMatters.initializePlatform() {
    initialize(WebMatterPlatformDependencies, InMemoryLoggerBackend)
}
