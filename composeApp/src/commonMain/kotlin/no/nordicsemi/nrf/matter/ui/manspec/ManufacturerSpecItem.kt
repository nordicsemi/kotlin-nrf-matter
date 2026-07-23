package no.nordicsemi.nrf.matter.ui.manspec

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.cloudy.cloudy
import no.nordicsemi.composeapp.generated.resources.Res
import no.nordicsemi.composeapp.generated.resources.light_bulb
import no.nordicsemi.nrf.matter.commission.DecommissionDevice
import no.nordicsemi.nrf.matter.device.UiState
import no.nordicsemi.nrf.matter.domain.ManufacturerSpecificData
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceMatterInfo
import no.nordicsemi.nrf.matter.model.DeviceType
import no.nordicsemi.nrf.matter.model.DeviceUiModel
import no.nordicsemi.nrf.matter.model.toDeviceId
import no.nordicsemi.nrf.matter.theme.NordicRed
import no.nordicsemi.nrf.matter.theme.NordicSun
import no.nordicsemi.nrf.matter.theme.NordicTheme
import no.nordicsemi.nrf.matter.ui.BasicInformationBottomSheet
import no.nordicsemi.nrf.matter.ui.light.InfoItem
import org.jetbrains.compose.resources.painterResource

@Composable
fun ManufacturerSpecItem(
    device: DeviceUiModel,
    manufacturerSpecificData: ManufacturerSpecificData,
    isLedOn: UiState<Boolean>,
    isButtonOn: UiState<Boolean>,
    randomNumber: UiState<Int>,
    setLed: (Boolean) -> Unit,
    generateRandomNumber: () -> Unit,
    onDecommission: (DeviceId) -> Unit,
) {
    var showMatterDeviceInfo by rememberSaveable { mutableStateOf(false) }
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = if (device.isOnline) BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(0.3f)
        ) else CardDefaults.outlinedCardBorder(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                isExpanded = !isExpanded
            }
            .then(if (showMatterDeviceInfo) Modifier.cloudy() else Modifier)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val boxColor = if (device.isOnline)
                    NordicSun
                else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            boxColor,
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painterResource(Res.drawable.light_bulb),
                        contentDescription = null,
                        tint = if (device.isOnline)
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
                        text = manufacturerSpecificData.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = "Turn light ON or OFF",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(0.5f)
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = "Expand",
                )
            }

            Column(
                modifier = Modifier
                    .padding(8.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GenerateRandomNumberBlock(randomNumber, generateRandomNumber)

                LedButtonRow(
                    isLedOn = isLedOn,
                    isButtonOn = isButtonOn,
                    setLed = setLed,
                    isButtonPressed = isButtonOn is UiState.Success && isButtonOn.data
                )
            }

        }
        AnimatedVisibility(isExpanded) {
            Column {
                // Matter Device information section
                HorizontalDivider()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            showMatterDeviceInfo = true
                        },
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Info",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,

                            )
                        Text(
                            text = "Matter Device information",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (showMatterDeviceInfo) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                            contentDescription = "Info",
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoItem(
                            label = "Vendor",
                            value = device.device.vendorName ?: "UNKNOWN",
                            modifier = Modifier.weight(1f)
                        )
                        InfoItem(
                            label = "Firmware",
                            value = device.device.softwareVersion ?: "UNKNOWN",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Decommission device
                DecommissionDevice(device.device.deviceId, onDecommission)
            }

        }
    }

    // Basic Information Bottom Sheet Dialog
    if (showMatterDeviceInfo) {
        BasicInformationBottomSheet(device, onDismiss = { showMatterDeviceInfo = false })
    }

}

@Composable
private fun GenerateRandomNumberBlock(
    randomNumber: UiState<Int>,
    generateRandomNumber: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                generateRandomNumber()
            },
        ) {
            Text(
                text = "Generate number",
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Box(
            ) {
                when (randomNumber) {
                    is UiState.Error -> Icon(
                        Icons.Outlined.Error,
                        "Error",
                        tint = NordicRed,
                    )

                    is UiState.Idle<Int> -> Text(
                        "__",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    is UiState.Loading<Int> -> CircularProgressIndicator(modifier = Modifier.size(28.dp))
                    is UiState.Success<Int> -> Text(
                        "${randomNumber.data}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Text(
                text = "Random number",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LedButtonRow(
    isLedOn: UiState<Boolean>,
    isButtonOn: UiState<Boolean>,
    isButtonPressed: Boolean,
    setLed: (Boolean) -> Unit,
) {
    val ledState = (isLedOn as? UiState.Success)?.data ?: false
    val buttonState = (isButtonOn as? UiState.Success)?.data == true

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        ControlCardContainer(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    Switch(
                        checked = ledState,
                        onCheckedChange = setLed,
                        modifier = Modifier.padding(4.dp),
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }
            }
            Text(
                text = "LED",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        ControlCardContainer(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.weight(1f),
            ) {
                Icon(
                    imageVector = if (buttonState) Icons.Default.RadioButtonChecked else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = "Push Action",
                    modifier = Modifier.size(28.dp),
                    tint = if (isButtonPressed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            }
            Text(
                text = if (buttonState) "Button pressed" else "Press button 01",
                style = MaterialTheme.typography.labelMedium,
                color = if (isButtonPressed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ControlCardContainer(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    val containerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f)
    val borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .background(containerColor)
            .padding(12.dp),
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun DeviceItemContainerPreview() {
    NordicTheme {
        ManufacturerSpecItem(
            device = TestDeviceManu,
            manufacturerSpecificData = TestDeviceManu.device.deviceMatterInfo[0].manufacturerSpecificData!!,
            isLedOn = UiState.Success(true),
            isButtonOn = UiState.Success(true),
            randomNumber = UiState.Success(123),
            setLed = {},
            generateRandomNumber = {},
            onDecommission = {}
        )
    }
}

internal val DeviceTest_MANUFACTURER =
    Device(
        dateCommissioned = 123456789L,
        vendorId = "4568",
        productId = "1235",
        deviceType = DeviceType.MANUFACTURER_SPECIFIC_DEVICE,
        deviceId = 1L.toDeviceId(),
        name = "Manufacturer Specific Device",
        productName = "Custom cluster device",
        vendorName = "Nordic Semiconductor",
        deviceMatterInfo = listOf(
            DeviceMatterInfo(
                endpoint = 1,
                types = listOf(1, 2, 3),
                serverClusters = listOf(1, 2, 3),
                clientClusters = listOf(1, 2, 3),
                manufacturerSpecificData = ManufacturerSpecificData(
                    name = "Custom cluster device",
                    led = true,
                    button = true
                )
            )
        )
    )

private val TestDeviceManu = DeviceUiModel(
    device = DeviceTest_MANUFACTURER,
    isOnline = true,
    isOn = true
)
