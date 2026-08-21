package no.nordicsemi.nrf.matter.ui.device

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import no.nordicsemi.nrf.matter.composeapp.generated.resources.Res
import no.nordicsemi.nrf.matter.composeapp.generated.resources.door_lock
import no.nordicsemi.nrf.matter.composeapp.generated.resources.door_lock_open_right
import no.nordicsemi.nrf.matter.composeapp.generated.resources.light_bulb
import no.nordicsemi.nrf.matter.composeapp.generated.resources.power_settings
import no.nordicsemi.nrf.matter.composeapp.generated.resources.smart_outlet
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceType
import org.jetbrains.compose.resources.painterResource

@Composable
fun Device.toIcon(isActive: Boolean): Painter = when (deviceType) {
    DeviceType.DOOR_LOCK -> painterResource(
        if (isActive) Res.drawable.door_lock else Res.drawable.door_lock_open_right
    )

    DeviceType.OUTLET -> painterResource(Res.drawable.smart_outlet)
    DeviceType.LIGHT_SWITCH -> painterResource(Res.drawable.power_settings)
    else -> painterResource(Res.drawable.light_bulb)
}

fun Device.toTitle(): String = manufacturerSpecificName() ?: productName ?: deviceType.toString()

fun Device.toSubtitle(): String = when (deviceType) {
    DeviceType.DOOR_LOCK -> "Smart Lock"

    DeviceType.OUTLET,
    DeviceType.LIGHT_SWITCH -> "Bind the switch with other devices"

    DeviceType.LIGHT_ON_OFF,
    DeviceType.DIMMABLE_LIGHT,
    DeviceType.COLOR_TEMPERATURE_LIGHT,
    DeviceType.EXTENDED_COLOR_LIGHT,
    DeviceType.MANUFACTURER_SPECIFIC_DEVICE -> "Turn light ON or OFF"

    DeviceType.UNSUPPORTED -> "Unknown device type."
}

private fun Device.manufacturerSpecificName(): String? = deviceMatterInfo
    .firstNotNullOfOrNull { it.manufacturerSpecificData?.name }
    ?.takeIf { it.isNotBlank() }

fun Device.isBindingCapable(): Boolean {
    return deviceMatterInfo.any { it.serverClusters.contains(6) }
}
