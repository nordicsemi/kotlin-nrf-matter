package no.nordicsemi.nrf.matter.chip

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider

internal class BindingLogsProviderImpl(
    chipClient: ChipClient,
) : BindingLogsProvider {

    override val bindingLogs: Flow<String> = chipClient.chipLogFlow
}