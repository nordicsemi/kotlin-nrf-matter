package no.nordicsemi.nrf.matter.controller

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Web/demo actual: a simple broadcast of the simulated binding-operation traffic lines
 * [WebBindingController] writes into [logFlow], standing in for the real chip log stream.
 */
internal class WebBindingLogsProvider : BindingLogsProvider {
    val logFlow = MutableSharedFlow<String>(extraBufferCapacity = 64)
    override val bindingLogs: Flow<String> = logFlow
}
