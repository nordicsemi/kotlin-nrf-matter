package no.nordicsemi.nrf.matter.api

import android.content.Context
import no.nordicsemi.nrf.matter.binding.BindingDataSource
import no.nordicsemi.nrf.matter.binding.DataStoreProvider
import no.nordicsemi.nrf.matter.chip.BindingControllerImpl
import no.nordicsemi.nrf.matter.chip.BindingLogsProviderImpl
import no.nordicsemi.nrf.matter.chip.ChipClient
import no.nordicsemi.nrf.matter.chip.MatterDecommissionerImpl
import no.nordicsemi.nrf.matter.cluster.AndroidMatterClient
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.commission.FinaliseCommissioningUseCase
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.repository.AndroidDeviceStateDataSource
import no.nordicsemi.nrf.matter.repository.AndroidDevicesDataSource

fun NordicMatters.initialize(context: Context) {
    ContextHolder.initialise(context)
}

internal actual class MatterPlatformDependencies {

    private val context = ContextHolder.getContext()

    val chipClient by lazy { ChipClient(context) }

    actual val finaliseCommissioningUseCase by lazy { FinaliseCommissioningUseCase(matterClient) }

    actual val devicesDataSource: DevicesDataSource by lazy {
        AndroidDevicesDataSource(context)
    }

    actual val deviceStateDataSource: DeviceStateDataSource by lazy {
        AndroidDeviceStateDataSource(context)
    }

    actual val bindingDataSource: BindingDataSource by lazy {
        DataStoreProvider(context).createBindingDataSource()
    }

    actual val matterClient: MatterClient by lazy { AndroidMatterClient(chipClient) }
    actual val matterDecommissioner: MatterDecommissioner by lazy {
        MatterDecommissionerImpl(chipClient)
    }
    actual val bindingController: BindingController by lazy { BindingControllerImpl(chipClient) }
    actual val bindingLogsProvider: BindingLogsProvider by lazy {
        BindingLogsProviderImpl(chipClient)
    }
}
