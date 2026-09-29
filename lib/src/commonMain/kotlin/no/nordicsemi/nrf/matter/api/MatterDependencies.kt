package no.nordicsemi.nrf.matter.api

import kotlinx.coroutines.CoroutineDispatcher
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.commission.FinaliseCommissioningUseCase
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.repository.BindingRepository
import no.nordicsemi.nrf.matter.repository.DevicesRepository
import no.nordicsemi.nrf.matter.repository.DevicesStateRepository

internal class MatterDependencies(val platform: MatterPlatformDependencies) {

    val devicesRepository = DevicesRepository(platform.devicesDataSource)
    val devicesStateRepository = DevicesStateRepository(platform.deviceStateDataSource)
    val bindingRepository = BindingRepository(platform.bindingDataSource)
    val finaliseCommissioningUseCase = FinaliseCommissioningUseCase(platform.matterClient, platform.errorCodeMapper::errorCodeOf)

    val matterClient: MatterClient get() = platform.matterClient
    val bindingController: BindingController get() = platform.bindingController
    val bindingLogsProvider: BindingLogsProvider get() = platform.bindingLogsProvider
    val matterDecommissioner: MatterDecommissioner get() = platform.matterDecommissioner
    val ioDispatcher: CoroutineDispatcher get() = platform.ioDispatcher
}
