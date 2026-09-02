package no.nordicsemi.nrf.matter.binding

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.logger.NordicLogger

internal class BindingLogsProviderImpl: BindingLogsProvider {

    override val bindingLogs: Flow<String> = NordicLogger.logsChannel.receiveAsFlow()
}