package no.nordicsemi.nrf.matter.commission

import chip.devicecontroller.ChipDeviceControllerException

/**
 * The CHIP error code, for the failures the device controller raises.
 *
 * CHIP wraps the read failures behind [no.nordicsemi.nrf.matter.cluster.MatterClient] in
 * [IllegalStateException], so the controller's own exception is looked for down the cause chain
 * rather than only at the top.
 */
internal actual fun Throwable.matterErrorCode(): Int? {
    var cause: Throwable? = this

    while (cause != null) {
        (cause as? ChipDeviceControllerException)?.let { return it.errorCode.toInt() }
        cause = cause.cause.takeIf { it != cause }
    }

    return null
}
