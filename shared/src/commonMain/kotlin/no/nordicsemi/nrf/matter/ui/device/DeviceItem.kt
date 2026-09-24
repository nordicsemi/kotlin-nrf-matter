package no.nordicsemi.nrf.matter.ui.device

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.List
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.cloudy.cloudy
import no.nordicsemi.nrf.matter.binding.isBindingSource
import no.nordicsemi.nrf.matter.cluster.cleaningMode
import no.nordicsemi.nrf.matter.commission.DecommissionDevice
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.LockDeviceState
import no.nordicsemi.nrf.matter.model.RvcOperationalState
import no.nordicsemi.nrf.matter.theme.NordicSun
import no.nordicsemi.nrf.matter.ui.BasicInformationBottomSheet
import no.nordicsemi.nrf.matter.ui.DeviceInfoBottomSheet
import no.nordicsemi.nrf.matter.ui.UiState
import no.nordicsemi.nrf.matter.ui.contact.ContactSensorActionItem
import no.nordicsemi.nrf.matter.ui.contact.ContactSensorController
import no.nordicsemi.nrf.matter.ui.infoext.BasicInfoExtControlItem
import no.nordicsemi.nrf.matter.ui.infoext.BasicInfoExtController
import no.nordicsemi.nrf.matter.ui.level.LevelControlItem
import no.nordicsemi.nrf.matter.ui.level.LevelControlController
import no.nordicsemi.nrf.matter.ui.light.OnOffActionItem
import no.nordicsemi.nrf.matter.ui.light.OnOffController
import no.nordicsemi.nrf.matter.ui.lock.DoorLockController
import no.nordicsemi.nrf.matter.ui.lock.LockActionItem
import no.nordicsemi.nrf.matter.ui.manspec.ManufacturerSpecControlItem
import no.nordicsemi.nrf.matter.ui.manspec.ManufacturerSpecController
import no.nordicsemi.nrf.matter.ui.rvc.RvcActionItem
import no.nordicsemi.nrf.matter.ui.rvc.RvcCleanModeController
import no.nordicsemi.nrf.matter.ui.rvc.RvcControlPanel
import no.nordicsemi.nrf.matter.ui.rvc.RvcOperationalStateController
import no.nordicsemi.nrf.matter.ui.rvc.RvcRunModeController
import no.nordicsemi.nrf.matter.ui.smokecoalarm.SmokeCoAlarmActionItem
import no.nordicsemi.nrf.matter.ui.smokecoalarm.SmokeCoAlarmControlItem
import no.nordicsemi.nrf.matter.ui.smokecoalarm.SmokeCoAlarmController
import no.nordicsemi.nrf.matter.ui.temperature.TemperatureSensorActionItem
import no.nordicsemi.nrf.matter.ui.temperature.TemperatureSensorController

@Composable
internal fun DeviceItem(
    device: Device,
    clusters: List<ClusterController>,
    onDecommission: (DeviceId) -> Unit,
) {
    val onOff = clusters.filterIsInstance<OnOffController>().firstOrNull()
    val doorLock = clusters.filterIsInstance<DoorLockController>().firstOrNull()
    val levelControl = clusters.filterIsInstance<LevelControlController>().firstOrNull()
    val manufacturerSpec = clusters.filterIsInstance<ManufacturerSpecController>().firstOrNull()
    val basicInfoExt = clusters.filterIsInstance<BasicInfoExtController>().firstOrNull()
    val contactSensor = clusters.filterIsInstance<ContactSensorController>().firstOrNull()
    val temperatureSensor = clusters.filterIsInstance<TemperatureSensorController>().firstOrNull()
    val smokeCoAlarm = clusters.filterIsInstance<SmokeCoAlarmController>().firstOrNull()
    val rvcOperationalState = clusters.filterIsInstance<RvcOperationalStateController>().firstOrNull()
    val rvcRunMode = clusters.filterIsInstance<RvcRunModeController>().firstOrNull()
    val rvcCleanMode = clusters.filterIsInstance<RvcCleanModeController>().firstOrNull()

    val onOffState = onOff?.state?.collectAsStateWithLifecycle()?.value
    val lockState = doorLock?.state?.collectAsStateWithLifecycle()?.value
    val manufacturerSpecState = manufacturerSpec?.state?.collectAsStateWithLifecycle()?.value
    val contactSensorState = contactSensor?.state?.collectAsStateWithLifecycle()?.value
    val temperatureSensorState = temperatureSensor?.state?.collectAsStateWithLifecycle()?.value
    val rvcOperationalStateValue = rvcOperationalState?.state?.collectAsStateWithLifecycle()?.value
    val rvcRunModeValue = rvcRunMode?.state?.collectAsStateWithLifecycle()?.value
    val smokeCoAlarmState = smokeCoAlarm?.state?.collectAsStateWithLifecycle()?.value

    // The lock keeps its last known state while it is moving, so that the label does not flicker.
    var isLocked by remember { mutableStateOf(false) }
    LaunchedEffect(lockState) {
        (lockState as? UiState.Success)?.let { isLocked = it.data == LockDeviceState.LOCKED }
    }

    val vacuumState = (rvcOperationalStateValue as? UiState.Success)?.data?.state
    val isVacuumRunning = vacuumState == RvcOperationalState.RUNNING
    val isVacuumPaused = vacuumState == RvcOperationalState.PAUSED
    val isActive = onOffState?.isOn == true || isLocked || isVacuumRunning
    val isIconLit = isActive || contactSensorState?.isContactDetected == true ||
            smokeCoAlarmState?.isAlarmActive == true
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var showMatterDeviceInfo by rememberSaveable { mutableStateOf(false) }
    var showDeviceInfo by rememberSaveable { mutableStateOf(false) }

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
            .then(if (showMatterDeviceInfo || showDeviceInfo) Modifier.cloudy() else Modifier)
    ) {

        DeviceHeader(
            isOn = isIconLit,
            icon = device.toIcon(isIconLit),
            title = manufacturerSpecState?.displayName ?: device.toTitle(),
            subtitle = contactSensorState?.let {
                if (it.isContactDetected) "Contact detected" else "Contact not detected"
            } ?: smokeCoAlarmState?.let {
                if (it.isAlarmActive) "Alarm active" else "No alarm"
            } ?: device.toSubtitle(),
            bindingCapable = device.isBindingSource() != null,
            spinning = isVacuumRunning,
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

                contactSensor != null && contactSensorState != null -> ContactSensorActionItem(
                    isContactDetected = contactSensorState.isContactDetected,
                )

                temperatureSensor != null && temperatureSensorState != null -> TemperatureSensorActionItem(
                    temperatureCelsius = temperatureSensorState.temperatureCelsius,
                )

                rvcOperationalState != null && rvcOperationalStateValue != null -> RvcActionItem(
                    operationalState = rvcOperationalStateValue,
                    onPlay = {
                        if (isVacuumPaused) {
                            rvcOperationalState.resume()
                        } else {
                            (rvcRunModeValue as? UiState.Success)?.data?.supportedModes?.cleaningMode()
                                ?.let { rvcRunMode.changeToMode(it.mode) }
                        }
                    },
                    onStop = rvcOperationalState::goHome,
                )

                smokeCoAlarm != null && smokeCoAlarmState != null -> SmokeCoAlarmActionItem(
                    isAlarmActive = smokeCoAlarmState.isAlarmActive,
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

                levelControl?.let { BrightnessControl(it, device.deviceId) }
                basicInfoExt?.let { RandomNumberControl(it) }
                manufacturerSpec?.let { LedAndButtonControl(it) }
                smokeCoAlarm?.let { SmokeCoAlarmControl(it) }

                if (rvcOperationalState != null || rvcRunMode != null || rvcCleanMode != null) {
                    RvcControlPanel(rvcOperationalState, rvcRunMode, rvcCleanMode)
                }

                SharedSection(device) { showMatterDeviceInfo = it }
                EndpointsClustersRow(onClick = { showDeviceInfo = true })

                // Decommission device
                DecommissionDevice(device.deviceId, onDecommission)
            }
        }

        // Basic Information Bottom Sheet Dialog
        if (showMatterDeviceInfo) {
            BasicInformationBottomSheet(device, onDismiss = { showMatterDeviceInfo = false })
        }

        // Endpoints & Clusters Bottom Sheet Dialog
        if (showDeviceInfo) {
            DeviceInfoBottomSheet(device, onDismiss = { showDeviceInfo = false })
        }
    }
}

@Composable
private fun BrightnessControl(controller: LevelControlController, deviceId: DeviceId) {
    val state by controller.state.collectAsStateWithLifecycle()

    LevelControlItem(
        deviceId = deviceId,
        brightness = state.brightness,
        isEnabled = state.isEnabled,
        onBrightnessChange = { _, brightness -> controller.setBrightness(brightness) },
        onBrightnessChangeFinished = { controller.commitBrightness() },
        modifier = Modifier.padding(16.dp),
    )
}

@Composable
private fun LedAndButtonControl(controller: ManufacturerSpecController) {
    val state by controller.state.collectAsStateWithLifecycle()

    ManufacturerSpecControlItem(
        isLedOn = state.isLedOn,
        isButtonOn = state.isButtonPressed,
        isButtonPressed = (state.isButtonPressed as? UiState.Success)?.data == true,
        setLed = controller::setLed,
    )
}

@Composable
private fun SmokeCoAlarmControl(controller: SmokeCoAlarmController) {
    val state by controller.state.collectAsStateWithLifecycle()
    val selfTestState by controller.selfTestState.collectAsStateWithLifecycle()

    SmokeCoAlarmControlItem(
        state = state,
        selfTestState = selfTestState,
        onRunSelfTest = controller::runSelfTest,
        modifier = Modifier.padding(16.dp),
    )
}

@Composable
private fun RandomNumberControl(controller: BasicInfoExtController) {
    val randomNumber by controller.randomNumber.collectAsStateWithLifecycle()

    BasicInfoExtControlItem(
        randomNumber = randomNumber,
        generateRandomNumber = controller::generateRandomNumber,
        modifier = Modifier.padding(16.dp),
    )
}

@Composable
private fun SharedSection(
    device: Device,
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
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Info",
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoItem(
                label = "Vendor",
                value = device.basicInformation.vendorName ?: "UNKNOWN",
                modifier = Modifier.weight(1f)
            )
            InfoItem(
                label = "Firmware",
                value = device.basicInformation.softwareVersion ?: "UNKNOWN",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun EndpointsClustersRow(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.List,
            contentDescription = "Endpoints & Clusters",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "Endpoints & Clusters",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
        )
    }
}

@Composable
private fun DeviceHeader(
    isOn: Boolean,
    icon: Painter,
    title: String,
    subtitle: String,
    bindingCapable: Boolean,
    spinning: Boolean = false,
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

        // The one animated moment in this card: the icon spins while the device is actively
        // doing the thing it exists to do (currently only wired up for a running vacuum).
        val rotation = if (spinning) {
            rememberInfiniteTransition(label = "device-icon-spin")
                .animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(tween(1400, easing = LinearEasing)),
                    label = "angle",
                ).value
        } else 0f

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
                modifier = Modifier.size(28.dp).rotate(rotation)
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
