package no.nordicsemi.nrf.matter.ui.device

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.cloudy.cloudy
import no.nordicsemi.nrf.matter.commission.DecommissionDevice
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceUiModel
import no.nordicsemi.nrf.matter.model.LockDeviceState
import no.nordicsemi.nrf.matter.theme.NordicSun
import no.nordicsemi.nrf.matter.ui.BasicInformationBottomSheet
import no.nordicsemi.nrf.matter.ui.infoext.BasicInfoExtControlItem
import no.nordicsemi.nrf.matter.ui.infoext.BasicInfoExtViewModel
import no.nordicsemi.nrf.matter.ui.level.LevelControlItem
import no.nordicsemi.nrf.matter.ui.level.LevelControlViewModel
import no.nordicsemi.nrf.matter.ui.light.OnOffActionItem
import no.nordicsemi.nrf.matter.ui.light.OnOffViewModel
import no.nordicsemi.nrf.matter.ui.lock.DoorLockViewModel
import no.nordicsemi.nrf.matter.ui.lock.LockActionItem
import no.nordicsemi.nrf.matter.ui.manspec.ManufacturerSpecControlItem
import no.nordicsemi.nrf.matter.ui.manspec.ManufacturerSpecViewModel

@Composable
internal fun DeviceItem(
    device: DeviceUiModel,
    clusters: List<ClusterViewModel>,
    onDecommission: (DeviceId) -> Unit,
) {
    val onOff = clusters.filterIsInstance<OnOffViewModel>().firstOrNull()
    val doorLock = clusters.filterIsInstance<DoorLockViewModel>().firstOrNull()
    val levelControl = clusters.filterIsInstance<LevelControlViewModel>().firstOrNull()
    val manufacturerSpec = clusters.filterIsInstance<ManufacturerSpecViewModel>().firstOrNull()
    val basicInfoExt = clusters.filterIsInstance<BasicInfoExtViewModel>().firstOrNull()

    val onOffState = onOff?.state?.collectAsStateWithLifecycle()?.value
    val lockState = doorLock?.state?.collectAsStateWithLifecycle()?.value

    // The lock keeps its last known state while it is moving, so that the label does not flicker.
    var isLocked by remember { mutableStateOf(false) }
    LaunchedEffect(lockState) {
        (lockState as? UiState.Success)?.let { isLocked = it.data == LockDeviceState.LOCKED }
    }

    val isActive = onOffState?.isOn == true || isLocked
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var showMatterDeviceInfo by rememberSaveable { mutableStateOf(false) }

    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = if (isActive) BorderStroke(
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

        DeviceHeader(
            isOn = isActive,
            icon = device.device.toIcon(isActive),
            title = device.device.toTitle(),
            subtitle = device.device.toSubtitle(),
            bindingCapable = device.device.isBindingCapable(),
        ) {
            when {
                doorLock != null && lockState != null -> LockActionItem(
                    lockState = lockState,
                    isLocked = isLocked,
                    onLockUnlockDoor = doorLock::setLocked,
                )

                onOff != null && onOffState != null -> OnOffActionItem(
                    isOn = onOffState.isOn,
                    isEnabled = onOffState.isEnabled,
                    onCheckedChange = onOff::setOn,
                )

                else -> Icon(
                    imageVector = if (isExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { isExpanded = !isExpanded }
                )
            }
        }

        AnimatedVisibility(isExpanded) {
            Column {
                HorizontalDivider()

                levelControl?.let { BrightnessControl(it, device.device.deviceId) }
                basicInfoExt?.let { RandomNumberControl(it) }
                manufacturerSpec?.let { LedAndButtonControl(it) }

                SharedSection(device, showMatterDeviceInfo) { showMatterDeviceInfo = it }

                // Decommission device
                DecommissionDevice(device.device.deviceId, onDecommission)
            }
        }

        // Basic Information Bottom Sheet Dialog
        if (showMatterDeviceInfo) {
            BasicInformationBottomSheet(device, onDismiss = { showMatterDeviceInfo = false })
        }
    }
}

@Composable
private fun BrightnessControl(viewModel: LevelControlViewModel, deviceId: DeviceId) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LevelControlItem(
        deviceId = deviceId,
        brightness = state.brightness,
        isEnabled = state.isEnabled,
        onBrightnessChange = { _, brightness -> viewModel.setBrightness(brightness) },
        onBrightnessChangeFinished = { viewModel.commitBrightness() },
        modifier = Modifier.padding(16.dp),
    )
}

@Composable
private fun LedAndButtonControl(viewModel: ManufacturerSpecViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ManufacturerSpecControlItem(
        isLedOn = state.isLedOn,
        isButtonOn = state.isButtonPressed,
        isButtonPressed = (state.isButtonPressed as? UiState.Success)?.data == true,
        setLed = viewModel::setLed,
    )
}

@Composable
private fun RandomNumberControl(viewModel: BasicInfoExtViewModel) {
    val randomNumber by viewModel.randomNumber.collectAsStateWithLifecycle()

    BasicInfoExtControlItem(
        randomNumber = randomNumber,
        generateRandomNumber = viewModel::generateRandomNumber,
        modifier = Modifier.padding(16.dp),
    )
}

@Composable
private fun SharedSection(
    deviceUiModel: DeviceUiModel,
    showMatterDeviceInfo: Boolean,
    onShowMatterDeviceInfoChange: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onShowMatterDeviceInfoChange(true)
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
                value = deviceUiModel.device.vendorName ?: "UNKNOWN",
                modifier = Modifier.weight(1f)
            )
            InfoItem(
                label = "Firmware",
                value = deviceUiModel.device.softwareVersion ?: "UNKNOWN",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DeviceHeader(
    isOn: Boolean,
    icon: Painter,
    title: String,
    subtitle: String,
    bindingCapable: Boolean,
    mainAction: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val boxColor = if (isOn)
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
                tint = if (isOn)
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
            if (bindingCapable) {
                Text(
                    text = "Binding capability",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.5f)
                )
            }
        }

        mainAction()
    }
}
