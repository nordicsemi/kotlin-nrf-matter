package no.nordicsemi.nrf.matter.ui.light

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.domain.mapType
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceUiModel
import no.nordicsemi.nrf.matter.ui.MatterController
import kotlin.time.Duration.Companion.milliseconds

data class LightDeviceState(
    val isOn: Boolean = false,
    val localBrightness: Float = 0.0f,
    val remoteBrightness: Float = 0.0f,
    val errorMessage: String? = null,
)

class LightController(
    private val device: DeviceUiModel,
    private val commandHandler: LightCommandHandler,
    private val scope: CoroutineScope,
) : MatterController {
    val lightDeviceState = MutableStateFlow(LightDeviceState())

    val ledState = MutableStateFlow<UiState<Boolean>>(UiState.Idle())
    val brightnessLevelState = MutableStateFlow<UiState<Float>>(UiState.Idle())

    init {
        observeDeviceRealtimeState()
    }

    private fun observeDeviceRealtimeState() {
        commandHandler.observeLightDeviceState(device.device)
            .onEach { state ->
                NordicLogger.info("New light device state: $state")
                when (state) {
                    is UiState.Success -> lightDeviceState.update {
                        NordicLogger.info("New light device state: $state", tag = TAG)
                        it.copy(isOn = state.data, errorMessage = null)
                    }

                    is UiState.Error -> lightDeviceState.update {
                        it.copy(errorMessage = state.message)
                    }

                    else -> Unit
                }
            }
            .launchIn(scope)

        commandHandler.observeBrightnessState(device.device)
            .onEach { state ->
                NordicLogger.info("New brightness state: $state")
                when (state) {
                    is UiState.Success -> lightDeviceState.update {
                        NordicLogger.info("New brightness state: $state", tag = TAG)
                        it.copy(
                            localBrightness = state.data,
                            remoteBrightness = state.data,
                            errorMessage = null
                        )
                    }

                    is UiState.Error -> lightDeviceState.update {
                        it.copy(errorMessage = state.message)
                    }

                    else -> Unit
                }
            }
            .launchIn(scope)
    }


    fun setLet(device: Device, isOn: Boolean) {
        commandHandler.handleLed(device, isOn)
            .onStart {
                lightDeviceState.update {
                    it.copy(
                        isOn = isOn,
                        errorMessage = null
                    )
                }
            }
            .delaySuccess()
            .catch {
                NordicLogger.error(
                    "Failed to send Brightness level adjustment",
                    it,
                    tag = TAG
                )
            }
            .onEach {
                NordicLogger.info("Led state $it", tag = TAG)
                ledState.value = it.mapType { isOn }
                when (val newState = it.mapType { isOn }) {
                    is UiState.Success -> lightDeviceState.update {
                        it.copy(isOn = newState.data)
                    }

                    is UiState.Error -> lightDeviceState.update {
                        it.copy(isOn = !isOn, errorMessage = newState.message)
                    }

                    else -> Unit
                }
            }
            .launchIn(scope)
    }

    fun setBrightness(brightnessLevel: Float) {
        lightDeviceState.update {
            it.copy(localBrightness = brightnessLevel)
        }
    }

    fun updateRemoteBrightness(device: Device) {
        val brightnessLevel = lightDeviceState.value.localBrightness
        commandHandler.handleBrightness(device, brightnessLevel)
            .onStart { lightDeviceState.update { it.copy(errorMessage = null) } }
            .catch {
                NordicLogger.error(
                    "Failed to send Brightness level adjustment",
                    it,
                    tag = TAG
                )
            }
            .onEach {
                NordicLogger.info("Brightness state $it", tag = TAG)
                brightnessLevelState.value = it.mapType { brightnessLevel }

                when (val newState = it.mapType { brightnessLevel }) {
                    is UiState.Success -> lightDeviceState.update {
                        it.copy(remoteBrightness = newState.data)
                    }

                    is UiState.Error -> lightDeviceState.update {
                        it.copy(
                            localBrightness = it.remoteBrightness,
                            errorMessage = newState.message
                        )
                    }

                    else -> Unit
                }
            }
            .launchIn(scope)
    }

    // A dk can go to a strange state when there are too many requests at once.
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun <T> Flow<UiState<T>>.delaySuccess(): Flow<UiState<T>> {
        return flatMapConcat { state ->
            when (state) {
                is UiState.Success -> flow {
                    delay(300.milliseconds)
                    emit(state)
                }

                else -> flowOf(state)
            }
        }
    }

    @Composable
    override fun Item(onDecommission: (DeviceId) -> Unit) {
        val ledRequestState = ledState.collectAsStateWithLifecycle().value
        val brightnessRequestState =
            brightnessLevelState.collectAsStateWithLifecycle().value
        val isEnabled =
            ledRequestState !is UiState.Loading && brightnessRequestState !is UiState.Loading

        LightItem(
            device = device,
            lightDeviceState = lightDeviceState.collectAsStateWithLifecycle().value,
            isEnabled = isEnabled,
            onBrightnessChange = { _, brightnessLevel ->
                setBrightness(brightnessLevel)
            },
            onBrightnessChangeFinished = {
                updateRemoteBrightness(device.device)
            },
            updateDeviceState = { deviceId, state ->
                setLet(device.device, state)
            },
            onDecommission = onDecommission
        )
    }

    companion object {
        private const val TAG = "LightController"
    }
}
