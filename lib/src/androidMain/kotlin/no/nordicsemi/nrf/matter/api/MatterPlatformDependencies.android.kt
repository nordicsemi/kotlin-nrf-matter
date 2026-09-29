package no.nordicsemi.nrf.matter.api

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import no.nordicsemi.nrf.matter.binding.BaseBindingDataSource
import no.nordicsemi.nrf.matter.binding.BindingDataSource
import no.nordicsemi.nrf.matter.binding.DataStoreProvider
import no.nordicsemi.nrf.matter.chip.BindingControllerImpl
import no.nordicsemi.nrf.matter.chip.BindingLogsProviderImpl
import no.nordicsemi.nrf.matter.chip.ChipClient
import no.nordicsemi.nrf.matter.chip.MatterDecommissionerImpl
import no.nordicsemi.nrf.matter.cluster.AndroidMatterClient
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.commission.AndroidCommissioningTaskProvider
import no.nordicsemi.nrf.matter.commission.CommissioningTaskProvider
import no.nordicsemi.nrf.matter.commission.MatterErrorCodeMapper
import no.nordicsemi.nrf.matter.commission.toMatterErrorCode
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.logger.AndroidLoggerBackend
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.repository.AndroidDeviceStateDataSource
import no.nordicsemi.nrf.matter.repository.AndroidDevicesDataSource

fun NordicMatters.initialize(context: Context) {
    ContextHolder.initialise(context)
    NordicLogger.setBackend(AndroidLoggerBackend)
    configure(AndroidPlatformDependencies())
}

internal class AndroidPlatformDependencies : MatterPlatformDependencies {

    private val context = ContextHolder.getContext()

    val chipClient by lazy { ChipClient(context) }

    override val devicesDataSource: DevicesDataSource by lazy {
        AndroidDevicesDataSource(context)
    }

    override val deviceStateDataSource: DeviceStateDataSource by lazy {
        AndroidDeviceStateDataSource(context)
    }

    override val bindingDataSource: BindingDataSource by lazy {
        BaseBindingDataSource(DataStoreProvider(context).createStorage())
    }

    override val matterClient: MatterClient by lazy { AndroidMatterClient(chipClient) }

    override val matterDecommissioner: MatterDecommissioner by lazy {
        MatterDecommissionerImpl(chipClient)
    }

    override val bindingController: BindingController by lazy { BindingControllerImpl(chipClient) }

    override val bindingLogsProvider: BindingLogsProvider by lazy {
        BindingLogsProviderImpl(chipClient)
    }

    override val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    override val errorCodeMapper = MatterErrorCodeMapper { it.toMatterErrorCode() }

    override val commissioningTaskProvider: CommissioningTaskProvider = AndroidCommissioningTaskProvider()
}
