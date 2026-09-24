package no.nordicsemi.nrf.matter.model

enum class RvcOperationalState(val value: Int) {
    STOPPED(0x00),
    RUNNING(0x01),
    PAUSED(0x02),
    ERROR(0x03),
    SEEKING_CHARGER(0x40),
    CHARGING(0x41),
    DOCKED(0x42),
}
