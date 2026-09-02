package no.nordicsemi.nrf.matter.controller

import kotlinx.coroutines.flow.Flow

internal interface BindingLogsProvider {

    val bindingLogs: Flow<String>
}
