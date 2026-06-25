package no.nordicsemi.nrf.matter.model

enum class LockDeviceState(val value: Int) {
    NOT_FULLY_LOCKED(0),
    LOCKED(1),
    UNLOCKED(2);

    companion object {
        fun create(value: Int): LockDeviceState {
            return LockDeviceState.entries.first { it.value == value }
        }
    }
}
