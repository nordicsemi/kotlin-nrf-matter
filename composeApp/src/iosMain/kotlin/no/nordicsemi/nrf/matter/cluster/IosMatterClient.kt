@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package no.nordicsemi.nrf.matter.cluster

import iosMatter.LocalMatterClient
import iosMatter.MatterValue
import iosMatter.MatterValueTypeBoolean
import iosMatter.MatterValueTypeBytes
import iosMatter.MatterValueTypeDouble
import iosMatter.MatterValueTypeFloat
import iosMatter.MatterValueTypeSignedInteger
import iosMatter.MatterValueTypeString
import iosMatter.MatterValueTypeUnsignedInteger
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.adapters.IOSException
import no.nordicsemi.nrf.matter.adapters.handleResult
import no.nordicsemi.nrf.matter.adapters.toNSNumber
import no.nordicsemi.nrf.matter.model.DeviceId
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.Foundation.create
import platform.posix.memcpy
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class IosMatterClient : MatterClient() {

    private val client = LocalMatterClient()

    /**
     * Writes [value] to the given attribute.
     *
     * The Kotlin type of [value] determines the Matter type sent to the device, so signedness
     * matters: pass unsigned types ([UByte], [UShort], [UInt], [ULong]) for Matter `uintX`
     * attributes and the signed counterparts for `intX` attributes.
     */
    override suspend fun <T> setAttribute(
        value: T,
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long
    ) {
        return suspendCancellableCoroutine { continuation ->
            client.writeAttributeWithDeviceId(
                deviceId = deviceId.toNSNumber(),
                endpoint = endpoint.toNSNumber(),
                cluster = clusterId.toNSNumber(),
                attribute = attributeId.toNSNumber(),
                value = value.toMatterValue(),
            ) { error ->
                continuation.handleResult(error)
            }
        }
    }

    override suspend fun <T> readAttribute(
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long
    ): T {
        return suspendCancellableCoroutine { continuation ->
            client.readAttributeWithDeviceId(
                deviceId = deviceId.toNSNumber(),
                endpoint = endpoint.toNSNumber(),
                cluster = clusterId.toNSNumber(),
                attribute = attributeId.toNSNumber(),
            ) { value, error ->
                continuation.resumeWithValue(value, error)
            }
        }
    }

    /**
     * Subscribes to the given attribute and emits its value on every report from the device.
     *
     * The subscription lives as long as the app and is shared with every other observer of the
     * same device, so cancelling the collection only stops the delivery of reports.
     */
    override fun <T> observeAttribute(
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long
    ): Flow<T> {
        return callbackFlow {
            client.observeAttributeWithDeviceId(
                deviceId = deviceId.toNSNumber(),
                endpoint = endpoint.toNSNumber(),
                cluster = clusterId.toNSNumber(),
                attribute = attributeId.toNSNumber(),
                onUpdate = { value ->
                    @Suppress("UNCHECKED_CAST")
                    trySend(value?.toKotlinValue() as T)
                }
            ) { error ->
                error?.let { close(IOSException(it)) }
            }

            awaitClose { }
        }
    }

    /**
     * Invokes the given command with [value] as its single field, or without fields when [value]
     * is `null` or [Unit].
     *
     * The command is sent when this method is called; the returned flow replays its outcome and
     * emits the first field of the command response, or nothing when the device answered with a
     * status and no data.
     *
     * When [timedInvokeTimeoutMs] is given the command is sent as a timed invoke.
     */
    override suspend fun <T> executeCommand(
        value: T,
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        commandId: Long,
        timedInvokeTimeoutMs: Int?
    ) {
        suspendCancellableCoroutine { continuation ->
            client.executeCommandWithDeviceId(
                deviceId = deviceId.toNSNumber(),
                endpoint = endpoint.toNSNumber(),
                cluster = clusterId.toNSNumber(),
                command = commandId.toNSNumber(),
                value = value.takeUnless { it == null || it == Unit }?.toMatterValue(),
                timedInvokeTimeoutMs = timedInvokeTimeoutMs?.toNSNumber(),
            ) { responseValue, error ->
                continuation.handleResult<Unit>(error)
            }
        }
    }
}

/**
 * Resumes with the Kotlin representation of [value], or with the failure reported by iOS.
 */
private fun <T> CancellableContinuation<T>.resumeWithValue(value: MatterValue?, error: NSError?) {
    if (error != null) {
        resumeWithException(IOSException(error))
    } else {
        @Suppress("UNCHECKED_CAST")
        resume(value?.toKotlinValue() as T)
    }
}

/**
 * Converts a value reported by Matter into its Kotlin counterpart.
 *
 * Integers are widened to [Long] because the Matter type only tells signedness, not width, and
 * types that do not cross the bridge - structures and lists - are converted to `null`.
 */
private fun MatterValue.toKotlinValue(): Any? = when (type) {
    MatterValueTypeBoolean -> number?.boolValue
    MatterValueTypeSignedInteger -> number?.longLongValue
    MatterValueTypeUnsignedInteger -> number?.unsignedLongLongValue?.toLong()
    MatterValueTypeFloat -> number?.floatValue
    MatterValueTypeDouble -> number?.doubleValue
    MatterValueTypeString -> string
    MatterValueTypeBytes -> bytes?.toByteArray()
    else -> null
}

/**
 * Converts a Kotlin value into a Matter value, deriving the Matter type from the Kotlin type.
 */
private fun Any?.toMatterValue(): MatterValue = when (this) {
    null -> MatterValue.nullValue()
    is Boolean -> MatterValue.boolean(this)
    is Byte -> MatterValue.signedInteger(NSNumber(char = this))
    is Short -> MatterValue.signedInteger(NSNumber(short = this))
    is Int -> MatterValue.signedInteger(NSNumber(int = this))
    is Long -> MatterValue.signedInteger(NSNumber(longLong = this))
    is UByte -> MatterValue.unsignedInteger(NSNumber(unsignedChar = this))
    is UShort -> MatterValue.unsignedInteger(NSNumber(unsignedShort = this))
    is UInt -> MatterValue.unsignedInteger(NSNumber(unsignedInt = this))
    is ULong -> MatterValue.unsignedInteger(NSNumber(unsignedLongLong = this))
    is Float -> MatterValue.float(this)
    is Double -> MatterValue.double(this)
    is String -> MatterValue.string(this)
    is ByteArray -> MatterValue.bytes(toNSData())
    else -> throw IllegalArgumentException(
        "Unsupported Matter value type: ${this::class.simpleName}. Supported types are Boolean, " +
                "signed and unsigned integers, Float, Double, String and ByteArray."
    )
}

private fun ByteArray.toNSData(): NSData = if (isEmpty()) {
    NSData()
} else {
    usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = size.toULong())
    }
}

private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    if (size == 0) return ByteArray(0)

    return ByteArray(size).apply {
        usePinned { pinned -> memcpy(pinned.addressOf(0), bytes, length) }
    }
}
