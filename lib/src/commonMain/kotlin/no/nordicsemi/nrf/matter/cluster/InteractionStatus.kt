package no.nordicsemi.nrf.matter.cluster

/**
 * The Interaction Model status a device answered a command (or other interaction) with, as
 * defined in the Matter specification's status codes table.
 *
 * Only the statuses worth telling a user apart are listed; anything else maps to [OTHER].
 */
enum class InteractionStatus(val code: Int) {
    FAILURE(0x01),
    UNSUPPORTED_ACCESS(0x7E),
    UNSUPPORTED_ENDPOINT(0x7F),
    UNSUPPORTED_COMMAND(0x81),
    INVALID_COMMAND(0x85),
    CONSTRAINT_ERROR(0x87),
    RESOURCE_EXHAUSTED(0x89),
    TIMEOUT(0x94),
    BUSY(0x9C),
    UNSUPPORTED_CLUSTER(0xC3),
    INVALID_IN_STATE(0xCB),
    OTHER(-1),
}

/**
 * The Interaction Model status carried by a failure raised by the platform Matter stack, or
 * `null` when the failure didn't come from the device (for example, an encoding error).
 *
 * A timeout waiting for the device is reported as [InteractionStatus.TIMEOUT], even though the
 * platforms raise it as a transport error rather than a status sent by the device.
 */
fun Throwable.toInteractionStatus(): InteractionStatus? {
    val code = toInteractionStatusCode() ?: return null
    return InteractionStatus.entries.firstOrNull { it.code == code } ?: InteractionStatus.OTHER
}

internal expect fun Throwable.toInteractionStatusCode(): Int?
