package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

open class Cluster(
    val deviceId: DeviceId,
    val endpoint: Int,
    val id: Long,
    protected val controller: MatterClient,
) {

    suspend fun <T> readAttribute(attributeId: Long): T =
        controller.readAttribute(deviceId, endpoint, id, attributeId)

    fun <T> observeAttribute(attributeId: Long): Flow<T> =
        controller.observeAttribute(deviceId, endpoint, id, attributeId)

    suspend fun executeCommand(
        commandId: Long,
        value: Any? = null,
        timedInvokeTimeoutMs: Int? = null,
    ) {
        controller.executeCommand(value, deviceId, endpoint, id, commandId, timedInvokeTimeoutMs)
    }
}
