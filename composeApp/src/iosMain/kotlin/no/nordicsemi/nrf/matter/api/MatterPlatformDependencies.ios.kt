package no.nordicsemi.nrf.matter.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import no.nordicsemi.nrf.matter.MatterCommissioner
import no.nordicsemi.nrf.matter.adapters.BindingControllerImpl
import no.nordicsemi.nrf.matter.adapters.MatterCommissionerImpl
import no.nordicsemi.nrf.matter.adapters.MatterDecommissionerImpl
import no.nordicsemi.nrf.matter.binding.BindingLogsProviderImpl
import no.nordicsemi.nrf.matter.binding.DataStoreProvider
import no.nordicsemi.nrf.matter.cluster.IosMatterClient
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.commission.FinaliseCommissioningUseCase
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.repository.IosDevicesDataSource
import no.nordicsemi.nrf.matter.repository.IosDevicesStateDataSource

internal actual class MatterPlatformDependencies {

    val matterCommissioner: MatterCommissioner by lazy { MatterCommissionerImpl() }

    actual val devicesDataSource: DevicesDataSource by lazy { IosDevicesDataSource() }
    actual val deviceStateDataSource: DeviceStateDataSource by lazy { IosDevicesStateDataSource() }
    actual val matterClient: MatterClient by lazy { IosMatterClient() }
    actual val finaliseCommissioningUseCase: FinaliseCommissioningUseCase by lazy {
        FinaliseCommissioningUseCase(matterClient)
    }
    actual val matterDecommissioner: MatterDecommissioner by lazy { MatterDecommissionerImpl() }
    actual val bindingController: BindingController by lazy { BindingControllerImpl() }
    actual val bindingLogsProvider: BindingLogsProvider by lazy { BindingLogsProviderImpl() }

    actual val bindingDataStore: DataStore<Preferences> by lazy {
        DataStoreProvider().createDataStore()
    }
}
