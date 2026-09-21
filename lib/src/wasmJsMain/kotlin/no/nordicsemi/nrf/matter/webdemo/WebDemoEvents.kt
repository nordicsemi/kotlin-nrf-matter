package no.nordicsemi.nrf.matter.webdemo

import kotlinx.coroutines.flow.MutableStateFlow
import no.nordicsemi.nrf.matter.model.DeviceId

/**
 * The seam a docs/demo host observes to know what the visitor just did, so it can reveal the
 * matching documentation snippet. This exists only in the wasmJs target -- it has no effect on
 * and no visibility from the Android/iOS apps.
 *
 * [WebMatterClient] is the single choke point every real cluster controller in `:shared` writes
 * through, so [ClusterAttributeWritten]/[ClusterCommandExecuted] cover on/off, lock, brightness,
 * manufacturer LED and the "generate number" command alike -- the docs host maps them to a
 * doc anchor by (device kind, clusterId), not by re-deriving which specific control fired.
 */
object WebDemoEvents {
    val lastAction = MutableStateFlow<WebDemoAction?>(null)

    internal fun publish(action: WebDemoAction) {
        lastAction.value = action
    }
}

sealed class WebDemoAction {
    data class ClusterAttributeWritten(
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
}
