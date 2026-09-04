package no.nordicsemi.nrf.matter.commission

import no.nordicsemi.nrf.matter.adapters.IOSException

/**
 * The `NSError` code, for the failures `ios-matter` raises.
 *
 * A read failure crosses the bridge as an [IOSException] wrapping the `NSError` the Matter
 * framework reported, whose code is the Matter status.
 */
internal actual fun Throwable.matterErrorCode(): Int? {
    var cause: Throwable? = this

    while (cause != null) {
        (cause as? IOSException)?.let { return it.origin.code.toInt() }
        cause = cause.cause.takeIf { it != cause }
    }

    return null
}
