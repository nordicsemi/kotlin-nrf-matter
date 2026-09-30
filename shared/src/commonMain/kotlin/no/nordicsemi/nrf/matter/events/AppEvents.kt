package no.nordicsemi.nrf.matter.events

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow

object AppEvents {
    val events = MutableSharedFlow<AppEvent>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val commissioningAcknowledgement = Channel<Unit>(Channel.CONFLATED)

    fun emit(event: AppEvent) {
        events.tryEmit(event)
    }

    suspend fun awaitCommissioningAcknowledged() {
        commissioningAcknowledgement.receive()
    }

    fun acknowledgeCommissioning() {
        commissioningAcknowledgement.trySend(Unit)
    }
}
