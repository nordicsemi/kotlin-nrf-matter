package no.nordicsemi.nrf.matter.model

/** Smoke CO Alarm ExpressedStateEnum: the state the device is currently expressing visually/audibly. */
enum class ExpressedState(val value: Int) {
    NORMAL(0),
    SMOKE_ALARM(1),
    CO_ALARM(2),
    BATTERY_ALERT(3),
    TESTING(4),
    HARDWARE_FAULT(5),
    END_OF_SERVICE(6),
    INTERCONNECT_SMOKE(7),
    INTERCONNECT_CO(8),
}
