package no.nordicsemi.nrf.matter.ui.thermostat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.nordicsemi.nrf.matter.cluster.ThermostatClusterInfo.Feature
import no.nordicsemi.nrf.matter.model.ThermostatSystemMode
import kotlin.math.roundToInt

private val HeatColor = Color(0xFFF87171)
private val CoolColor = Color(0xFF38BDF8)

@Composable
fun ThermostatControlItem(
    state: ThermostatState,
    onSystemModeChange: (ThermostatSystemMode) -> Unit,
    onHeatingSetpointChange: (Float) -> Unit,
    onCoolingSetpointChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Local temperature",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            state.localTemperatureCelsius?.let {
                Text(
                    text = "${it.format()}°C",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.testTag("thermostat_local_temperature"),
                )
            }
        }

        state.outdoorTemperatureCelsius?.let { outdoor ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Outdoor temperature",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "${outdoor.format()}°C",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.testTag("thermostat_outdoor_temperature"),
                )
            }
        }

        SystemModeSelector(state, onSystemModeChange)

        if (state.supportsHeating && state.heatingSetpointCelsius != null) {
            SetpointStepper(
                label = "Heating setpoint",
                accent = HeatColor,
                accentEnd = Color(0xFFF97316),
                tag = "heating_setpoint",
                value = state.heatingSetpointCelsius,
                enabled = state.isEnabled,
                onChange = onHeatingSetpointChange,
            )
        }
        if (state.supportsCooling && state.coolingSetpointCelsius != null) {
            SetpointStepper(
                label = "Cooling setpoint",
                accent = CoolColor,
                accentEnd = Color(0xFF22D3EE),
                tag = "cooling_setpoint",
                value = state.coolingSetpointCelsius,
                enabled = state.isEnabled,
                onChange = onCoolingSetpointChange,
            )
        }

        SupportedFeatures(state)
    }
}

@Composable
private fun SystemModeSelector(state: ThermostatState, onChange: (ThermostatSystemMode) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "System mode",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            state.selectableModes.forEach { mode ->
                ModePill(
                    mode = mode,
                    selected = mode == state.systemMode,
                    enabled = state.isEnabled,
                    onClick = { onChange(mode) },
                )
            }
        }
    }
}

@Composable
private fun ModePill(mode: ThermostatSystemMode, selected: Boolean, enabled: Boolean, onClick: () -> Unit) {
    val accent = mode.accent()
    val primary = MaterialTheme.colorScheme.primary
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        color = if (selected) primary else accent.copy(alpha = 0.12f),
        contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else accent,
        border = BorderStroke(1.dp, if (selected) primary else accent.copy(alpha = 0.4f)),
        modifier = Modifier.testTag("system_mode_${mode.name}"),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = if (selected) Icons.Filled.Check else mode.icon(),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = mode.label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                maxLines = 1,
            )
        }
    }
}

private fun ThermostatSystemMode.icon(): ImageVector = when (this) {
    ThermostatSystemMode.OFF -> Icons.Filled.PowerSettingsNew
    ThermostatSystemMode.AUTO -> Icons.Filled.AutoMode
    ThermostatSystemMode.COOL -> Icons.Filled.AcUnit
    ThermostatSystemMode.HEAT -> Icons.Filled.LocalFireDepartment
    ThermostatSystemMode.EMERGENCY_HEAT -> Icons.Filled.WarningAmber
    ThermostatSystemMode.PRECOOLING -> Icons.Filled.Schedule
    ThermostatSystemMode.FAN_ONLY -> Icons.Filled.Air
    ThermostatSystemMode.DRY -> Icons.Filled.WaterDrop
    ThermostatSystemMode.SLEEP -> Icons.Filled.Bedtime
    ThermostatSystemMode.OTHER -> Icons.Filled.MoreHoriz
}

private fun ThermostatSystemMode.accent(): Color = when (this) {
    ThermostatSystemMode.OFF, ThermostatSystemMode.OTHER -> Color(0xFF94A3B8)
    ThermostatSystemMode.AUTO -> Color(0xFF22D3EE)
    ThermostatSystemMode.COOL -> CoolColor
    ThermostatSystemMode.HEAT -> HeatColor
    ThermostatSystemMode.EMERGENCY_HEAT -> Color(0xFFEF4444)
    ThermostatSystemMode.PRECOOLING -> Color(0xFF67E8F9)
    ThermostatSystemMode.FAN_ONLY -> Color(0xFF34D399)
    ThermostatSystemMode.DRY -> Color(0xFFA78BFA)
    ThermostatSystemMode.SLEEP -> Color(0xFF818CF8)
}

@Composable
private fun SetpointStepper(
    label: String,
    tag: String,
    value: Float,
    enabled: Boolean,
    accent: Color,
    accentEnd: Color,
    onChange: (Float) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .width(6.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Brush.verticalGradient(listOf(accent, accentEnd))),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = accent,
            )
            Text(
                text = "${value.format()}°C",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag(tag),
            )
        }
        Row(
            modifier = Modifier
                .height(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(accent.copy(alpha = if (enabled) 0.16f else 0.06f)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onChange(-ThermostatController.SETPOINT_STEP_CELSIUS) },
                enabled = enabled,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = accent,
                    disabledContentColor = accent.copy(alpha = 0.38f),
                ),
                modifier = Modifier.size(width = 48.dp, height = 40.dp).testTag("${tag}_decrease"),
            ) { Icon(Icons.Filled.Remove, contentDescription = "Decrease $label") }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(20.dp)
                    .background(accent.copy(alpha = 0.4f)),
            )
            IconButton(
                onClick = { onChange(ThermostatController.SETPOINT_STEP_CELSIUS) },
                enabled = enabled,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = accent,
                    disabledContentColor = accent.copy(alpha = 0.38f),
                ),
                modifier = Modifier.size(width = 48.dp, height = 40.dp).testTag("${tag}_increase"),
            ) { Icon(Icons.Filled.Add, contentDescription = "Increase $label") }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SupportedFeatures(state: ThermostatState) {
    val features = listOf(
        Feature.HEATING to "Heating",
        Feature.COOLING to "Cooling",
        Feature.OCCUPANCY to "Occupancy",
        Feature.SCHEDULE_CONFIGURATION to "Schedule",
        Feature.SETBACK to "Setback",
        Feature.AUTO_MODE to "Auto",
        Feature.LOCAL_TEMPERATURE_NOT_EXPOSED to "Local temperature hidden",
    ).filter { state.supports(it.first) }.map { it.second }

    if (features.isEmpty()) return

    Column(
        modifier = Modifier.testTag("supported_features"),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "Supported features",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            features.forEach { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }
        }
    }
}

private fun Float.format(): String = ((this * 10).roundToInt() / 10f).toString()
