package no.nordicsemi.nrf.matter.ui.device

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceType
import no.nordicsemi.nrf.matter.model.SupportedDeviceType
import no.nordicsemi.nrf.matter.nordic.NordicDeviceType
import no.nordicsemi.nrf.matter.nordic.isNordicManufacturerSpecific
import no.nordicsemi.nrf.matter.shared.generated.resources.Res
import no.nordicsemi.nrf.matter.shared.generated.resources.contact_sensor
import no.nordicsemi.nrf.matter.shared.generated.resources.door_lock
import no.nordicsemi.nrf.matter.shared.generated.resources.door_lock_open_right
import no.nordicsemi.nrf.matter.shared.generated.resources.light_bulb
import no.nordicsemi.nrf.matter.shared.generated.resources.power_settings
import no.nordicsemi.nrf.matter.shared.generated.resources.smart_outlet
import no.nordicsemi.nrf.matter.shared.generated.resources.smoke_co_alarm
import no.nordicsemi.nrf.matter.shared.generated.resources.temperature
import org.jetbrains.compose.resources.painterResource

@Composable
fun Device.toIcon(isActive: Boolean): Painter = when (deviceType) {
    SupportedDeviceType.DOOR_LOCK.value -> painterResource(
        if (isActive) Res.drawable.door_lock else Res.drawable.door_lock_open_right
    )

    SupportedDeviceType.OUTLET.value -> painterResource(Res.drawable.smart_outlet)
    SupportedDeviceType.LIGHT_SWITCH.value -> painterResource(Res.drawable.power_settings)
    SupportedDeviceType.CONTACT_SENSOR.value -> painterResource(Res.drawable.contact_sensor)
    SupportedDeviceType.TEMPERATURE_SENSOR.value -> painterResource(Res.drawable.temperature)
    SupportedDeviceType.ROBOTIC_VACUUM_CLEANER.value -> rememberVectorPainter(Icons.Outlined.CleaningServices)
    SupportedDeviceType.SMOKE_CO_ALARM.value -> painterResource(Res.drawable.smoke_co_alarm)
    else -> painterResource(Res.drawable.light_bulb)
}

fun Device.toTitle(): String = basicInformation.productName ?: deviceType.toString()

fun Device.toSubtitle(): String = when {
    isNordicManufacturerSpecific() -> "Turn light ON or OFF"
    else -> deviceType.toSubtitle()
}

private fun DeviceType.toSubtitle(): String = when (this) {
    SupportedDeviceType.DOOR_LOCK.value -> "Smart Lock"

    SupportedDeviceType.OUTLET.value,
    SupportedDeviceType.DIMMER_SWITCH.value,
    SupportedDeviceType.LIGHT_SWITCH.value -> "Bind the switch with other devices"

    NordicDeviceType,
    SupportedDeviceType.LIGHT_ON_OFF.value,
    SupportedDeviceType.DIMMABLE_LIGHT.value,
    SupportedDeviceType.COLOR_TEMPERATURE_LIGHT.value,
    SupportedDeviceType.EXTENDED_COLOR_LIGHT.value -> "Turn light ON or OFF"

    SupportedDeviceType.CONTACT_SENSOR.value -> "Indicates opening status."
    SupportedDeviceType.TEMPERATURE_SENSOR.value -> "Measures temperature"
    SupportedDeviceType.ROBOTIC_VACUUM_CLEANER.value -> "Robot vacuum cleaner"
    SupportedDeviceType.SMOKE_CO_ALARM.value -> "Detects smoke and carbon monoxide"

    else -> "Unsupported device type."
}
