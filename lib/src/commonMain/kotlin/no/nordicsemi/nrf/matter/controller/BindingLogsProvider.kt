package no.nordicsemi.nrf.matter.controller

import kotlinx.coroutines.flow.Flow

interface BindingLogsProvider {

    val bindingLogs: Flow<String>
}
