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
import no.nordicsemi.nrf.matter.commission.DeviceInfoProvider
import no.nordicsemi.nrf.matter.commission.IosDeviceInfoProvider
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.repository.IosDevicesDataSource
import no.nordicsemi.nrf.matter.repository.IosDevicesStateDataSource

/**
 * The iOS side of the library: the `ios-matter` adapters and the file-backed stores.
 *
 * Needs nothing from the app, which is why iOS has no `initialize`.
 *
 * Every property carries the type the `expect` declares rather than its implementation's, because
 * an `actual` has to match the expected signature exactly. The commissioner is iOS-only and free
 * of that, and is what `platformDependencies` is reached for on this platform.
 *
 * Everything is created lazily, so an app that never commissions a device never touches the local
 * Matter controller.
 */
internal actual class MatterPlatformDependencies {

    val matterCommissioner: MatterCommissioner by lazy { MatterCommissionerImpl() }

    actual val devicesDataSource: DevicesDataSource by lazy { IosDevicesDataSource() }
    actual val deviceStateDataSource: DeviceStateDataSource by lazy { IosDevicesStateDataSource() }
    actual val matterClient: MatterClient by lazy { IosMatterClient() }
    actual val deviceInfoProvider: DeviceInfoProvider by lazy { IosDeviceInfoProvider() }
    actual val matterDecommissioner: MatterDecommissioner by lazy { MatterDecommissionerImpl() }
    actual val bindingController: BindingController by lazy { BindingControllerImpl() }
    actual val bindingLogsProvider: BindingLogsProvider by lazy { BindingLogsProviderImpl() }

    actual val bindingDataStore: DataStore<Preferences> by lazy {
        DataStoreProvider().createDataStore()
    }
}
