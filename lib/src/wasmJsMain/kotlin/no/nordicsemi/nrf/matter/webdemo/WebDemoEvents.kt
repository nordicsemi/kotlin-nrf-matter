package no.nordicsemi.nrf.matter.webdemo

import kotlinx.coroutines.flow.MutableStateFlow
import no.nordicsemi.nrf.matter.model.DeviceId

/**
 * The seam a docs/demo host observes to know what the visitor just did, so it can reveal the
 * matching documentation snippet. This exists only in the wasmJs target -- it has no effect on
 * and no visibility from the Android/iOS apps.
 *
 * [WebMatterClient] is the single choke point every real cluster controller in `:shared` reads
 * and writes through, so [ClusterAttributeObserved]/[ClusterCommandExecuted] cover on/off, lock,
 * brightness, manufacturer LED and the "generate number" command alike -- the docs host maps
 * them to a doc anchor by (device kind, clusterId), not by re-deriving which specific control
 * fired.
 */
object WebDemoEvents {
    val lastAction = MutableStateFlow<WebDemoAction?>(null)

    /** Public (not internal): `:shared`'s wasmJs actuals publish named interactions too, see [WebDemoAction.NamedInteraction]. */
    fun publish(action: WebDemoAction) {
        lastAction.value = action
    }
}

sealed class WebDemoAction {
    /** A real cluster controller started observing this attribute -- fires the moment a device card expands. */
    data class ClusterAttributeObserved(
        val deviceId: DeviceId,
        val endpoint: Int,
        val clusterId: Long,
        val attributeId: Long,
    ) : WebDemoAction()

    data class ClusterCommandExecuted(
        val deviceId: DeviceId,
        val endpoint: Int,
        val clusterId: Long,
        val commandId: Long,
    ) : WebDemoAction()

    data object BindingStarted : WebDemoAction()
    data class BindingCompleted(val sourceNodeId: DeviceId, val targetNodeId: DeviceId) : WebDemoAction()
    data class DeviceDecommissioned(val deviceId: DeviceId) : WebDemoAction()
    data object CommissioningStarted : WebDemoAction()
    data class CommissioningSucceeded(val deviceId: DeviceId) : WebDemoAction()
    data object CommissioningFailed : WebDemoAction()

    /** A UI element with no natural cluster-level signal was tapped -- published from `:shared`. */
    data class NamedInteraction(val key: String) : WebDemoAction()
}
