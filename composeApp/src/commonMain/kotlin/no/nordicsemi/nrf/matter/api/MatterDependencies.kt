package no.nordicsemi.nrf.matter.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import no.nordicsemi.nrf.matter.binding.BaseBindingDataSource
import no.nordicsemi.nrf.matter.binding.BindDevicesUseCase
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.commission.DecommissionUseCases
import no.nordicsemi.nrf.matter.commission.DeviceInfoProvider
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.repository.BindingRepository
import no.nordicsemi.nrf.matter.repository.DevicesRepository
import no.nordicsemi.nrf.matter.repository.DevicesStateRepository

/**
 * The platform half of the library: everything that needs the platform's Matter stack or its file
 * storage.
 *
 * Implemented once per platform - `AndroidMatterPlatform`, `IosMatterPlatform` - and handed to
 * [MatterDependencies], which wires up everything that is platform independent.
 */
internal interface MatterPlatform {

    val devicesDataSource: DevicesDataSource
    val deviceStateDataSource: DeviceStateDataSource
    val bindingDataStore: DataStore<Preferences>
    val matterClient: MatterClient
    val deviceInfoProvider: DeviceInfoProvider
    val matterDecommissioner: MatterDecommissioner
    val bindingController: BindingController
    val bindingLogsProvider: BindingLogsProvider
}

/**
 * The library's object graph, built by hand.
 *
 * The library is a dependency of apps that bring their own dependency injection - or none - so it
 * cannot own a container of its own: [NordicMatters.install] holds the single instance, and every
 * [Fabric] is handed the one it was created with.
 */
internal class MatterDependencies(val platform: MatterPlatform) {

    val devicesRepository = DevicesRepository(platform.devicesDataSource)
    val devicesStateRepository = DevicesStateRepository(platform.deviceStateDataSource)
    val bindingRepository = BindingRepository(BaseBindingDataSource(platform.bindingDataStore))

    val deviceInfoProvider: DeviceInfoProvider get() = platform.deviceInfoProvider
    val matterClient: MatterClient get() = platform.matterClient

    val decommissionUseCases = DecommissionUseCases(
        deviceController = platform.matterDecommissioner,
        devicesStateRepository = devicesStateRepository,
        devicesRepository = devicesRepository,
        bindingRepository = bindingRepository,
    )

    val bindDevicesUseCase = BindDevicesUseCase(
        deviceController = platform.bindingController,
        bindingLogsProvider = platform.bindingLogsProvider,
        bindingRepository = bindingRepository,
    )
}
