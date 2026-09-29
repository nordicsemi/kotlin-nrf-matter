package no.nordicsemi.nrf.matter.controller

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class WebBindingLogsProvider : BindingLogsProvider {
    val logFlow = MutableSharedFlow<String>(extraBufferCapacity = 64)
    override val bindingLogs: Flow<String> = logFlow
}
