package no.nordicsemi.nrf.matter.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import no.nordicsemi.nrf.matter.binding.BindingViewModel
import no.nordicsemi.nrf.matter.binding.CastTabRow
import no.nordicsemi.nrf.matter.binding.GroupBindingTable
import no.nordicsemi.nrf.matter.binding.GroupBindingViewModel
import no.nordicsemi.nrf.matter.binding.UnicastBindingTable
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.model.DeviceBinding
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.GroupBinding
import no.nordicsemi.nrf.matter.model.toDeviceId
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
    val groupBindingViewModel: GroupBindingViewModel = koinViewModel()

    val bindingUiState by bindingViewModel.bindingUiState.collectAsStateWithLifecycle()
    val groupBindingUiState by groupBindingViewModel.uiState.collectAsStateWithLifecycle()

    val bindingLogs by bindingViewModel.bindingLogs.collectAsStateWithLifecycle()
    val groupBindingLogs by groupBindingViewModel.bindingLogs.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = if (groupBindingUiState.groupBindingSupported) {
        listOf("Unicast", "Group")
    } else {
        listOf("Unicast")
    }

    if (selectedTab == 0) {
        BindingStateHandler(
            bindingLogs = bindingLogs,
            bindingState = bindingUiState.bindingState,
            onUpdateBindingState = { bindingViewModel.updateBindingState(it) }
        )
    } else {
        BindingStateHandler(
            bindingLogs = groupBindingLogs,
            bindingState = groupBindingUiState.groupBindingState,
            onUpdateBindingState = { groupBindingViewModel.updateGroupBindingState(it) }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .then(
                if ((if (selectedTab == 0) bindingUiState.bindingState else groupBindingUiState.groupBindingState) is UiState.Loading) {
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
            val sourceDevices =
                if (selectedTab == 0) bindingUiState.sourceDevices else groupBindingUiState.sourceDevices
            if (sourceDevices.isEmpty()) {
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
            } else {
                if (selectedTab == 0) {
                    UnicastBindingTable(
                        uiState = bindingUiState,
                        onSourceSelected = { bindingViewModel.onSourceSelected(it) },
                        onTargetSelected = { bindingViewModel.onTargetSelected(it) },
                        onInitiateBinding = { sourceId, targetId ->
                            bindingViewModel.initiateBinding(sourceId, targetId)
                        }
                    )
                } else {
                    GroupBindingTable(
                        uiState = groupBindingUiState,
                        onSourceSelected = { groupBindingViewModel.onSourceSelected(it) },
                        onTargetSelected = { groupBindingViewModel.onTargetSelected(it) },
                        onGroupSelected = { groupBindingViewModel.onGroupSelected(it) },
                        onGroupNameSet = { groupBindingViewModel.onGroupNameSet(it) },
                        onInitiateGroupBinding = { sourceId, targetId, groupId, groupName ->
                            groupBindingViewModel.initiateGroupBinding(
                                sourceId,
                                targetId,
                                groupId,
                                groupName
                            )
                        }
                    )
                }
            }
        }

        // Active Binding Lists
        item {
            Text(
                text = if (selectedTab == 0) {
                    "Active Binding Table Entries (${bindingUiState.activeBindings.size})"
                } else {
                    "Active Group Binding Table Entries (${groupBindingUiState.activeGroupBindings.size})"
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        val activeItems = if (selectedTab == 0) {
            bindingUiState.activeBindings.map { ActiveBindingEntry.Unicast(it) }
        } else {
            groupBindingUiState.activeGroupBindings.map { ActiveBindingEntry.Group(it) }
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
