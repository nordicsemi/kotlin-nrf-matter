package no.nordicsemi.nrf.matter.ui.smokecoalarm

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import no.nordicsemi.nrf.matter.model.AlarmState
import no.nordicsemi.nrf.matter.model.ExpressedState
import no.nordicsemi.nrf.matter.theme.NordicFall
import no.nordicsemi.nrf.matter.theme.NordicRed

fun Number.toAlarmState(): AlarmState =
    AlarmState.entries.firstOrNull { it.value == toInt() } ?: AlarmState.NORMAL

fun Number.toExpressedState(): ExpressedState =
    ExpressedState.entries.firstOrNull { it.value == toInt() } ?: ExpressedState.NORMAL

fun ExpressedState.toLabel(): String = when (this) {
    ExpressedState.NORMAL -> "Normal"
    ExpressedState.SMOKE_ALARM -> "Smoke alarm"
    ExpressedState.CO_ALARM -> "CO alarm"
    ExpressedState.BATTERY_ALERT -> "Battery alert"
    ExpressedState.TESTING -> "Testing"
    ExpressedState.HARDWARE_FAULT -> "Hardware fault"
    ExpressedState.END_OF_SERVICE -> "End of service"
    ExpressedState.INTERCONNECT_SMOKE -> "Smoke alarm (interconnected)"
    ExpressedState.INTERCONNECT_CO -> "CO alarm (interconnected)"
}

fun ExpressedState.toIcon(): ImageVector = when (this) {
    ExpressedState.NORMAL -> Icons.Filled.CheckCircle
    ExpressedState.SMOKE_ALARM -> Icons.Filled.LocalFireDepartment
    ExpressedState.CO_ALARM -> Icons.Filled.Co2
    ExpressedState.BATTERY_ALERT -> Icons.Filled.BatteryAlert
    ExpressedState.TESTING -> Icons.Filled.Science
    ExpressedState.HARDWARE_FAULT -> Icons.Filled.ErrorOutline
    ExpressedState.END_OF_SERVICE -> Icons.Filled.EventBusy
    ExpressedState.INTERCONNECT_SMOKE,
    ExpressedState.INTERCONNECT_CO -> Icons.Filled.Hub
}

@Composable
fun ExpressedState.toColor(): Color = when (this) {
    ExpressedState.NORMAL -> MaterialTheme.colorScheme.primary
    ExpressedState.TESTING -> MaterialTheme.colorScheme.tertiary
    ExpressedState.BATTERY_ALERT,
    ExpressedState.HARDWARE_FAULT,
    ExpressedState.END_OF_SERVICE -> NordicFall
    ExpressedState.SMOKE_ALARM,
    ExpressedState.CO_ALARM,
    ExpressedState.INTERCONNECT_SMOKE,
    ExpressedState.INTERCONNECT_CO -> NordicRed
}
