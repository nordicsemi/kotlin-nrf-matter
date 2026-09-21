package no.nordicsemi.nrf.matter.api

import no.nordicsemi.nrf.matter.binding.BindingDataSource
import no.nordicsemi.nrf.matter.binding.DataStoreProvider
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.cluster.WebMatterClient
import no.nordicsemi.nrf.matter.commission.FinaliseCommissioningUseCase
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.controller.WebBindingController
import no.nordicsemi.nrf.matter.controller.WebBindingLogsProvider
import no.nordicsemi.nrf.matter.controller.WebMatterDecommissioner
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.repository.WebDeviceStateDataSource
import no.nordicsemi.nrf.matter.repository.WebDevicesDataSource

/**
 * Web/demo actual: every dependency is an in-memory fake -- there is no native Matter/BLE stack
 * on the web. [matterClient] is the one that matters: it's the seam every real cluster
 * controller in `:shared` talks through, so faking it is what lets that real business logic run
 * against demo data.
 */
internal actual class MatterPlatformDependencies {

    actual val devicesDataSource: DevicesDataSource by lazy { WebDevicesDataSource() }
    actual val deviceStateDataSource: DeviceStateDataSource by lazy { WebDeviceStateDataSource() }
    actual val matterClient: MatterClient by lazy { WebMatterClient() }
    actual val finaliseCommissioningUseCase: FinaliseCommissioningUseCase by lazy {
        FinaliseCommissioningUseCase(matterClient)
    }
    actual val matterDecommissioner: MatterDecommissioner by lazy { WebMatterDecommissioner() }

    private val webBindingLogsProvider by lazy { WebBindingLogsProvider() }
    actual val bindingLogsProvider: BindingLogsProvider get() = webBindingLogsProvider
    actual val bindingController: BindingController by lazy { WebBindingController(webBindingLogsProvider) }

    actual val bindingDataSource: BindingDataSource by lazy { DataStoreProvider().createBindingDataSource() }
}
