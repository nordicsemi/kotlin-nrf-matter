package no.nordicsemi.nrf.matter.cluster

import chip.devicecontroller.ChipDeviceControllerException

// CHIP_ERROR encoding: statuses sent by the device are reported as 0x500 + status
// (SdkPart::kIMGlobalStatus), and CHIP_ERROR_TIMEOUT is 0x32.
private const val IM_GLOBAL_STATUS_BASE = 0x500
private const val CHIP_ERROR_TIMEOUT = 0x32

/**
 * CHIP wraps invoke failures in [IllegalStateException], so the controller's own exception is
 * looked for down the cause chain rather than only at the top.
 */
internal actual fun Throwable.toInteractionStatusCode(): Int? {
    var cause: Throwable? = this

    while (cause != null) {
        (cause as? ChipDeviceControllerException)?.let {
            val errorCode = it.errorCode.toInt()
            return when {
                errorCode == CHIP_ERROR_TIMEOUT -> InteractionStatus.TIMEOUT.code
                errorCode and 0xFF00 == IM_GLOBAL_STATUS_BASE -> errorCode and 0xFF
                else -> null
            }
        }
        cause = cause.cause.takeIf { it != cause }
    }

    return null
}
