package no.nordicsemi.nrf.matter.events

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import no.nordicsemi.nrf.matter.model.DeviceId

/**
 * The seam a docs/demo host observes to know what the visitor just did, so it can reveal the
 * matching documentation snippet. `:shared` publishes unconditionally on every platform; only
 * the wasmJs docs host actually collects [events], so publishing has no effect on Android/iOS.
 *
 * [WebMatterClient] is the single choke point every real cluster controller in `:shared` reads
 * and writes through, so [ClusterAttributeObserved]/[ClusterCommandExecuted] cover on/off, lock,
 * brightness, manufacturer LED and the "generate number" command alike -- the docs host maps
 * them to a doc anchor by (device kind, clusterId), not by re-deriving which specific control
 * fired.
 */
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
    /** A real cluster controller started observing this attribute -- fires the moment a device card expands. */
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

    /** A UI element with no natural cluster-level signal was tapped -- published from `:shared`. */
    data class NamedInteraction(val interaction: AppInteraction) : AppEvent()
}
