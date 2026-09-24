package no.nordicsemi.nrf.matter.ui.rvc

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import no.nordicsemi.nrf.matter.model.RvcOperationalState
import no.nordicsemi.nrf.matter.theme.NordicFall

fun Int.toRvcOperationalState(): RvcOperationalState? =
    RvcOperationalState.entries.firstOrNull { it.value == this }

fun RvcOperationalState?.toLabel(): String = when (this) {
    RvcOperationalState.RUNNING -> "Cleaning"
    RvcOperationalState.PAUSED -> "Paused"
    RvcOperationalState.STOPPED -> "Stopped"
    RvcOperationalState.ERROR -> "Error"
    RvcOperationalState.SEEKING_CHARGER -> "Returning to dock"
    RvcOperationalState.CHARGING -> "Charging"
    RvcOperationalState.DOCKED -> "Docked"
    null -> "Unknown"
}

fun RvcOperationalState?.toIcon(): ImageVector = when (this) {
    RvcOperationalState.RUNNING -> Icons.Filled.CleaningServices
    RvcOperationalState.PAUSED -> Icons.Filled.Pause
    RvcOperationalState.STOPPED -> Icons.Filled.Stop
    RvcOperationalState.ERROR -> Icons.Filled.ErrorOutline
    RvcOperationalState.SEEKING_CHARGER -> Icons.Filled.Home
    RvcOperationalState.CHARGING -> Icons.Filled.BatteryChargingFull
    RvcOperationalState.DOCKED -> Icons.Filled.Home
    null -> Icons.Filled.CleaningServices
}

@Composable
fun RvcOperationalState?.toColor(): Color = when (this) {
    RvcOperationalState.RUNNING -> MaterialTheme.colorScheme.primary
    RvcOperationalState.PAUSED -> MaterialTheme.colorScheme.tertiary
    RvcOperationalState.ERROR -> MaterialTheme.colorScheme.error
    RvcOperationalState.CHARGING,
    RvcOperationalState.SEEKING_CHARGER -> NordicFall
    RvcOperationalState.STOPPED,
    RvcOperationalState.DOCKED,
    null -> MaterialTheme.colorScheme.onSurfaceVariant
}
