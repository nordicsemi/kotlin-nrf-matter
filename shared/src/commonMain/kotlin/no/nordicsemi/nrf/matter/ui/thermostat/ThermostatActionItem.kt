package no.nordicsemi.nrf.matter.ui.thermostat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import no.nordicsemi.nrf.matter.ui.temperature.TemperatureSensorActionItem

/** Header badge: the local temperature, or an expand arrow when the device doesn't expose it. */
@Composable
fun ThermostatActionItem(localTemperatureCelsius: Float?) {
    if (localTemperatureCelsius != null) {
        TemperatureSensorActionItem(temperatureCelsius = localTemperatureCelsius)
    } else {
        Icon(
            imageVector = Icons.Outlined.ExpandMore,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
