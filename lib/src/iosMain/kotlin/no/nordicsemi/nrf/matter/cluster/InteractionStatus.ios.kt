package no.nordicsemi.nrf.matter.cluster

import no.nordicsemi.nrf.matter.adapters.IOSException

// Statuses sent by the device come in MTRInteractionErrorDomain with the status as the code;
// Matter.framework's own failures come in MTRErrorDomain, where MTRErrorCodeTimeout is 9.
private const val MTR_INTERACTION_ERROR_DOMAIN = "MTRInteractionErrorDomain"
private const val MTR_ERROR_DOMAIN = "MTRErrorDomain"
private const val MTR_ERROR_CODE_TIMEOUT = 9L

internal actual fun Throwable.toInteractionStatusCode(): Int? {
    var cause: Throwable? = this

    while (cause != null) {
        (cause as? IOSException)?.origin?.let { error ->
            return when {
                error.domain == MTR_INTERACTION_ERROR_DOMAIN -> error.code.toInt()
                error.domain == MTR_ERROR_DOMAIN && error.code == MTR_ERROR_CODE_TIMEOUT -> InteractionStatus.TIMEOUT.code
                else -> null
            }
        }
        cause = cause.cause.takeIf { it != cause }
    }

    return null
}
