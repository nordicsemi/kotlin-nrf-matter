package no.nordicsemi.nrf.matter.commission

import no.nordicsemi.nrf.matter.adapters.IOSException

internal actual fun Throwable.toMatterErrorCode(): Int? {
    var cause: Throwable? = this

    while (cause != null) {
        (cause as? IOSException)?.let { return it.origin.code.toInt() }
        cause = cause.cause.takeIf { it != cause }
    }

    return null
}
