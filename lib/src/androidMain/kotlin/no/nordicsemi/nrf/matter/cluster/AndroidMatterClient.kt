package no.nordicsemi.nrf.matter.cluster

import chip.devicecontroller.ChipStructs
import chip.devicecontroller.ChipTLVType
import kotlinx.coroutines.flow.Flow
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

private fun Any?.toCommonValue(): Any? = when (this) {
    is List<*> -> map { it.toCommonValue() }

    is ChipStructs.DescriptorClusterDeviceTypeStruct -> MatterStruct(
        mapOf(DescriptorClusterInfo.DeviceTypeStruct.DEVICE_TYPE to deviceType)
    )

    is ChipStructs.RvcRunModeClusterModeOptionStruct -> MatterStruct(
        mapOf(
            ModeOptionStruct.LABEL to label,
            ModeOptionStruct.MODE to mode,
            ModeOptionStruct.MODE_TAGS to modeTags.toCommonValue()
        )
    )

    is ChipStructs.RvcRunModeClusterModeTagStruct -> MatterStruct(
        mapOf(ModeTagStruct.VALUE to value)
    )

    is ChipStructs.RvcCleanModeClusterModeOptionStruct -> MatterStruct(
        mapOf(
            ModeOptionStruct.LABEL to label,
            ModeOptionStruct.MODE to mode,
            ModeOptionStruct.MODE_TAGS to modeTags.toCommonValue()
        )
    )

    is ChipStructs.RvcCleanModeClusterModeTagStruct -> MatterStruct(
        mapOf(ModeTagStruct.VALUE to value)
    )

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

    is ChipTLVType.ArrayType -> (0 until size()).map { value(it).toCommonValue() }

    is ChipTLVType.StructType -> MatterStruct(
        value().associate { it.contextTagNum() to it.value().toCommonValue() }
    )

    is ChipTLVType.IntType -> value()
    is ChipTLVType.UIntType -> value()
    is ChipTLVType.StringType -> value()
    is ChipTLVType.BooleanType -> value()
    is ChipTLVType.DoubleType -> value()
    is ChipTLVType.FloatType -> value()
    is ChipTLVType.ByteArrayType -> value()
    is ChipTLVType.NullType, is ChipTLVType.EmptyType -> null

    else -> this
}
