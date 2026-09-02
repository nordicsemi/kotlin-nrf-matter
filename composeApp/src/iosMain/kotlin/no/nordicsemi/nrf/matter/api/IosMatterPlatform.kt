package no.nordicsemi.nrf.matter.api

import no.nordicsemi.nrf.matter.MatterCommissioner
import no.nordicsemi.nrf.matter.adapters.BindingControllerImpl
import no.nordicsemi.nrf.matter.adapters.MatterCommissionerImpl
import no.nordicsemi.nrf.matter.adapters.MatterDecommissionerImpl
import no.nordicsemi.nrf.matter.binding.BindingLogsProviderImpl
import no.nordicsemi.nrf.matter.binding.DataStoreProvider
import no.nordicsemi.nrf.matter.cluster.IosMatterClient
import no.nordicsemi.nrf.matter.commission.IosDeviceInfoProvider
import no.nordicsemi.nrf.matter.repository.IosDevicesDataSource
import no.nordicsemi.nrf.matter.repository.IosDevicesStateDataSource

/**
 * Initializes the library. Call it once, before anything else - when the app's view controller is
 * created, for instance.
 *
 * Calling it again is a no-op, whichever thread it comes from: the graph built by the first call
 * stays in place, and the one built here is discarded unused.
 */
fun NordicMatters.initialize() {
    if (isInitialized) return

    install(MatterDependencies(IosMatterPlatform()))
}

/**
 * The platform graph, for the iOS internals that need more of it than [MatterPlatform] carries -
 * the commissioning task.
 */
internal val iosMatterPlatform: IosMatterPlatform
    get() = NordicMatters.requireDependencies().platform as? IosMatterPlatform
        ?: error("NordicMatters was initialized with a platform other than iOS's.")

/**
 * The iOS side of the library: the `ios-matter` adapters and the file-backed stores.
 *
 * Everything is created lazily, so an app that never commissions a device never touches the local
 * Matter controller.
 */
internal class IosMatterPlatform : MatterPlatform {

    val matterCommissioner: MatterCommissioner by lazy { MatterCommissionerImpl() }

    override val devicesDataSource by lazy { IosDevicesDataSource() }
    override val deviceStateDataSource by lazy { IosDevicesStateDataSource() }
    override val bindingDataStore by lazy { DataStoreProvider().createDataStore() }
    override val matterClient by lazy { IosMatterClient() }
    override val deviceInfoProvider by lazy { IosDeviceInfoProvider() }
    override val matterDecommissioner by lazy { MatterDecommissionerImpl() }
    override val bindingController by lazy { BindingControllerImpl() }
    override val bindingLogsProvider by lazy { BindingLogsProviderImpl() }
}
