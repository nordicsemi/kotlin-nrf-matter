package no.nordicsemi.nrf.matter.ui.rvc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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

private fun Int.toDurationLabel(): String {
    val minutes = this / 60
    val seconds = this % 60
    return when {
        minutes > 0 && seconds > 0 -> "${minutes}m ${seconds}s left"
        minutes > 0 -> "${minutes}m left"
        else -> "${seconds}s left"
    }
}
