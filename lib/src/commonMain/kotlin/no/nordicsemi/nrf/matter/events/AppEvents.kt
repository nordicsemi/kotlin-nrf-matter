package no.nordicsemi.nrf.matter.events

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import no.nordicsemi.nrf.matter.model.DeviceId

object AppEvents {
    val events = MutableSharedFlow<AppEvent>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private val commissioningAcknowledgement = Channel<Unit>(Channel.CONFLATED)

    fun publish(event: AppEvent) {
        events.tryEmit(event)
    }

    suspend fun awaitCommissioningAcknowledged() {
        commissioningAcknowledgement.receive()
    }

    fun acknowledgeCommissioning() {
        commissioningAcknowledgement.trySend(Unit)
    }
}

sealed class AppEvent {
    data class ClusterAttributeObserved(
        val deviceId: DeviceId,
        val endpoint: Int,
        val clusterId: Long,
        val attributeId: Long,
    ) : AppEvent()

    data class ClusterCommandExecuted(
        val deviceId: DeviceId,
        val endpoint: Int,
        val clusterId: Long,
        val commandId: Long,
    ) : AppEvent()

    data object BindingStarted : AppEvent()
    data class BindingCompleted(val sourceNodeId: DeviceId, val targetNodeId: DeviceId) : AppEvent()
    data class DeviceDecommissioned(val deviceId: DeviceId) : AppEvent()
    data object CommissioningStarted : AppEvent()
    data class CommissioningSucceeded(val deviceId: DeviceId) : AppEvent()
    data object CommissioningFailed : AppEvent()

    data class NamedInteraction(val interaction: AppInteraction) : AppEvent()
}
