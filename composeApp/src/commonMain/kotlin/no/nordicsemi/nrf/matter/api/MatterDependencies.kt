package no.nordicsemi.nrf.matter.api

import no.nordicsemi.nrf.matter.binding.BaseBindingDataSource
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.commission.FinaliseCommissioningUseCase
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.repository.BindingRepository
import no.nordicsemi.nrf.matter.repository.DevicesRepository
import no.nordicsemi.nrf.matter.repository.DevicesStateRepository

internal class MatterDependencies(val platformDependencies: MatterPlatformDependencies) {

    val devicesRepository = DevicesRepository(platformDependencies.devicesDataSource)
    val devicesStateRepository = DevicesStateRepository(platformDependencies.deviceStateDataSource)
    val bindingRepository =
        BindingRepository(BaseBindingDataSource(platformDependencies.bindingDataStore))

    val matterClient: MatterClient get() = platformDependencies.matterClient
    val bindingController: BindingController get() = platformDependencies.bindingController
    val bindingLogsProvider: BindingLogsProvider
        get() = platformDependencies.bindingLogsProvider
    val matterDecommissioner: MatterDecommissioner
        get() = platformDependencies.matterDecommissioner
    val finaliseCommissioningUseCase: FinaliseCommissioningUseCase
        get() = platformDependencies.finaliseCommissioningUseCase
}
