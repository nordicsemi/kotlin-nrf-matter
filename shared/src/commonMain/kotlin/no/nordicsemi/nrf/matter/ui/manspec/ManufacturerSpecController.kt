package no.nordicsemi.nrf.matter.ui.manspec

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.cluster.ManufacturerSpecCluster
import no.nordicsemi.nrf.matter.ui.UiState
import no.nordicsemi.nrf.matter.ui.device.ClusterController

data class ManufacturerSpecState(
    val isLedOn: UiState<Boolean> = UiState.Idle(),
    val isButtonPressed: UiState<Boolean> = UiState.Idle(),
)

class ManufacturerSpecController(
    private val cluster: ManufacturerSpecCluster,
    scope: CoroutineScope,
) : ClusterController(scope) {

    private val _state = MutableStateFlow(ManufacturerSpecState())
    val state = _state.asStateFlow()

    init {
        cluster.observeLed()
            .withUiState()
            .onEach { value -> _state.update { it.copy(isLedOn = value) } }
            .launchIn(scope)

        cluster.observeButton()
            .withUiState()
            .onEach { value -> _state.update { it.copy(isButtonPressed = value) } }
            .launchIn(scope)
    }

    fun setLed(isOn: Boolean) {
        execute { cluster.setLed(isOn) }
            .map { isOn }
            .withUiState()
            .onEach { value -> _state.update { it.copy(isLedOn = value) } }
            .launchIn(scope)
    }
}
