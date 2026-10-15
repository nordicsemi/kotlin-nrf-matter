package no.nordicsemi.nrf.matter.model

/** Thermostat cluster SystemModeEnum values that the app can show or select. */
enum class ThermostatSystemMode(val value: Int, val label: String) {
    OFF(0, "Off"),
    AUTO(1, "Auto"),
    COOL(3, "Cool"),
    HEAT(4, "Heat"),
    EMERGENCY_HEAT(5, "Emergency heat"),
    PRECOOLING(6, "Precooling"),
    FAN_ONLY(7, "Fan only"),
    DRY(8, "Dry"),
    SLEEP(9, "Sleep"),
    OTHER(-1, "Other");

    companion object {
        fun from(value: Int): ThermostatSystemMode = entries.firstOrNull { it.value == value } ?: OTHER
    }
}
