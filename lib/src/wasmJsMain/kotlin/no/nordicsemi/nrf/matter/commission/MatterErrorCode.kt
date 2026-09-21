package no.nordicsemi.nrf.matter.commission

/**
 * The web/demo target has no native Matter stack to carry a real error code, so failures raised
 * by the fake commissioning task never have one.
 */
internal actual fun Throwable.toMatterErrorCode(): Int? = null
