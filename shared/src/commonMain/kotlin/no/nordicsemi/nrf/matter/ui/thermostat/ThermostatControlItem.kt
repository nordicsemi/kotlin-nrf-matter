package no.nordicsemi.nrf.matter.ui.thermostat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.nordicsemi.nrf.matter.cluster.ThermostatClusterInfo.Feature
import no.nordicsemi.nrf.matter.model.ThermostatSystemMode
import kotlin.math.roundToInt

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
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Thermostat",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
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

        SystemModeSelector(state, onSystemModeChange)

        if (state.supportsHeating && state.heatingSetpointCelsius != null) {
            SetpointStepper(
                label = "Heating setpoint",
                accent = MaterialTheme.colorScheme.error,
                onAccent = MaterialTheme.colorScheme.onError,
                tag = "heating_setpoint",
                value = state.heatingSetpointCelsius,
                enabled = state.isEnabled,
                onChange = onHeatingSetpointChange,
            )
        }
        if (state.supportsCooling && state.coolingSetpointCelsius != null) {
            SetpointStepper(
                label = "Cooling setpoint",
                accent = MaterialTheme.colorScheme.primary,
                onAccent = MaterialTheme.colorScheme.onPrimary,
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
    val modes = state.selectableModes

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "System mode",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.alpha(0.6f),
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            modes.forEachIndexed { index, mode ->
                SegmentedButton(
                    selected = mode == state.systemMode,
                    onClick = { onChange(mode) },
                    enabled = state.isEnabled,
                    modifier = Modifier.testTag("system_mode_${mode.name}"),
                    shape = SegmentedButtonDefaults.itemShape(index, modes.size),
                    label = { Text(mode.label) },
                )
            }
        }
    }
}

@Composable
private fun SetpointStepper(
    label: String,
    tag: String,
    value: Float,
    enabled: Boolean,
    accent: Color,
    onAccent: Color,
    onChange: (Float) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(accent),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                .background(accent.copy(alpha = if (enabled) 1f else 0.38f)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onChange(-ThermostatController.SETPOINT_STEP_CELSIUS) },
                enabled = enabled,
                colors = IconButtonDefaults.iconButtonColors(contentColor = onAccent, disabledContentColor = onAccent),
                modifier = Modifier.size(width = 52.dp, height = 40.dp).testTag("${tag}_decrease"),
            ) { Icon(Icons.Filled.Remove, contentDescription = "Decrease $label") }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(20.dp)
                    .background(onAccent.copy(alpha = 0.4f)),
            )
            IconButton(
                onClick = { onChange(ThermostatController.SETPOINT_STEP_CELSIUS) },
                enabled = enabled,
                colors = IconButtonDefaults.iconButtonColors(contentColor = onAccent, disabledContentColor = onAccent),
                modifier = Modifier.size(width = 52.dp, height = 40.dp).testTag("${tag}_increase"),
            ) { Icon(Icons.Filled.Add, contentDescription = "Increase $label") }
        }
    }
}

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

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Supported features",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.alpha(0.6f),
        )
        Text(
            text = features.joinToString(", "),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.testTag("supported_features"),
        )
    }
}

private fun Float.format(): String = ((this * 10).roundToInt() / 10f).toString()
