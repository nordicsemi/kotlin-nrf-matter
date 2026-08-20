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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SwapCalls
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GroupBindingTable(
    sourceDevices: List<Device>,
    selectedSourceDeviceId: DeviceId?,
    eligibleTargetDevices: List<Device>,
    selectedTargetDeviceId: DeviceId?,
    availableGroups: List<GroupInfo>,
    selectedGroupId: Int?,
    newGroupName: String?,
    onSourceSelected: (sourceDeviceId: DeviceId) -> Unit,
    onTargetSelected: (targetDeviceId: DeviceId) -> Unit,
    onGroupSelected: (groupId: Int?) -> Unit,
    onGroupNameSet: (groupName: String?) -> Unit,
    initiateGroupBinding: (sourceDeviceId: DeviceId, targetDeviceId: DeviceId, groupId: Int?, groupName: String?) -> Unit,
) {
    var isSourceDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var isTargetDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var isGroupDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    var showGroupNamePopup by rememberSaveable { mutableStateOf(false) }
    var groupNameInput by rememberSaveable { mutableStateOf("") }

    val sourceText = sourceDevices
        .firstOrNull { it.deviceId == selectedSourceDeviceId }
        ?.let { it.productName ?: "Node ${it.deviceId.longValue}" }
        ?: "Select Light Switch"

    val targetText = eligibleTargetDevices
        .firstOrNull { it.deviceId == selectedTargetDeviceId }
        ?.let { it.productName ?: "Node ${it.deviceId.longValue}" }
        ?: "Select Light Bulb"

    val groupText = when {
        selectedGroupId != null -> {
            availableGroups.firstOrNull { it.groupId == selectedGroupId }?.groupName
                ?: "Group $selectedGroupId"
        }

        newGroupName != null -> {
            newGroupName
        }

        else -> "Select a Group"
    }

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
                        sourceDevices.forEach { device ->
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

            if (selectedSourceDeviceId == null) {
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
            } else if (eligibleTargetDevices.isEmpty()) {
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
                            eligibleTargetDevices.forEach { device ->
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

            if (selectedTargetDeviceId != null) {
                // Group Configuration
                Column {
                    Text(
                        text = "Group Configuration",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (availableGroups.isNotEmpty() || newGroupName != null) {
                            ExposedDropdownMenuBox(
                                expanded = isGroupDropdownExpanded,
                                onExpandedChange = {
                                    isGroupDropdownExpanded = !isGroupDropdownExpanded
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = groupText,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier
                                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                        .fillMaxWidth(),
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGroupDropdownExpanded)
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                )

                                ExposedDropdownMenu(
                                    expanded = isGroupDropdownExpanded,
                                    onDismissRequest = { isGroupDropdownExpanded = false }
                                ) {
                                    availableGroups.forEach { group ->
                                        DropdownMenuItem(
                                            text = { Text(group.groupName) },
                                            onClick = {
                                                isGroupDropdownExpanded = false
                                                onGroupSelected(group.groupId)
                                            }
                                        )
                                    }
                                    if (newGroupName != null) {
                                        DropdownMenuItem(
                                            text = { Text(newGroupName) },
                                            onClick = {
                                                isGroupDropdownExpanded = false
                                                onGroupNameSet(newGroupName)
                                            }
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "No existing groups. Click + to create one.",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = { showGroupNamePopup = true },
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add New Group",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
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
                    text = "Target Action: Install Group Key, add group membership, then write a multicast binding",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Execute Button
            val canSubmit = selectedTargetDeviceId != null &&
                        (selectedGroupId != null || newGroupName != null)

            Button(
                onClick = {
                    if (selectedTargetDeviceId != null) {
                        if (selectedGroupId != null) {
                            val group = availableGroups.first { it.groupId == selectedGroupId }
                            initiateGroupBinding(selectedSourceDeviceId, selectedTargetDeviceId, group.groupId, group.groupName)
                        } else if (newGroupName != null) {
                            initiateGroupBinding(selectedSourceDeviceId, selectedTargetDeviceId, null, newGroupName)
                        }
                    }
                },
                enabled = canSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Create Group Binding")
            }
        }
    }

    if (showGroupNamePopup) {
        AlertDialog(
            onDismissRequest = { showGroupNamePopup = false },
            title = { Text("Name your Group") },
            text = {
                OutlinedTextField(
                    value = groupNameInput,
                    onValueChange = { groupNameInput = it },
                    label = { Text("Group Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (groupNameInput.isNotBlank()) {
                            onGroupNameSet(groupNameInput)
                            showGroupNamePopup = false
                            groupNameInput = ""
                        }
                    },
                    enabled = groupNameInput.isNotBlank()
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showGroupNamePopup = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
