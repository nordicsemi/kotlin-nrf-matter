package no.nordicsemi.nrf.matter.api

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import no.nordicsemi.nrf.matter.MatterCommissioner
import no.nordicsemi.nrf.matter.adapters.BindingControllerImpl
import no.nordicsemi.nrf.matter.adapters.IOSLoggerImpl
import no.nordicsemi.nrf.matter.adapters.MatterCommissionerImpl
import no.nordicsemi.nrf.matter.adapters.MatterDecommissionerImpl
import no.nordicsemi.nrf.matter.binding.BaseBindingDataSource
import no.nordicsemi.nrf.matter.binding.BindingDataSource
import no.nordicsemi.nrf.matter.binding.BindingLogsProviderImpl
import no.nordicsemi.nrf.matter.binding.DataStoreProvider
import no.nordicsemi.nrf.matter.cluster.IosMatterClient
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.commission.CommissioningTaskProvider
import no.nordicsemi.nrf.matter.commission.IosCommissioningTaskProvider
import no.nordicsemi.nrf.matter.commission.MatterErrorCodeMapper
import no.nordicsemi.nrf.matter.commission.toMatterErrorCode
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.logger.IosLoggerBackend
import no.nordicsemi.nrf.matter.repository.IosDevicesDataSource
import no.nordicsemi.nrf.matter.repository.IosDevicesStateDataSource

fun NordicMatters.initializePlatform() {
    IosLoggerBackend.logger = IOSLoggerImpl()
    initialize(IosPlatformDependencies(), IosLoggerBackend)
}

internal class IosPlatformDependencies : MatterPlatformDependencies {

    val matterCommissioner: MatterCommissioner by lazy { MatterCommissionerImpl() }

    override val devicesDataSource: DevicesDataSource by lazy { IosDevicesDataSource() }
    override val deviceStateDataSource: DeviceStateDataSource by lazy { IosDevicesStateDataSource() }
    override val matterClient: MatterClient by lazy { IosMatterClient() }
    override val matterDecommissioner: MatterDecommissioner by lazy { MatterDecommissionerImpl() }
    override val bindingController: BindingController by lazy { BindingControllerImpl() }
    override val bindingLogsProvider: BindingLogsProvider by lazy { BindingLogsProviderImpl() }

    override val bindingDataSource: BindingDataSource by lazy {
        BaseBindingDataSource(DataStoreProvider().createStorage())
    }

    override val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    override val errorCodeMapper = MatterErrorCodeMapper { it.toMatterErrorCode() }

    override val commissioningTaskProvider: CommissioningTaskProvider by lazy {
        IosCommissioningTaskProvider(matterCommissioner)
    }
}
