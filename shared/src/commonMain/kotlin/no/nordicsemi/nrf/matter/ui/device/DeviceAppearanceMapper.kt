package no.nordicsemi.nrf.matter.ui.device

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import no.nordicsemi.nrf.matter.shared.generated.resources.Res
import no.nordicsemi.nrf.matter.shared.generated.resources.door_lock
import no.nordicsemi.nrf.matter.shared.generated.resources.door_lock_open_right
import no.nordicsemi.nrf.matter.shared.generated.resources.light_bulb
import no.nordicsemi.nrf.matter.shared.generated.resources.power_settings
import no.nordicsemi.nrf.matter.shared.generated.resources.smart_outlet
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceType
import no.nordicsemi.nrf.matter.model.StandardDeviceType
import no.nordicsemi.nrf.matter.nordic.isNordicManufacturerSpecific
import org.jetbrains.compose.resources.painterResource

@Composable
fun Device.toIcon(isActive: Boolean): Painter = when (deviceType) {
    StandardDeviceType.DOOR_LOCK.value -> painterResource(
        if (isActive) Res.drawable.door_lock else Res.drawable.door_lock_open_right
    )

    StandardDeviceType.OUTLET.value -> painterResource(Res.drawable.smart_outlet)
    StandardDeviceType.LIGHT_SWITCH.value -> painterResource(Res.drawable.power_settings)
    else -> painterResource(Res.drawable.light_bulb)
}

fun Device.toTitle(): String = basicInformation.productName ?: deviceType.toString()

fun Device.toSubtitle(): String = when {
    isNordicManufacturerSpecific() -> "Turn light ON or OFF"
    else -> deviceType.toSubtitle()
}

private fun DeviceType.toSubtitle(): String = when (this) {
    StandardDeviceType.DOOR_LOCK.value -> "Smart Lock"

    StandardDeviceType.OUTLET.value,
    StandardDeviceType.LIGHT_SWITCH.value -> "Bind the switch with other devices"

    StandardDeviceType.LIGHT_ON_OFF.value,
    StandardDeviceType.DIMMABLE_LIGHT.value,
    StandardDeviceType.COLOR_TEMPERATURE_LIGHT.value,
    StandardDeviceType.EXTENDED_COLOR_LIGHT.value -> "Turn light ON or OFF"

    else -> "Unknown device type."
}
