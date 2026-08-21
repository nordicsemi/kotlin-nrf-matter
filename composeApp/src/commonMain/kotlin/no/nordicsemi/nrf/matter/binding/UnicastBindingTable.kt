package no.nordicsemi.nrf.matter.binding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapCalls
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import no.nordicsemi.nrf.matter.model.DeviceId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UnicastBindingTable(
    uiState: BindingUiState,
    onSourceSelected: (DeviceId) -> Unit,
    onTargetSelected: (DeviceId) -> Unit,
    onInitiateBinding: (DeviceId, DeviceId) -> Unit,
) {
    var isSourceDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var isTargetDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    val sourceText = uiState.sourceDevices
        .firstOrNull { it.deviceId == uiState.selectedSourceDeviceId }
        ?.let { it.productName ?: "Node ${it.deviceId.longValue}" }
        ?: "Select Light Switch"

    val targetText = uiState.eligibleTargetDevices
        .firstOrNull { it.deviceId == uiState.selectedTargetDeviceId }
        ?.let { it.productName ?: "Node ${it.deviceId.longValue}" }
        ?: "Select Light Bulb"

    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Source Dropdown
            Column {
                Text(
                    text = "Select Client / Source Node (Write Client)",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
                ExposedDropdownMenuBox(
                    expanded = isSourceDropdownExpanded,
                    onExpandedChange = { isSourceDropdownExpanded = !isSourceDropdownExpanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = sourceText,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSourceDropdownExpanded)
                        },
                        shape = RoundedCornerShape(8.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = isSourceDropdownExpanded,
                        onDismissRequest = { isSourceDropdownExpanded = false }
                    ) {
                        uiState.sourceDevices.forEach { device ->
                            DropdownMenuItem(
                                text = {
                                    Text("${device.productName} (Node ID: ${device.deviceId.longValue})")
                                },
                                onClick = {
                                    isSourceDropdownExpanded = false
                                    onSourceSelected(device.deviceId)
                                }
                            )
                        }
                    }
                }
            }

            if (uiState.selectedSourceDeviceId == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Please select a source device to see eligible target devices.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
                return@Column
            } else if (uiState.eligibleTargetDevices.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(
                        text = "No eligible target devices found for the selected source. Please ensure you have a compatible Light or Dimmable light device added.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
                return@Column
            } else {
                // Target Dropdown
                Column {
                    Text(
                        text = "Select Server / Target Node (Control Target)",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                    ExposedDropdownMenuBox(
                        expanded = isTargetDropdownExpanded,
                        onExpandedChange = { isTargetDropdownExpanded = !isTargetDropdownExpanded },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        OutlinedTextField(
                            value = targetText,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTargetDropdownExpanded)
                            },
                            shape = RoundedCornerShape(8.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = isTargetDropdownExpanded,
                            onDismissRequest = { isTargetDropdownExpanded = false }
                        ) {
                            uiState.eligibleTargetDevices.forEach { device ->
                                DropdownMenuItem(
                                    text = {
                                        Text("${device.productName} (Node ID: ${device.deviceId.longValue})")
                                    },
                                    onClick = {
                                        isTargetDropdownExpanded = false
                                        onTargetSelected(device.deviceId)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Target Cluster Info Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.SwapCalls,
                    contentDescription = "Target Cluster",
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Target Action: Write Cluster 0x0006 (OnOff Bind Struct)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Execute Button
            val canSubmit = uiState.selectedTargetDeviceId != null

            Button(
                onClick = {
                    val source = uiState.selectedSourceDeviceId
                    val target = uiState.selectedTargetDeviceId
                    if (target != null) {
                        onInitiateBinding(source, target)
                    }
                },
                enabled = canSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Write Binding")
            }
        }
    }
}
