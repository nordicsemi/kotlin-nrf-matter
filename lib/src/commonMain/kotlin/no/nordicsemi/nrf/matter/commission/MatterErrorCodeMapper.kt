package no.nordicsemi.nrf.matter.commission

fun interface MatterErrorCodeMapper {
    fun errorCodeOf(throwable: Throwable): Int?
}
