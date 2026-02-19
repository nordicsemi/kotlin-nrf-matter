package no.nordicsemi.nrf.matter.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceState
import no.nordicsemi.nrf.matter.model.DeviceType
import no.nordicsemi.nrf.matter.model.DeviceUiModel
import no.nordicsemi.nrf.matter.screens.DeviceItemContainer
import nrfmatterformobile.composeapp.generated.resources.Res
import nrfmatterformobile.composeapp.generated.resources.light_bulb_smart_light
import nrfmatterformobile.composeapp.generated.resources.light_fixture
import nrfmatterformobile.composeapp.generated.resources.temperature
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

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

val MatterGreen = Color(0xFF22C55E)

// Specific Device: Dimmable Light
@Preview(showBackground = true)
@Composable
fun DimmableLightItem() {
    DeviceItemContainer(
        icon = painterResource(resource = Res.drawable.light_bulb_smart_light),
        title = "Living Room Lamp",
        subtitle = "Dimmable Light",
        onDeviceClick = {
            /* TODO: Add onClick handler */
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("50%", fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            LinearProgressIndicator(
                progress = 0.5f,
                modifier = Modifier
                    .width(64.dp)
                    .height(6.dp)
                    .clip(CircleShape),
                color = Color(0xFFF59E0B),
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )
        }
    }
}

// Specific Device: Smart Switch
@Composable
fun SwitchDeviceItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onOnOffClick: (deviceId: Long, value: Boolean) -> Unit,
    onDeviceClick: () -> Unit
) {
    DeviceItemContainer(
        icon = painterResource(resource = Res.drawable.light_fixture),// TODO: Change it to the Power icon
        title = title,
        subtitle = subtitle,
        isOnline = checked,
        onDeviceClick = { onDeviceClick() }
    ) {
        Switch(
            checked = checked,
            onCheckedChange = {
                onOnOffClick(1L, it)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SwitchDeviceItemPreview() {
    SwitchDeviceItem(
        title = "Living Room Lamp",
        subtitle = "Smart Switch",
        checked = true,
        onOnOffClick = { _, _ -> },
        onDeviceClick = {},
    )
}

@Preview(showBackground = true)
@Composable
fun FilterChipsRow() {
    val filters = listOf("All", "Living Room", "Kitchen", "Bedroom")
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filters) { filter ->
            val isSelected = filter == "All"
            Surface(
                shape = CircleShape,
                border = if (isSelected) null else BorderStroke(
                    1.dp,
                    Color.LightGray.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = filter,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    color = if (isSelected) Color.White else Color.Unspecified,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
internal fun DeviceList(
    devicesList: List<DeviceUiModel>,
    onDeviceClick: (DeviceUiModel) -> Unit,
    onOnOffClick: (deviceId: Long, value: Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // --- Section: Lights ---
        this.items(devicesList, key = { device -> device.device.deviceId }) { device ->
            SectionTitle("Lights")
            SwitchDeviceItem(
                title = device.device.name ?: "Living Room Lamp",
                subtitle = "Smart Light",
                checked = device.isOn,
                onOnOffClick = { deviceId, value -> onOnOffClick(deviceId, value) },
            ) { onDeviceClick(device) }

        }
        /*
                item { SectionHeader("Lights") }
                item { DimmableLightItem() }
                item {
                    SwitchDeviceItem(
                        title = "Hallway Light",
                        subtitle = "Smart Light",
                        initialState = false
                    )
                }

                        // --- Section: Power & Energy ---
                        item { SectionHeader("Power & Energy") }
                        item {
                            SwitchDeviceItem(
                                title = "Coffee Maker",
                                subtitle = "Smart Plug",
                                initialState = true
                            )
                        }

                        // --- Section: Climate & Security ---
                        item { SectionHeader("Climate & Security") }
                        item { ThermostatItem() }
                        item { LockItem() }

                        // --- Empty State / Add Suggestion ---
                        item { AddDeviceSuggestion() }
                  */
    }
}

// Thermostat Item
@Preview(showBackground = true)
@Composable
fun ThermostatItem() {
    DeviceItemContainer(
        icon = painterResource(resource = Res.drawable.temperature),
        title = "Downstairs AC",
        subtitle = "Target: 70°F", onDeviceClick = {}
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Text(
                "72°",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text("Cooling", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

// Lock Item
@Preview(showBackground = true)
@Composable
fun LockItem() {
    DeviceItemContainer(
        icon = painterResource(Res.drawable.light_bulb_smart_light),// TODO: Change it to the door lock icon.
        title = "Front Door",
        subtitle = "Smart Lock",
        onDeviceClick = {}
    ) {
        Surface(
            color = Color.LightGray.copy(alpha = 0.2f),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "LOCKED",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE11D48)
            )
        }
    }
}

// -----------------------------------------------------------------------------------------------
// Constant objects used in Compose Preview

// DeviceState -- Online and On
private val DeviceState_OnlineOn =
    DeviceState(
        dateCaptured = Clock.System.now(),
        deviceId = 1L,
        on = true,
        online = true

    )

// DeviceState -- Offline
private val DeviceState_Offline =
    DeviceState(
        dateCaptured = Clock.System.now(),
        deviceId = 1L,
        on = true,
        online = false

    )

private val DeviceTest =
    Device(
        dateCommissioned = 123456789L,
        vendorId = "1234",
        productId = "5678",
        deviceType = DeviceType.LIGHT_ON_OFF,
        deviceId = 1L,
        name = "Living Room Light",
        productName = "My Light",
        vendorName = "MyVendor"

    )
