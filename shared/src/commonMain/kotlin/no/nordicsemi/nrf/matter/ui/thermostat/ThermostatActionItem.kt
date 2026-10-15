package no.nordicsemi.nrf.matter.ui.thermostat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/** Header action: an expand arrow. The local temperature is shown in the expanded thermostat card. */
@Composable
fun ThermostatActionItem() {
    Icon(
        imageVector = Icons.Outlined.ExpandMore,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
