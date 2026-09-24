package no.nordicsemi.nrf.matter.model

enum class AlarmState(val value: Int) {
    NORMAL(0),
    WARNING(1),
    CRITICAL(2),
}