package no.nordicsemi.nrf.matter.commission

import no.nordicsemi.nrf.matter.model.DeviceId

data class CommissioningException(
    val deviceId: DeviceId?,
    val stage: Stage,
    val errorCode: Int?,
    val displayMessage: String,
    val fabricId: Int = 1,
) : Throwable(displayMessage) {

    val displayFabricId = fabricId.toHexString(ShortHexFormat)
    val displayDeviceId = deviceId?.longValue?.toHexString(ShortHexFormat) ?: "unknown"
    val displayErrorCode = errorCode?.toHexString(ShortHexFormat) ?: "unknown"

    companion object {

        private val ShortHexFormat = HexFormat {
            upperCase = true
            number {
                removeLeadingZeros = true
                prefix = "0x"
            }
        }

        fun unknown(stage: Stage) = CommissioningException(
            deviceId = null,
            stage = stage,
            errorCode = null,
            displayMessage = "Unknown error"
        )
    }
}

enum class Stage {
    COMMISSIONING,
    READ_BASIC_INFORMATION,
    READ_DESCRIPTOR_CLUSTER
}
