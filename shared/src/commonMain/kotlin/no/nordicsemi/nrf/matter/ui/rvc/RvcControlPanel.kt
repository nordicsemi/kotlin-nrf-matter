package no.nordicsemi.nrf.matter.ui.rvc

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import no.nordicsemi.nrf.matter.cluster.ModeOption
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
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        operationalState?.let { OperationalStateSection(it) }
        runMode?.let { ModeSection(title = "Run mode", controller = it) }
        cleanMode?.let { ModeSection(title = "Clean mode", controller = it) }
        serviceArea?.let { ServiceAreaSection(it) }
    }
}

private enum class RvcAction(val label: String) {
    PAUSE("Pause"),
    RESUME("Resume"),
    GO_HOME("Go home"),
}

@Composable
private fun OperationalStateSection(controller: RvcOperationalStateController) {
    val state by controller.state.collectAsStateWithLifecycle()
    val data = (state as? UiState.Success)?.data

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Picker(
            label = "Status",
            selectedLabel = data?.state.toLabel(),
            options = RvcAction.entries,
            optionLabel = { it.label },
            onOptionSelected = { action ->
                when (action) {
                    RvcAction.PAUSE -> controller.pause()
                    RvcAction.RESUME -> controller.resume()
                    RvcAction.GO_HOME -> controller.goHome()
                }
            },
        )

        val details = listOfNotNull(
            data?.currentPhase?.let { "Phase $it" },
            data?.countdownTimeSeconds?.let { "${it}s left" },
        ).joinToString(" · ")

        if (details.isNotEmpty()) {
            Text(
                text = details,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.alpha(0.5f),
            )
        }
    }
}

@Composable
private fun ModeSection(title: String, controller: ModeController) {
    val state by controller.state.collectAsStateWithLifecycle()
    val data = (state as? UiState.Success)?.data ?: return

    val selectedLabel = data.supportedModes.firstOrNull { it.mode == data.currentMode }?.label ?: "Unknown"

    Picker(
        label = title,
        selectedLabel = selectedLabel,
        options = data.supportedModes,
        optionLabel = ModeOption::label,
        onOptionSelected = { option -> controller.changeToMode(option.mode) },
    )
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
            modifier = Modifier.alpha(0.5f),
        )

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            data.supportedAreas.forEach { area ->
                val label = (area.name ?: "Area ${area.areaId}") +
                        if (area.areaId == data.currentAreaId) " •" else ""

                FilterChip(
                    selected = area.areaId in selected,
                    onClick = {
                        selected =
                            if (area.areaId in selected) selected - area.areaId else selected + area.areaId
                    },
                    label = { Text(label) },
                )
            }
        }

        TextButton(onClick = { controller.selectAreas(selected.toList()) }) {
            Text("Clean selected areas")
        }
    }
}
