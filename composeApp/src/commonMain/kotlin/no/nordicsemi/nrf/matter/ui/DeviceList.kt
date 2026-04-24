package no.nordicsemi.nrf.matter.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.flow
import no.nordicsemi.nrf.matter.HomeViewModel
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceType
import no.nordicsemi.nrf.matter.model.DeviceUiModel
import no.nordicsemi.nrf.matter.model.toDeviceId
import no.nordicsemi.nrf.matter.screens.DeviceItemContainer
import no.nordicsemi.nrf.matter.theme.NordicTheme
import no.nordicsemi.nrf.matter.ui.blinky.BlinkyControlView
import no.nordicsemi.nrf.matter.utils.title
import no.nordicsemi.nrf.matter.utils.toSection
import nrfmatterformobile.composeapp.generated.resources.Res
import nrfmatterformobile.composeapp.generated.resources.door_lock
import nrfmatterformobile.composeapp.generated.resources.door_lock_open_right
import nrfmatterformobile.composeapp.generated.resources.light_bulb
import nrfmatterformobile.composeapp.generated.resources.power_settings
import nrfmatterformobile.composeapp.generated.resources.smart_outlet
import nrfmatterformobile.composeapp.generated.resources.temperature
import org.jetbrains.compose.resources.painterResource

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
internal fun DeviceList(
    homeViewModel: HomeViewModel,
    devicesList: List<DeviceUiModel>,
    onDeviceClick: (DeviceUiModel) -> Unit,
    updateDeviceState: (deviceId: DeviceId, value: Boolean) -> Unit
) {

    val groupedDevices = devicesList.groupBy { it.device.deviceType.toSection() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        groupedDevices.forEach { (section, devices) ->

            item {
                SectionTitle(section.title())
            }

            items(devices, key = { it.device.deviceId.stringValue }) { device ->

                when (device.device.deviceType) {

                    DeviceType.MANUFACTURER_SPECIFIC_DEVICE -> {
                        ManufacturerSpecItem(
                            homeViewModel = homeViewModel,
                            device = device,
                            enabled = device.isOn,
                            updateDeviceState = updateDeviceState,
                            onClick = { onDeviceClick(device) }
                        )
                    }
                    DeviceType.LIGHT_ON_OFF,
                    DeviceType.DIMMABLE_LIGHT,
                    DeviceType.COLOR_TEMPERATURE_LIGHT,
                    DeviceType.EXTENDED_COLOR_LIGHT -> {

                        DeviceControlItem(
                            deviceId = device.device.deviceId,
                            title = "Light",
                            subtitle = "Turn light ON or OFF",
                            icon = painterResource(Res.drawable.light_bulb),
                            enabled = device.isOn,
                            updateDeviceState = updateDeviceState,
                            onClick = { onDeviceClick(device) }
                        )
                    }

                    DeviceType.LIGHT_SWITCH,
                    DeviceType.OUTLET -> {

                        DeviceControlItem(
                            deviceId = device.device.deviceId,
                            title = "Power Outlet",
                            subtitle = "Turn device ON or OFF",
                            icon = painterResource(Res.drawable.smart_outlet),
                            enabled = device.isOn,
                            updateDeviceState = updateDeviceState,
                            onClick = { onDeviceClick(device) }
                        )
                    }

                    DeviceType.DOOR_LOCK -> {
                        LockItem(
                            deviceId = device.device.deviceId,
                            title = "Front Door",
                            subtitle = "Smart Lock",
                            isLocked = device.isOn,
                            onLockUnlockDoor = updateDeviceState,
                            onDeviceClick = { onDeviceClick(device) }
                        )
                    }

                    DeviceType.UNKNOWN -> {
                        Text("Unsupported device")
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun DeviceListPreview() {
//    NordicTheme {
//        DeviceList(
//            devicesList = DeviceUiModel_Test,
//            isButtonOn = false,
//            onDeviceClick = {},
//            updateDeviceState = { _, _ -> }
//        )
//    }
//}

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
        icon = painterResource(Res.drawable.door_lock),// TODO: Change it based on the lock/unlock command.
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

@Composable
fun deviceIcon(device: DeviceUiModel): Painter {

    return when (device.device.deviceType) {

        DeviceType.LIGHT_ON_OFF,
        DeviceType.DIMMABLE_LIGHT,
        DeviceType.COLOR_TEMPERATURE_LIGHT,
        DeviceType.EXTENDED_COLOR_LIGHT ->
            painterResource(Res.drawable.light_bulb)

        DeviceType.OUTLET ->
            painterResource(Res.drawable.smart_outlet)

        DeviceType.DOOR_LOCK ->
            if (device.isOn)
                painterResource(Res.drawable.door_lock_open_right)
            else
                painterResource(Res.drawable.door_lock)

        DeviceType.LIGHT_SWITCH ->
            painterResource(Res.drawable.power_settings)

        else ->
            painterResource(Res.drawable.power_settings)
    }
}

// -----------------------------------------------------------------------------------------------
// Constant objects used in Compose Preview

internal val DeviceTest_LIGHT =
    Device(
        dateCommissioned = 123456789L,
        vendorId = "1234",
        productId = "5678",
        deviceType = DeviceType.LIGHT_ON_OFF,
        deviceId = 1L.toDeviceId(),
        name = "Living Room Light",
        productName = "My Light",
        vendorName = "MyVendor",
        deviceMatterInfo = emptyList()
    )

internal val DeviceTest_DOORLOCK =
    Device(
        dateCommissioned = 123456789L,
        vendorId = "1234",
        productId = "5678",
        deviceType = DeviceType.DOOR_LOCK,
        deviceId = 2L.toDeviceId(), // Fix: Changed deviceId to 2L to ensure unique keys in LazyColumn
        name = "Front Door", // Updated name for consistency
        productName = "My Lock",
        vendorName = "MyVendor",
        deviceMatterInfo = emptyList()
    )

internal val DEVICE_LIST_TEST =
    listOf(DeviceTest_LIGHT, DeviceTest_DOORLOCK)

val DeviceUiModel_Test =
    listOf(
        DeviceUiModel(
            device = DeviceTest_LIGHT,
            isOnline = true,
            isOn = true
        ),
        DeviceUiModel(
            device = DeviceTest_DOORLOCK,
            isOnline = true,
            isOn = false
        ),
    )
