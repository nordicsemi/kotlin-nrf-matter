package no.nordicsemi.nrf.matter.matter

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber
import platform.Matter.MTRBaseDevice
import platform.Matter.MTRContextTagKey
import platform.Matter.MTRDataKey
import platform.Matter.MTRStructureValueType
import platform.Matter.MTRTypeKey
import platform.Matter.MTRValueKey

/** One TLV-tagged field of a Matter command invocation, keyed by its spec-defined context tag. */
internal data class CommandField(val contextTag: Int, val type: String, val value: Any)

/**
 * Mirrors `iosApp/iosApp/kotlin/local/CommandExecutor.swift`. Generalized to support
 * multi-field (and zero-field) command invocations, needed for commands like
 * "Move to Level with On/Off" that the original Swift only used with a single field.
 */
@OptIn(ExperimentalForeignApi::class)
internal class CommandExecutor(deviceId: NSNumber) {

    private val baseDevice: MTRBaseDevice = MTRBaseDevice.deviceWithNodeID(
        nodeID = deviceId,
        controller = LocalControllerProviderImpl.getController("CommandExecutor"),
    )

    suspend fun executeCommand(
        endpoint: NSNumber,
        cluster: NSNumber,
        command: NSNumber,
        type: String,
        value: Any,
    ) = executeCommand(endpoint, cluster, command, listOf(CommandField(0, type, value)))

    suspend fun executeCommand(
        endpoint: NSNumber,
        cluster: NSNumber,
        command: NSNumber,
        fields: List<CommandField> = emptyList(),
    ) {
        val fieldValues = mapOf(
            MTRTypeKey to MTRStructureValueType,
            MTRValueKey to fields.map { field ->
                mapOf(
                    MTRContextTagKey to NSNumber(int = field.contextTag),
                    MTRDataKey to mapOf(
                        MTRTypeKey to field.type,
                        MTRValueKey to field.value,
                    ),
                )
            },
        )

        mtrCall { completion ->
            baseDevice.invokeCommandWithEndpointID(
                endpointID = endpoint,
                clusterID = cluster,
                commandID = command,
                commandFields = fieldValues,
                timedInvokeTimeout = null,
                queue = defaultMatterQueue(),
                completion = completion,
            )
        }
    }
}
