package no.nordicsemi.nrf.matter.ui.lock

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import no.nordicsemi.nrf.matter.device.UiState
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceUiModel
import no.nordicsemi.nrf.matter.model.LockDeviceState
import no.nordicsemi.nrf.matter.ui.MatterController
import kotlin.time.Duration.Companion.milliseconds

class LockController(
    private val device: DeviceUiModel,
    private val commandHandler: LockCommandHandler,
    private val scope: CoroutineScope,
) : MatterController {

    val lockState = MutableStateFlow(LockDeviceState.UNLOCKED)
    val lockingState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val finalState = lockState.combine(lockingState) { lockState, operationState ->
        when (operationState) {
            is UiState.Error -> UiState.Error(operationState.message, operationState.cause)
            is UiState.Loading -> UiState.Loading()
            is UiState.Idle,
            is UiState.Success -> when (lockState) {
                LockDeviceState.NOT_FULLY_LOCKED -> UiState.Loading()
                LockDeviceState.LOCKED,
                LockDeviceState.UNLOCKED -> UiState.Success(lockState)
            }
        }
    }
        .debounce(300.milliseconds)
        .stateIn(scope, SharingStarted.Eagerly, UiState.Loading())

    init {
        observeDeviceRealtimeState()
    }

    private fun observeDeviceRealtimeState() {
        commandHandler.observeLockDeviceState(device.device)
            .onEach {
                lockState.value = it
            }
            .launchIn(scope)
    }

    fun setLock(device: Device, isOn: Boolean) {
        commandHandler.handleLock(device, isOn)
            .catch {
                NordicLogger.error(
                    "Failed to send Lock/Unlock command",
                    it,
                    tag = "LockController"
                )
            }
            .onEach {
                lockingState.value = it
            }
            .launchIn(scope)
    }

    @Composable
    override fun Item(onDecommission: (DeviceId) -> Unit) {
        LockItem(
            device = device,
            lockState = finalState.collectAsStateWithLifecycle().value,
            onLockUnlockDoor = { _, state ->
                setLock(device.device, state)
            },
            onDecommission = onDecommission,
        )
    }
}
