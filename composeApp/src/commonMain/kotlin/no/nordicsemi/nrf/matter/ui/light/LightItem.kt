package no.nordicsemi.nrf.matter.ui.light

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.cloudy.cloudy
import no.nordicsemi.composeapp.generated.resources.Res
import no.nordicsemi.composeapp.generated.resources.light_bulb
import no.nordicsemi.nrf.matter.commission.DecommissionDevice
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceUiModel
import no.nordicsemi.nrf.matter.theme.NordicSun
import no.nordicsemi.nrf.matter.ui.BasicInformationBottomSheet
import no.nordicsemi.nrf.matter.ui.TestDeviceLight
import no.nordicsemi.nrf.matter.ui.manspec.ControlCardContainer
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

@Composable
fun LightItem(
    device: DeviceUiModel,
    lightDeviceState: LightDeviceState,
    isEnabled: Boolean,
    onBrightnessChange: (deviceId: DeviceId, brightnessLevel: Float) -> Unit,
    updateDeviceState: (deviceId: DeviceId, Boolean) -> Unit,
    onDecommission: (DeviceId) -> Unit,
) {

    LightItemContainer(
        device = device,
        deviceId = device.device.deviceId,
        title = device.device.productName ?: "Light",
        subtitle = "Turn light ON or OFF",
        icon = painterResource(Res.drawable.light_bulb),
        isLightOn = lightDeviceState.isOn,
        brightnessLevel = lightDeviceState.brightness,
        updateDeviceState = updateDeviceState,
        isEnabled = isEnabled,
        onBrightnessChange = onBrightnessChange,
        onDecommission = onDecommission,
    )
}

@Composable
internal fun LightItemContainer(
    device: DeviceUiModel,
    deviceId: DeviceId,
    title: String,
    subtitle: String,
    icon: Painter,
    isLightOn: Boolean,
    brightnessLevel: Float,
    isEnabled: Boolean,
    onBrightnessChange: (DeviceId, Float) -> Unit,
    updateDeviceState: (DeviceId, Boolean) -> Unit,
    onDecommission: (DeviceId) -> Unit,
) {
    var showMatterDeviceInfo by rememberSaveable { mutableStateOf(false) }
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = if (isLightOn) BorderStroke(
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
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val boxColor = if (isLightOn)
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
                    icon,
                    contentDescription = null,
                    tint = if (isLightOn)
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
                Text(
                    text = "Binding capability",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.5f)
                )

            }
            Switch(
                checked = isLightOn,
                onCheckedChange = {
                    updateDeviceState(deviceId, it)
                },
                enabled = isEnabled
            )
        }
        AnimatedVisibility(isExpanded) {
            Column {
                // TODO: Add if statement to show brightness only if the device supports it.
                // Brightness control section
                HorizontalDivider()
                BrightnessControlCard(
                    deviceId = deviceId,
                    brightnessLevel = brightnessLevel,
                    modifier = Modifier.padding(16.dp),
                    isEnabled = isEnabled,
                    onBrightnessChange = { deviceId, brightnessLevel ->
                        onBrightnessChange(deviceId, brightnessLevel)
                    }
                )

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
                DecommissionDevice(
                    deviceId,
                    onDecommission
                )
            }

        }
    }

    // Basic Information Bottom Sheet Dialog
    if (showMatterDeviceInfo) {
        BasicInformationBottomSheet(device, onDismiss = { showMatterDeviceInfo = false })
    }
}

@Composable
fun InfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    ControlCardContainer(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = value,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrightnessControlCard(
    modifier: Modifier = Modifier,
    deviceId: DeviceId,
    brightnessLevel: Float,
    isEnabled: Boolean,
    onBrightnessChange: (DeviceId, Float) -> Unit,
) {
    var brightness by remember { mutableFloatStateOf(brightnessLevel) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Brightness Control",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${(brightness * 100).roundToInt()}%",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Slider(
            value = brightness,
            onValueChange = { brightness = it },
            onValueChangeFinished = {
                onBrightnessChange(deviceId, brightness)
            },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                activeTrackColor = MaterialTheme.colorScheme.primaryContainer,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                thumbColor = MaterialTheme.colorScheme.primary
            ),
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = remember { MutableInteractionSource() },
                    colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isEnabled,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.LightMode,
                contentDescription = "Low Brightness",
                tint = MaterialTheme.colorScheme.outline,
            )
            Icon(
                imageVector = Icons.Filled.LightMode,
                contentDescription = "High Brightness",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LightItemContainerPreview() {
    LightItemContainer(
        device = TestDeviceLight,
        deviceId = DeviceId.Zero,
        title = "Light",
        subtitle = "Turn light ON or OFF",
        icon = painterResource(Res.drawable.light_bulb),
        isLightOn = true,
        brightnessLevel = 60f,
        onBrightnessChange = { _, _ -> },
        updateDeviceState = { _, _ -> },
        isEnabled = true,
        onDecommission = {  },
    )
}
