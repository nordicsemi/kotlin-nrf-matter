package no.nordicsemi.nrf.matter.api

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import no.nordicsemi.nrf.matter.binding.DataStoreProvider
import no.nordicsemi.nrf.matter.chip.BindingControllerImpl
import no.nordicsemi.nrf.matter.chip.BindingLogsProviderImpl
import no.nordicsemi.nrf.matter.chip.ChipClient
import no.nordicsemi.nrf.matter.chip.MatterDecommissionerImpl
import no.nordicsemi.nrf.matter.cluster.AndroidMatterClient
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.commission.ClusterDeviceInfoProvider
import no.nordicsemi.nrf.matter.commission.DeviceInfoProvider
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.repository.AndroidDeviceStateDataSource
import no.nordicsemi.nrf.matter.repository.AndroidDevicesDataSource

/**
 * Initializes the library.
 *
 * An app does not normally call this: [NordicMatterInitializer] has already run by the time any
 * app component does. It is here for apps that removed that provider from their manifest, and for
 * unit tests, where no provider runs.
 *
 * Only the context is retained. Calling it again is harmless - the graph is built once, from
 * whichever context was stored when it was first needed.
 */
fun NordicMatters.initialize(context: Context) {
    ContextHolder.initialise(context)
}

/**
 * The Android side of the library: the CHIP device controller and the DataStores behind it.
 *
 * Every property carries the type the `expect` declares rather than its implementation's, because
 * an `actual` has to match the expected signature exactly. The two Android-only members - the CHIP
 * client and the concretely typed device info provider - are free of that and are what
 * `platformDependencies` is reached for on this platform.
 *
 * Everything is created lazily, so an app that never commissions a device never starts the Matter
 * stack.
 */
internal actual class MatterPlatformDependencies {

    private val context = ContextHolder.getContext()

    val chipClient by lazy { ChipClient(context) }

    /**
     * Concretely typed, unlike the `actual` below, so that the Android commissioning flow can
     * hand it the name the Google Home flow gave the device.
     */
    val clusterDeviceInfoProvider by lazy { ClusterDeviceInfoProvider(matterClient) }

    actual val devicesDataSource: DevicesDataSource by lazy {
        AndroidDevicesDataSource(context)
    }

    actual val deviceStateDataSource: DeviceStateDataSource by lazy {
        AndroidDeviceStateDataSource(context)
    }

    actual val bindingDataStore: DataStore<Preferences> by lazy {
        DataStoreProvider(context).createDataStore()
    }

    actual val matterClient: MatterClient by lazy { AndroidMatterClient(chipClient) }
    actual val matterDecommissioner: MatterDecommissioner by lazy {
        MatterDecommissionerImpl(chipClient)
    }
    actual val bindingController: BindingController by lazy { BindingControllerImpl(chipClient) }
    actual val bindingLogsProvider: BindingLogsProvider by lazy {
        BindingLogsProviderImpl(chipClient)
    }

    actual val deviceInfoProvider: DeviceInfoProvider
        get() = clusterDeviceInfoProvider
}
