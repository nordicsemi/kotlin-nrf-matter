package no.nordicsemi.nrf.matter.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.HomeViewModel
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceUiModel
import no.nordicsemi.nrf.matter.ui.blinky.BlinkyControlView
import kotlin.time.Clock

@Composable
internal fun ManufacturerSpecItem(
    homeViewModel: HomeViewModel,
    device: DeviceUiModel,
    enabled: Boolean,
    updateDeviceState: (deviceId: DeviceId, Boolean) -> Unit,
    onClick: () -> Unit
) {
    val isButtonOn = homeViewModel.subscribeToButtonChanges(device.device.deviceId)
        .collectAsStateWithLifecycle(initialValue = false)
        .value
    val data = device.device.deviceMatterInfo.first().manufacturerSpecificData!! // Shouldn't be null for this device.

    val isBindingOn = remember { mutableStateOf<Boolean>(false) }
    val pressStartTime = remember { mutableStateOf<Long?>(null) }

    val buttonPressedFlow = remember { MutableSharedFlow<Unit>(extraBufferCapacity = 1) }
    val buttonLongPressedFlow = remember { MutableSharedFlow<Unit>(extraBufferCapacity = 1) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(isBindingOn.value, device.isOn) {
        Napier.i { "AAATESTAAA - device on: ${device.isOn}" }
        if (isBindingOn.value) {
                buttonPressedFlow.collect {
                    Napier.i { "AAATESTAAA - device on button click: ${device.isOn}" }
                    updateDeviceState(device.device.deviceId, !device.isOn)
                }
        }
    }

    LaunchedEffect(isButtonOn) {
        val now = Clock.System.now().toEpochMilliseconds()

        if (isButtonOn) {
            // START
            pressStartTime.value = now
        } else {
            // STOP
            val start = pressStartTime.value
            pressStartTime.value = null

            if (start != null) {
                val duration = now - start

                Napier.i { "AAATESTAAA - duration: $duration" }

                if (duration >= 1_000) {
                    buttonLongPressedFlow.emit(Unit)
                } else {
                    buttonPressedFlow.emit(Unit)
                }
            }
        }
    }

    BlinkyControlView(
        ledState = device.isOn,
        onStateChanged = { updateDeviceState(device.device.deviceId, !device.isOn) },
        onBlink = {
            homeViewModel.changeDeviceStat3Times(device.device.deviceId, !device.isOn)
        },
        bindingState = isBindingOn.value,
        onBindingChanged = {
            isBindingOn.value = it
        },
        buttonState = isButtonOn,
        buttonPressed = buttonPressedFlow,
        buttonLongPressed = buttonLongPressedFlow
    )

//    DeviceItemContainer(
//        icon = painterResource(Res.drawable.light_bulb),
//        title = data.name,
//        subtitle = "Turn light ON or OFF",
//        isOnline = enabled,
//        onDeviceClick = onClick
//    ) {
//        Switch(
//            checked = enabled,
//            onCheckedChange = {
//                updateDeviceState(device.device.deviceId, it)
//            }
//        )
//
//        Switch(
//            checked = isButtonOn,
//            onCheckedChange = {
//                updateDeviceState(device.device.deviceId, it)
//            },
//            enabled = false,
//        )
//    }
}