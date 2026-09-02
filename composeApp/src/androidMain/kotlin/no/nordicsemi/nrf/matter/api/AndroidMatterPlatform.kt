package no.nordicsemi.nrf.matter.api

import android.content.Context
import no.nordicsemi.nrf.matter.binding.DataStoreProvider
import no.nordicsemi.nrf.matter.chip.BindingControllerImpl
import no.nordicsemi.nrf.matter.chip.BindingLogsProviderImpl
import no.nordicsemi.nrf.matter.chip.ChipClient
import no.nordicsemi.nrf.matter.chip.ClustersHelper
import no.nordicsemi.nrf.matter.chip.MatterBasicInfoProvider
import no.nordicsemi.nrf.matter.chip.MatterDecommissionerImpl
import no.nordicsemi.nrf.matter.cluster.AndroidMatterClient
import no.nordicsemi.nrf.matter.commission.AndroidDeviceInfoProvider
import no.nordicsemi.nrf.matter.repository.AndroidDeviceStateDataSource
import no.nordicsemi.nrf.matter.repository.AndroidDevicesDataSource

/**
 * Initializes the library.
 *
 * An app does not normally call this: [NordicMatterInitializer] has already run by the time any
 * app component does. It is here for apps that removed that provider from their manifest, and for
 * unit tests, where no provider runs.
 *
 * Only the application context is retained. Calling it again is a no-op, whichever thread it comes
 * from: the graph built by the first call stays in place, and the one built here is discarded
 * unused.
 */
fun NordicMatters.initialize(context: Context) {
    if (isInitialized) return

    install(MatterDependencies(AndroidMatterPlatform(context.applicationContext)))
}

/**
 * The platform graph, for the Android internals that need more of it than [MatterPlatform] carries
 * - the commissioning service and the commissioning task.
 */
internal val androidMatterPlatform: AndroidMatterPlatform
    get() = NordicMatters.requireDependencies().platform as? AndroidMatterPlatform
        ?: error("NordicMatters was initialized with a platform other than Android's.")

/**
 * The Android side of the library: the CHIP device controller and the DataStores behind it.
 *
 * Everything is created lazily, so an app that never commissions a device never starts the Matter
 * stack.
 */
internal class AndroidMatterPlatform(private val context: Context) : MatterPlatform {

    val chipClient by lazy { ChipClient(context) }

    override val devicesDataSource by lazy { AndroidDevicesDataSource(context) }
    override val deviceStateDataSource by lazy { AndroidDeviceStateDataSource(context) }
    override val bindingDataStore by lazy { DataStoreProvider(context).createDataStore() }
    override val matterClient by lazy { AndroidMatterClient(chipClient) }
    override val matterDecommissioner by lazy { MatterDecommissionerImpl(chipClient) }
    override val bindingController by lazy { BindingControllerImpl(chipClient) }
    override val bindingLogsProvider by lazy { BindingLogsProviderImpl(chipClient) }

    /**
     * Typed as the implementation rather than as [MatterPlatform.deviceInfoProvider], because the
     * commissioning task hands it the device name that the Google Home flow collected.
     */
    override val deviceInfoProvider by lazy {
        AndroidDeviceInfoProvider(
            basicInfoProvider = MatterBasicInfoProvider(chipClient),
            clustersHelper = ClustersHelper(chipClient),
        )
    }
}
