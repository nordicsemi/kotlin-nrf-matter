package no.nordicsemi.nrf.matter.api

import android.content.Context
import androidx.compose.runtime.Composable
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
import no.nordicsemi.nrf.matter.commission.CommissioningException
import no.nordicsemi.nrf.matter.commission.CommissioningTask
import no.nordicsemi.nrf.matter.commission.rememberPlatformCommissioningTask
import no.nordicsemi.nrf.matter.commission.toMatterErrorCode
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.logger.AndroidLoggerBackend
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.repository.AndroidDeviceStateDataSource
import no.nordicsemi.nrf.matter.repository.AndroidDevicesDataSource

fun NordicMatters.initialize(context: Context) {
    ContextHolder.initialise(context)
    NordicLogger.setBackend(AndroidLoggerBackend)
    configure(MatterPlatformDependencies())
}

internal class MatterPlatformDependencies : MatterPlatform {

    private val context = ContextHolder.getContext()

    val chipClient by lazy { ChipClient(context) }

    override val devicesDataSource: DevicesDataSource by lazy {
        AndroidDevicesDataSource(context)
    }

    override val deviceStateDataSource: DeviceStateDataSource by lazy {
        AndroidDeviceStateDataSource(context)
    }

    override val bindingDataSource: BindingDataSource by lazy {
        BaseBindingDataSource(DataStoreProvider(context).createDataStore())
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

    override fun errorCodeOf(throwable: Throwable): Int? = throwable.toMatterErrorCode()

    @Composable
    override fun rememberCommissioningTask(
        fabric: Fabric,
        onSuccess: suspend (DeviceId) -> Unit,
        onError: (CommissioningException) -> Unit,
    ): CommissioningTask = rememberPlatformCommissioningTask(fabric, onSuccess, onError)
}
