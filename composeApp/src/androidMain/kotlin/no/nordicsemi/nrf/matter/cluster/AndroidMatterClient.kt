package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import no.nordicsemi.nrf.matter.chip.ChipClient
import no.nordicsemi.nrf.matter.model.DeviceId

class AndroidMatterClient(
    private val chipClient: ChipClient
) : MatterClient() {

    override suspend fun <T> setAttribute(
        value: T,
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long
    ) {
        val devicePointer = chipClient.getConnectedDevicePointer(deviceId.longValue)
        chipClient.writeAttribute(devicePointer, endpoint, clusterId, attributeId, value)
    }

    override suspend fun <T> readAttribute(
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long
    ): T {
        val devicePointer = chipClient.getConnectedDevicePointer(deviceId.longValue)
        @Suppress("UNCHECKED_CAST")
        return chipClient.readAttribute(devicePointer, endpoint, clusterId, attributeId) as T
    }

    override fun <T> observeAttribute(
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long
    ): Flow<T> {
        return chipClient.observeAttribute(deviceId, endpoint, clusterId, attributeId)
            .map {
                @Suppress("UNCHECKED_CAST")
                it as T
            }
    }

    override suspend fun <T> executeCommand(
        value: T,
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        commandId: Long,
        timedInvokeTimeoutMs: Int?
    ) {
        val devicePointer = chipClient.getConnectedDevicePointer(deviceId.longValue)
        chipClient.invokeCommand(
            devicePtr = devicePointer,
            endpoint = endpoint,
            clusterId = clusterId,
            commandId = commandId,
            value = value,
            timedRequestTimeoutMs = timedInvokeTimeoutMs ?: 0,
        )
    }
}
