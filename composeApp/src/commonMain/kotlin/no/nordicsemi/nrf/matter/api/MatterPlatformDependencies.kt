package no.nordicsemi.nrf.matter.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.commission.DeviceInfoProvider
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource

/**
 * The platform half of the library: everything that needs the platform's Matter stack or its file
 * storage.
 *
 * One `actual` per platform, each free to expose more than this - the CHIP client on Android, the
 * commissioner on iOS - and handed to [MatterDependencies], which wires up everything that is
 * platform independent.
 *
 * The constructor takes nothing: whatever a platform needs from the app, it collects itself. On
 * Android that is the application context, which `NordicMatters.initialize(context)` has stored by
 * the time anything is built.
 */
internal expect class MatterPlatformDependencies() {

    val devicesDataSource: DevicesDataSource
    val deviceStateDataSource: DeviceStateDataSource
    val bindingDataStore: DataStore<Preferences>
    val matterClient: MatterClient
    val deviceInfoProvider: DeviceInfoProvider
    val matterDecommissioner: MatterDecommissioner
    val bindingController: BindingController
    val bindingLogsProvider: BindingLogsProvider
}
