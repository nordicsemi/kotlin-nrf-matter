package no.nordicsemi.nrf.matter.ui.rvc

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

@Composable
private fun OperationalStateSection(controller: RvcOperationalStateController) {
    val state by controller.state.collectAsStateWithLifecycle()
    val data = (state as? UiState.Success)?.data

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(data?.state.toLabel(), style = MaterialTheme.typography.titleMedium)
            data?.currentPhase?.let { Text("Phase $it", style = MaterialTheme.typography.titleMedium) }
            data?.countdownTimeSeconds?.let {
                Text("${it}s left", style = MaterialTheme.typography.titleMedium)
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = controller::pause) { Text("Pause") }
            TextButton(onClick = controller::resume) { Text("Resume") }
            TextButton(onClick = controller::goHome) { Text("Go home") }
        }
    }
}

@Composable
private fun ModeSection(title: String, controller: ModeController) {
    val state by controller.state.collectAsStateWithLifecycle()
    val data = (state as? UiState.Success)?.data ?: return

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.alpha(0.5f),
        )

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            data.supportedModes.forEach { option ->
                FilterChip(
                    selected = option.mode == data.currentMode,
                    onClick = { controller.changeToMode(option.mode) },
                    label = { Text(option.label) },
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
