package no.nordicsemi.nrf.matter.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.commission.FinaliseCommissioningUseCase
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource

internal expect class MatterPlatformDependencies() {

    val devicesDataSource: DevicesDataSource
    val deviceStateDataSource: DeviceStateDataSource
    val bindingDataStore: DataStore<Preferences>
    val matterClient: MatterClient
    val finaliseCommissioningUseCase: FinaliseCommissioningUseCase
    val matterDecommissioner: MatterDecommissioner
    val bindingController: BindingController
    val bindingLogsProvider: BindingLogsProvider
}
