package no.nordicsemi.nrf.matter.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.cloudy.cloudy
import no.nordicsemi.nrf.matter.device.DevicePresenter
import no.nordicsemi.nrf.matter.device.RemoveDeviceState
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceState
import no.nordicsemi.nrf.matter.model.DeviceType
import no.nordicsemi.nrf.matter.ui.AlertDialogView
import no.nordicsemi.nrf.matter.ui.Loader
import no.nordicsemi.nrf.matter.ui.SectionTitle
import nrfmatterformobile.composeapp.generated.resources.Res
import nrfmatterformobile.composeapp.generated.resources.light_bulb_smart_light
import nrfmatterformobile.composeapp.generated.resources.light_fixture
import nrfmatterformobile.composeapp.generated.resources.no_matter_devices
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.getKoin
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

/**
 * The Device Screen shows all the information about the device that was selected in the Home
 * screen. It supports the following actions:
 * ```
 * - toggle the on/off state of the device
 * - share the device with another Matter commissioner app
 * - remove the device
 * - inspect the device (get all info we can from the clusters supported by the device)
 * ```
 *
 * When the screen is shown, state monitoring is activated to get the device's latest state. This
 * makes it possible to update the device's online status dynamically.
 */
@Composable
fun DeviceScreen(
    deviceId: Long,
    padding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit
) {
    val devicePresenter: DevicePresenter = getKoin().get()
    val uiState by devicePresenter.uiState.collectAsState()
    var isRemoving by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(deviceId) {
        devicePresenter.loadDevice(deviceId)
    }
    if (uiState.deviceUiModel == null) {
        Text("Still loading the device information")
        return
    }

    val device = uiState.deviceUiModel ?: run {
        Text("Loading device…")
        return
    }

    when (uiState.removeDeviceState) {

        RemoveDeviceState.ConfirmRemove -> {
            isRemoving = true
            AlertDialogView(
                onDismiss = { devicePresenter.updateRemoveDeviceState(RemoveDeviceState.Idle) },
                onConfirm = { devicePresenter.removeDevice(device.device.deviceId) },
                title = "Remove Device",
                message = "Are you sure you want to remove this device?"
            )
        }

        is RemoveDeviceState.ForceRemove -> {
            isRemoving = true
            AlertDialogView(
                onDismiss = { devicePresenter.updateRemoveDeviceState(RemoveDeviceState.Idle) },
                onConfirm = {
                    devicePresenter.removeDeviceWithoutUnlink(device.device.deviceId)
                },
                title = "Force Remove Device",
                message = "Unable to unlink device. Force remove?"
            )
        }

        is RemoveDeviceState.Removed -> {
            isRemoving = false
            LaunchedEffect(true) {
                snackbarHostState.showSnackbar("Device removed")
            }
            onBack()
        }

        RemoveDeviceState.Removing -> {
            isRemoving = true
            Loader {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Removing device...",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        "It might take a few seconds, please wait!",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        RemoveDeviceState.Idle -> {
            isRemoving = false
        }
    }

    Box(
        modifier = Modifier
            .padding(padding)
            .fillMaxWidth()
            .then(if (isRemoving) Modifier.cloudy() else Modifier)
    ) {

        Column(
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {

            DeviceHeader()

            PowerCard(
                enabled = uiState.deviceUiModel!!.isOn,
                onToggle = {
                    devicePresenter.updateDevicePowerState(
                        device.device.deviceId,
                        it
                    )
                }
            )

            SectionTitle("Sharing")
            ShareCard { /* todo: Add share device feature. */ }

            SectionTitle("Technical Details")
            TechnicalDetailsCard()

            Spacer(modifier = Modifier.height(16.dp))
            RemoveDeviceSection {
                devicePresenter.updateRemoveDeviceState(RemoveDeviceState.ConfirmRemove)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DeviceHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = painterResource(resource = Res.drawable.light_fixture),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )
        Text(
            "Living Room Light",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(MatterGreen, CircleShape)
            )
            Spacer(Modifier.width(8.dp))
            Text("Online", color = MatterGreen)
            Spacer(Modifier.width(8.dp))
            Text("•", color = Color.Gray)
            Spacer(Modifier.width(8.dp))
            Text("Matter Device", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun PowerCard(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    DeviceItemContainer(
        icon = painterResource(resource = Res.drawable.light_bulb_smart_light),
        title = "Power",
        subtitle = "Turn device ON or OFF",
        isOnline = enabled,
        onDeviceClick = { },
    ) {
        var isChecked by rememberSaveable { mutableStateOf(enabled) }
        Switch(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                onToggle(isChecked)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PowerCardPreview() {
    PowerCard(enabled = true) { }
}

@Composable
fun ShareCard(onShare: () -> Unit) {
    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder(enabled = false),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onShare() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Share with other apps",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    "You can share this device to control it from other apps or services.",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.5f)
                )
            }

            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShareCardPreview() {
    ShareCard { }
}

@Preview(showBackground = true)
@Composable
fun TechnicalDetailsCard() {
    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder(enabled = false),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            DetailRow("Vendor ID", "0x1234")
            DetailRow("Product ID", "0xABCD")
            DetailRow("Device Type", "Dimmable Light")
            DetailRow("Added Date", "Oct 24, 2023", divider = false)
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    divider: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            modifier = Modifier
                .alpha(0.5f)
        )
        Text(
            value,
            style = MaterialTheme.typography.titleMedium,
        )
    }

    if (divider) HorizontalDivider(
        modifier = Modifier.alpha(0.3f)
    )
}

@Preview(showBackground = true)
@Composable
fun RemoveDeviceSection(onRemove: () -> Unit = {}) {
    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder(enabled = false),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onRemove() }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Remove Device", fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Text(
                "Removing this device will disconnect it from your Matter fabric and home network.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.alpha(0.5f)
            )
        }
    }
}

@Composable
fun DeviceItemContainer(
    icon: Painter,
    title: String,
    subtitle: String,
    isOnline: Boolean = true,
    onDeviceClick: () -> Unit,
    content: @Composable () -> Unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = if (isOnline) BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(0.3f)
        ) else CardDefaults.outlinedCardBorder(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onDeviceClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (isOnline)
                        MaterialTheme.colorScheme.primary else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.5f)
                )

            }

            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceItemContainerPreview() {
    DeviceItemContainer(
        icon = painterResource(resource = Res.drawable.no_matter_devices),
        title = "Living Room Lamp",
        subtitle = "Dimmable Light",
        isOnline = true,
        {}
    ) {
        Text("50%", fontWeight = FontWeight.Bold)
    }

}

// Lock Item
@Composable
fun LockItem(icon: Painter) {
    DeviceItemContainer(
        icon = icon,// TODO: Change it to the door lock icon.
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

// Thermostat Item
@Composable
fun ThermostatItem(icon: Painter) {
    DeviceItemContainer(
        icon = icon,
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
