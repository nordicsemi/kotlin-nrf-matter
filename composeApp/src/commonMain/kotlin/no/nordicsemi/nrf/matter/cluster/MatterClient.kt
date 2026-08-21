package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

/**
 * Platform independent access to the Matter interaction model.
 *
 * Cluster, attribute and command identifiers are [Long] because manufacturer specific identifiers
 * (e.g. `0xFFF1FC01`) do not fit into a signed [Int].
 */
abstract class MatterClient {

    abstract suspend fun <T> setAttribute(
        value: T,
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long
    )

    abstract suspend fun <T> readAttribute(
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long
    ): T

    abstract fun <T> observeAttribute(
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long
    ): Flow<T>

    abstract suspend fun <T> executeCommand(
        value: T,
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        commandId: Long,
        timedInvokeTimeoutMs: Int? = null
    )
}
