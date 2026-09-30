package no.nordicsemi.nrf.matter.binding

import kotlinx.coroutines.flow.Flow

interface BindingsStorage {
    val data: Flow<String?>

    suspend fun update(transform: (String?) -> String)
}
