package no.nordicsemi.nrf.matter.api

import no.nordicsemi.nrf.matter.binding.BaseBindingDataSource
import no.nordicsemi.nrf.matter.binding.BindDevicesUseCase
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.commission.DecommissionUseCases
import no.nordicsemi.nrf.matter.commission.DeviceInfoProvider
import no.nordicsemi.nrf.matter.repository.BindingRepository
import no.nordicsemi.nrf.matter.repository.DevicesRepository
import no.nordicsemi.nrf.matter.repository.DevicesStateRepository

/**
 * The library's object graph, built by hand.
 *
 * The library is a dependency of apps that bring their own dependency injection - or none - so it
 * cannot own a container of its own: [NordicMatters.matterDependencies] holds the single instance,
 * and every [Fabric] is handed the one it was created with.
 */
internal class MatterDependencies(val platformDependencies: MatterPlatformDependencies) {

    val devicesRepository = DevicesRepository(platformDependencies.devicesDataSource)
    val devicesStateRepository = DevicesStateRepository(platformDependencies.deviceStateDataSource)
    val bindingRepository =
        BindingRepository(BaseBindingDataSource(platformDependencies.bindingDataStore))

    val deviceInfoProvider: DeviceInfoProvider get() = platformDependencies.deviceInfoProvider
    val matterClient: MatterClient get() = platformDependencies.matterClient

    val decommissionUseCases = DecommissionUseCases(
        deviceController = platformDependencies.matterDecommissioner,
        devicesStateRepository = devicesStateRepository,
        devicesRepository = devicesRepository,
        bindingRepository = bindingRepository,
    )

    val bindDevicesUseCase = BindDevicesUseCase(
        deviceController = platformDependencies.bindingController,
        bindingLogsProvider = platformDependencies.bindingLogsProvider,
        bindingRepository = bindingRepository,
    )
}
