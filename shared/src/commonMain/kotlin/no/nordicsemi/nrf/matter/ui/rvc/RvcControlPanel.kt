package no.nordicsemi.nrf.matter.ui.rvc

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import no.nordicsemi.nrf.matter.cluster.ModeOption
import no.nordicsemi.nrf.matter.model.RvcOperationalState
import no.nordicsemi.nrf.matter.ui.UiState

@Composable
internal fun RvcControlPanel(
    operationalState: RvcOperationalStateController?,
    runMode: RvcRunModeController?,
    cleanMode: RvcCleanModeController?,
    serviceArea: ServiceAreaController?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        operationalState?.let { OperationalStateSection(it) }
        runMode?.let { RunModeSection(it) }
        cleanMode?.let { CleanModeSection(it) }
        serviceArea?.let { ServiceAreaSection(it) }
    }
}

@Composable
private fun OperationalStateSection(controller: RvcOperationalStateController) {
    val state by controller.state.collectAsStateWithLifecycle()
    val data = (state as? UiState.Success)?.data
    val vacuumState = data?.state
    val tint = vacuumState.toColor()
    val isRunning = vacuumState == RvcOperationalState.RUNNING
    val isPaused = vacuumState == RvcOperationalState.PAUSED

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                imageVector = vacuumState.toIcon(),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = vacuumState.toLabel(),
                style = MaterialTheme.typography.titleMedium,
                color = tint,
                modifier = Modifier.weight(1f),
            )

            val details = listOfNotNull(
                data?.currentPhase?.let { "Phase $it" },
                data?.countdownTimeSeconds?.let { it.toDurationLabel() },
            )
            if (details.isNotEmpty()) {
                Text(
                    text = details.joinToString(", "),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.alpha(0.6f),
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledTonalButton(
                onClick = { if (isPaused) controller.resume() else controller.pause() },
                enabled = isRunning || isPaused,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = tint.copy(alpha = 0.15f),
                    contentColor = tint,
                ),
            ) {
                Icon(
                    imageVector = if (isPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(6.dp))
                Text(if (isPaused) "Resume" else "Pause")
            }

            OutlinedButton(onClick = controller::goHome) {
                Icon(Icons.Filled.Home, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Go home")
            }
        }
    }
}

@Composable
private fun RunModeSection(controller: RvcRunModeController) {
    val state by controller.state.collectAsStateWithLifecycle()
    val data = (state as? UiState.Success)?.data ?: return

    ModePicker(
        label = "Run mode",
        supportedModes = data.supportedModes,
        currentMode = data.currentMode,
        onSelect = controller::changeToMode,
    )
}

@Composable
private fun CleanModeSection(controller: RvcCleanModeController) {
    val state by controller.state.collectAsStateWithLifecycle()
    val data = (state as? UiState.Success)?.data ?: return

    ModePicker(
        label = "Clean mode",
        supportedModes = data.supportedModes,
        currentMode = data.currentMode,
        onSelect = controller::changeToMode,
    )
}

// A segmented row of many options gets squeezed into unreadable slivers (each segment shares the
// row width equally), so only use it for a handful of options; beyond that, a dropdown scales to
// any count instead of wrapping or truncating.
private const val MAX_SEGMENTED_OPTIONS = 5

@Composable
private fun ModePicker(
    label: String,
    supportedModes: List<ModeOption>,
    currentMode: Int,
    onSelect: (Int) -> Unit,
) {
    if (supportedModes.isEmpty()) return

    if (supportedModes.size <= MAX_SEGMENTED_OPTIONS) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.alpha(0.6f),
            )

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                supportedModes.forEachIndexed { index, option ->
                    SegmentedButton(
                        selected = option.mode == currentMode,
                        onClick = { onSelect(option.mode) },
                        shape = SegmentedButtonDefaults.itemShape(index, supportedModes.size),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            activeContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        label = { Text(option.label) },
                    )
                }
            }
        }
    } else {
        val selectedLabel = supportedModes.firstOrNull { it.mode == currentMode }?.label ?: "Unknown"

        Picker(
            label = label,
            selectedLabel = selectedLabel,
            options = supportedModes,
            optionLabel = ModeOption::label,
            onOptionSelected = { option -> onSelect(option.mode) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> Picker(
    label: String,
    selectedLabel: String,
    options: List<T>,
    optionLabel: (T) -> String,
    onOptionSelected: (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun ServiceAreaSection(controller: ServiceAreaController) {
    val state by controller.state.collectAsStateWithLifecycle()
    val data = (state as? UiState.Success)?.data ?: return

    if (data.supportedAreas.isEmpty()) return

    var selected by remember(data.selectedAreaIds) { mutableStateOf(data.selectedAreaIds.toSet()) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Areas",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.alpha(0.6f),
        )

        // The one boxed section in this panel: areas is the block with the most content and the
        // only one with its own call to action, so it earns a distinct container the compact
        // mode row above doesn't need.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                data.supportedAreas.forEach { area ->
                    val isCurrent = area.areaId == data.currentAreaId

                    FilterChip(
                        selected = area.areaId in selected,
                        onClick = {
                            selected = if (area.areaId in selected) {
                                selected - area.areaId
                            } else {
                                selected + area.areaId
                            }
                        },
                        label = { Text(area.name ?: "Area ${area.areaId}") },
                        leadingIcon = if (isCurrent) {
                            { PulsingDot(MaterialTheme.colorScheme.primary) }
                        } else null,
                        colors = tonalFilterChipColors(),
                    )
                }
            }

            FilledTonalButton(
                onClick = { controller.selectAreas(selected.toList()) },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Clean selected areas")
            }
        }
    }
}

// The app theme's default selected-chip colors come from secondaryContainer, a strong solid blue
// with white text — fine for a single chip, but this panel has many, and a wall of solid blocks
// is what actually read as messy. A soft tint reads as "selected" without shouting.
@Composable
private fun tonalFilterChipColors() = FilterChipDefaults.filterChipColors(
    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
    selectedLabelColor = MaterialTheme.colorScheme.primary,
    selectedLeadingIconColor = MaterialTheme.colorScheme.primary,
)

// A quiet "this is happening right now" signal on the area the vacuum is currently in, distinct
// from the bolder spin on the device icon so the panel doesn't compete with itself for attention.
@Composable
private fun PulsingDot(color: Color) {
    val alpha by rememberInfiniteTransition(label = "area-pulse")
        .animateFloat(
            initialValue = 1f,
            targetValue = 0.25f,
            animationSpec = infiniteRepeatable(tween(700, easing = LinearEasing), RepeatMode.Reverse),
            label = "pulse-alpha",
        )

    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = alpha))
    )
}

private fun Int.toDurationLabel(): String {
    val minutes = this / 60
    val seconds = this % 60
    return when {
        minutes > 0 && seconds > 0 -> "${minutes}m ${seconds}s left"
        minutes > 0 -> "${minutes}m left"
        else -> "${seconds}s left"
    }
}
