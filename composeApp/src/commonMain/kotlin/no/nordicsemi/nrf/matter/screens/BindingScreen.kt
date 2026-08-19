package no.nordicsemi.nrf.matter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.SwapCalls
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.cloudy.cloudy
import no.nordicsemi.nrf.matter.binding.BindingStateHandler
import no.nordicsemi.nrf.matter.binding.BindingUiState
import no.nordicsemi.nrf.matter.binding.BindingViewModel
import no.nordicsemi.nrf.matter.binding.CastTabRow
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.model.DeviceBinding
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.GroupBinding
import no.nordicsemi.nrf.matter.model.toDeviceId
import no.nordicsemi.nrf.matter.theme.NordicTheme
import no.nordicsemi.nrf.matter.ui.DeviceTest_LIGHT
import org.koin.compose.viewmodel.koinViewModel

/*
 * Copyright (c) 2025, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

@Composable
internal fun BindingsScreen(
) {
    val bindingViewModel: BindingViewModel = koinViewModel()
    val bindingUiState by bindingViewModel.bindingUiState.collectAsStateWithLifecycle()
    val bindingLogs by bindingViewModel.bindingLogs.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = if (bindingUiState.groupBindingSupported) {
        listOf("Unicast", "Group")
    } else {
        listOf("Unicast")
    }

    BindingStateHandler(
        bindingUiState,
        bindingLogs
        ,
        bindingState = if (selectedTab == 0) {
            bindingUiState.bindingState
        } else {
            bindingUiState.groupBindingState
        }
    ) { state ->
        if (selectedTab == 0) {
            bindingViewModel.updateBindingState(state as UiState<no.nordicsemi.nrf.matter.model.DeviceBinding>)
        } else {
            bindingViewModel.updateGroupBindingState(state as UiState<no.nordicsemi.nrf.matter.model.GroupBinding>)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        .then(
            if ((if (selectedTab == 0) bindingUiState.bindingState else bindingUiState.groupBindingState) is UiState.Loading) {
                Modifier.cloudy()
            } else {
                Modifier
            }
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Concept Header
        item {
            OutlinedCard {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cable,
                        contentDescription = "Bindings Explanation",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Understanding Matter Bindings",
                        )
                        Text(
                            text = "The Binding Cluster (0x001E) allows client devices (such as a light switch) to directly control target devices (such as light bulbs) over the Matter fabric. The group tab writes a multicast binding and provisions a shared group key for the devices.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                        BindingLearnMoreText()
                    }
                }
            }
        }
        item {
            CastTabRow(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                tabs = tabs
            )
        }


        // Configuration Section
        item {
            Text(
                text = if (selectedTab == 0) {
                    "Write Matter Binding Cluster (0x001E)"
                } else {
                    "Write Matter Group Binding Cluster (0x001E)"
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            if (bindingUiState.sourceDevices.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(
                        text = "No compatible source devices found. Add a supported source device (for example, a light switch) to configure bindings.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
                return@item
            } else {
                BindingTableDetails(
                    bindingScreenState = bindingUiState,
                    bindingActionText = if (selectedTab == 0) {
                        "Write Binding"
                    } else {
                        "Create Group Binding"
                    },
                    targetActionText = if (selectedTab == 0) {
                        "Target Action: Write Cluster 0x0006 (OnOff Bind Struct)"
                    } else {
                        "Target Action: Install Group Key, add group membership, then write a multicast binding"
                    },
                    isGroupTab = selectedTab == 1,
                    onSourceSelected = {
                        bindingViewModel.onSourceSelected(it)
                    },
                    onTargetSelected = { targetId ->
                        bindingViewModel.onTargetSelected(targetId)
                    },
                    onGroupSelected = { groupId ->
                        bindingViewModel.onGroupSelected(groupId)
                    },
                    initiateBinding = { sourceId, targetId ->
                        bindingViewModel.initiateBinding(sourceId, targetId)
                    },
                    initiateGroupBinding = { sourceId, targetId, groupId, groupName ->
                        bindingViewModel.initiateGroupBinding(sourceId, targetId, groupId, groupName)
                    }
                )
            }
        }

        // Active Binding Lists
        item {
            Text(
                text = if (selectedTab == 0) {
                    "Active Binding Table Entries (${bindingUiState.activeBindings.size})"
                } else {
                    "Active Group Binding Table Entries (${bindingUiState.activeGroupBindings.size})"
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        val activeItems = if (selectedTab == 0) {
            bindingUiState.activeBindings.map { ActiveBindingEntry.Unicast(it) }
        } else {
            bindingUiState.activeGroupBindings.map { ActiveBindingEntry.Group(it) }
        }

        if (activeItems.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(
                        text = "No active bindings configured.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }
        } else {
            // List active bindings
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    activeItems.forEach { binding ->
                        when (binding) {
                            is ActiveBindingEntry.Unicast -> BindingCardRow(binding = binding.binding)
                            is ActiveBindingEntry.Group -> GroupBindingCardRow(binding = binding.binding)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BindingTableDetails(
    bindingScreenState: BindingUiState,
    bindingActionText: String,
    targetActionText: String,
    isGroupTab: Boolean = false,
    onSourceSelected: (sourceDeviceId: DeviceId) -> Unit,
    onTargetSelected: (targetDeviceId: DeviceId) -> Unit,
    onGroupSelected: (groupId: Int?) -> Unit = {},
    initiateBinding: (sourceDeviceId: DeviceId, targetDeviceId: DeviceId) -> Unit,
    initiateGroupBinding: (sourceDeviceId: DeviceId, targetDeviceId: DeviceId, groupId: Int?, groupName: String?) -> Unit = { _, _, _, _ -> },
) {
    var isSourceDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var isTargetDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var isGroupDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    var showGroupNamePopup by rememberSaveable { mutableStateOf(false) }
    var groupNameInput by rememberSaveable { mutableStateOf("") }

    // Derive displayed text from current UiState
    val sourceText = bindingScreenState.sourceDevices
        .firstOrNull { it.deviceId == bindingScreenState.selectedSourceDeviceId }
        ?.let { it.productName ?: "Node ${it.deviceId.longValue}" }
        ?: "Select Light Switch"

    val targetText = bindingScreenState.eligibleTargetDevices
        .firstOrNull { it.deviceId == bindingScreenState.selectedTargetDeviceId }
        ?.let { it.productName ?: "Node ${it.deviceId.longValue}" }
        ?: "Select Light Bulb"

    val groupText = if (bindingScreenState.selectedGroupId == null) {
        "Add New Group"
    } else {
        bindingScreenState.availableGroups.firstOrNull { it.groupId == bindingScreenState.selectedGroupId }?.groupName ?: "Group ${bindingScreenState.selectedGroupId}"
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
                        bindingScreenState.sourceDevices.forEach { device ->
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

            if (bindingScreenState.selectedSourceDeviceId == null) {
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
            } else if (bindingScreenState.eligibleTargetDevices.isEmpty()) {
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
                            bindingScreenState.eligibleTargetDevices.forEach { device ->
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

            if (isGroupTab && bindingScreenState.selectedTargetDeviceId != null) {
                // Group Dropdown
                Column {
                    Text(
                        text = "Select Group",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                    ExposedDropdownMenuBox(
                        expanded = isGroupDropdownExpanded,
                        onExpandedChange = { isGroupDropdownExpanded = !isGroupDropdownExpanded },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
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
                            DropdownMenuItem(
                                text = { Text("Add New Group") },
                                onClick = {
                                    isGroupDropdownExpanded = false
                                    onGroupSelected(null)
                                }
                            )
                            bindingScreenState.availableGroups.forEach { group ->
                                DropdownMenuItem(
                                    text = { Text(group.groupName) },
                                    onClick = {
                                        isGroupDropdownExpanded = false
                                        onGroupSelected(group.groupId)
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
                    text = targetActionText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Execute Button
            val canSubmit = bindingScreenState.selectedTargetDeviceId != null

            Button(
                onClick = {
                    val source = bindingScreenState.selectedSourceDeviceId
                    val target = bindingScreenState.selectedTargetDeviceId
                    if (source != null && target != null) {
                        if (isGroupTab) {
                            if (bindingScreenState.selectedGroupId == null) {
                                showGroupNamePopup = true
                            } else {
                                val group = bindingScreenState.availableGroups.first { it.groupId == bindingScreenState.selectedGroupId }
                                initiateGroupBinding(source, target, group.groupId, group.groupName)
                            }
                        } else {
                            initiateBinding(source, target)
                        }
                    }
                },
                enabled = canSubmit,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(bindingActionText)
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
                        val source = bindingScreenState.selectedSourceDeviceId
                        val target = bindingScreenState.selectedTargetDeviceId
                        if (source != null && target != null && groupNameInput.isNotBlank()) {
                            initiateGroupBinding(source, target, null, groupNameInput)
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

@Preview(showBackground = true)
@Composable
private fun BindingTableDetailsPreview() {
    NordicTheme {
        BindingTableDetails(
            bindingScreenState = BindingUiState(
                sourceDevices = listOf(
                    DeviceTest_LIGHT
                ),
                eligibleTargetDevices = listOf(
                    DeviceTest_LIGHT
                )
            ),
            bindingActionText = "Write Binding",
            targetActionText = "Target Action: Write Cluster 0x0006 (OnOff Bind Struct)",
            onSourceSelected = {},
            onTargetSelected = {},
            initiateBinding = { _, _ -> }
        )

    }
}

@Composable
fun BindingCardRow(
    binding: DeviceBinding
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tag,
                        contentDescription = "Index",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Binding ID: ${binding.id}",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Client ID: ${binding.sourceNodeId.longValue}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingFlat,
                        contentDescription = "links to",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = "Server ID: ${binding.targetNodeId.longValue}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "Bound Cluster ID: 0x00${binding.clusterId}L (OnOff Cluster)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private const val LEARN_MORE_URL =
    "https://nrfconnectdocs.nordicsemi.com/ncs/latest/nrf/samples/matter/light_switch/README.html"

@Composable
private fun ColumnScope.BindingLearnMoreText() {
    val helpText = buildAnnotatedString {
        append("For more information, click ")
        withLink(
            link = LinkAnnotation.Url(
                url = LEARN_MORE_URL,
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                )
            )
        ) {
            append("here")
        }
        append(".")
    }

    Text(
        text = helpText,
        modifier = Modifier.align(Alignment.Start),
        style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun BindingCardRowPreview() {
    BindingCardRow(
        binding = DeviceBindingTest
    )
}


internal val DeviceBindingTest =
    DeviceBinding(
        sourceNodeId = DeviceId.Zero,
        sourceEndpoint = 1,
        targetNodeId = 2L.toDeviceId(),
        targetEndpoint = 2,
        clusterId = 6L,
        id = "123",
    )

@Preview(showBackground = true)
@Composable
private fun GroupBindingCardRowPreview() {
    GroupBindingCardRow(
        binding = GroupBinding(
            id = "group-1",
            sourceNodeId = DeviceId.Zero,
            sourceEndpoint = 1,
            targetNodeId = 2L.toDeviceId(),
            targetEndpoint = 1,
            clusterId = 6L,
            groupId = 42,
            groupName = "Group 42",
            keySetId = 7,
        )
    )
}

private sealed interface ActiveBindingEntry {
    data class Unicast(val binding: DeviceBinding) : ActiveBindingEntry
    data class Group(val binding: GroupBinding) : ActiveBindingEntry
}

@Composable
fun GroupBindingCardRow(
    binding: GroupBinding,
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tag,
                        contentDescription = "Index",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Group binding ID: ${binding.id}",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Text(
                    text = "Source Node ID: ${binding.sourceNodeId.longValue}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    text = "Group ID: ${binding.groupId} (${binding.groupName})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Cluster ID: 0x00${binding.clusterId}L",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
