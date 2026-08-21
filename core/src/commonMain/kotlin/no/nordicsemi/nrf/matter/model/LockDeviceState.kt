package no.nordicsemi.nrf.matter.model

enum class LockDeviceState(val value: Int) {
    NOT_FULLY_LOCKED(0),
    LOCKED(1),
    UNLOCKED(2),
    UNLATCHED(3),
}
