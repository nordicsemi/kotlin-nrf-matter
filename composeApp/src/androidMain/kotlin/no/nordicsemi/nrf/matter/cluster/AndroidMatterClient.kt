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
        return chipClient.readAttribute(devicePointer, endpoint, clusterId, attributeId)
            .toCommonValue() as T
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
                it.toCommonValue() as T
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

/**
 * Normalizes what the CHIP controller decoded into the shapes [MatterClient] promises: a Matter
 * list as a [List] and a Matter structure as a [MatterStruct], applied recursively.
 *
 * CHIP hands back the TLV decoded Java value, so a list arrives as a `List` and a structure as a
 * `Map` keyed by context tag - as a number, or as its decimal string in some SDK versions, hence
 * both being accepted. This is the one place that assumption lives: if a future CHIP release
 * decodes structures differently, this is what needs adjusting rather than every cluster.
 */
private fun Any?.toCommonValue(): Any? = when (this) {
    is List<*> -> map { it.toCommonValue() }

    is Map<*, *> -> MatterStruct(
        entries.mapNotNull { (contextTag, value) ->
            val tag = when (contextTag) {
                is Number -> contextTag.toLong()
                is String -> contextTag.toLongOrNull()
                else -> null
            }

            tag?.let { it to value.toCommonValue() }
        }.toMap()
    )

    else -> this
}
