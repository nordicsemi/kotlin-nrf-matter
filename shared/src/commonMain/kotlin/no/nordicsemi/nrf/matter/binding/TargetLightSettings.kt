package no.nordicsemi.nrf.matter.binding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.nrf.matter.model.Device

@Composable
internal fun TargetLightSettingsDialog(
    targetDevices: List<Device> = emptyList(), // All devices with light on/off capabilities and that are not already bound to the device.
    onDismiss: () -> Unit,
    onConfirmation: (selectedTargets: List<Device>) -> Unit,
) {
    val selectedOptions = remember {
        mutableStateListOf<Device>()
    }
    AlertDialog(
        title = {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Select Target Devices",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "Commissioned Devices",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                Column(Modifier.selectableGroup()) {
                    targetDevices.forEach { device ->
                        OutlinedCard(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = selectedOptions.contains(device),
                                    onCheckedChange = { isChecked ->
                                        if (isChecked) {
                                            selectedOptions.add(device)
                                        } else {
                                            selectedOptions.remove(device)
                                        }
                                    }
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp)
                                ) {
                                    Text(
                                        text = device.name ?: "Unknown",
                                        style = MaterialTheme.typography.bodyLarge,
                                    )
                                    Text(
                                        text = "${device.productName}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }


                            }
                        }
                    }
                }
            }
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmation(selectedOptions.toList())
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
            ) {
                Text("Bind")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant, // looks "muted"
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
            ) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun TargetLightSettingsDialogPreview() {
    TargetLightSettingsDialog(
        onDismiss = {},
        onConfirmation = {}
    )
}
