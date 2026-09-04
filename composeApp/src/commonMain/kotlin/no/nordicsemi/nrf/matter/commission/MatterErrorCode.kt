package no.nordicsemi.nrf.matter.commission

/**
 * The Matter status or error code carried by a failure raised by the platform Matter stack, or
 * `null` for a failure that carries none.
 *
 * Reported in [CommissioningException.errorCode], which the commissioning screens show so that a
 * failure can be looked up in the specification.
 */
internal expect fun Throwable.matterErrorCode(): Int?
