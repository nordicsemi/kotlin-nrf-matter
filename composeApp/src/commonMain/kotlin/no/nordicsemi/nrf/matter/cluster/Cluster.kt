package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

sealed class Cluster(protected val controller: MatterClient) {

    abstract val deviceId: DeviceId
    abstract val endpoint: Int
    abstract val id: Long

    protected suspend fun <T> readAttribute(attributeId: Long): T =
        controller.readAttribute(deviceId, endpoint, id, attributeId)

    protected fun <T> observeAttribute(attributeId: Long): Flow<T> =
        controller.observeAttribute(deviceId, endpoint, id, attributeId)

    protected suspend fun executeCommand(
        commandId: Long,
        value: Any? = null,
        timedInvokeTimeoutMs: Int? = null,
    ) {
        controller.executeCommand(value, deviceId, endpoint, id, commandId, timedInvokeTimeoutMs)
    }
}
