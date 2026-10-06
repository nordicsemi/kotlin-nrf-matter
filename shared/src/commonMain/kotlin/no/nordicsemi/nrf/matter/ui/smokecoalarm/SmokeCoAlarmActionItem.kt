package no.nordicsemi.nrf.matter.ui.smokecoalarm

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun SmokeCoAlarmActionItem(
    isAlarmActive: Boolean,
) {
    Icon(
        imageVector = if (isAlarmActive) Icons.Outlined.Warning else Icons.Outlined.ExpandMore,
        contentDescription = if (isAlarmActive) "Alarm active" else "No alarm",
        tint = if (isAlarmActive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.size(28.dp).testTag("smoke_co_alarm_icon"),
    )
}