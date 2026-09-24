package no.nordicsemi.nrf.matter.ui.rvc

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.cluster.ModeOption
import no.nordicsemi.nrf.matter.cluster.RvcCleanModeCluster
import no.nordicsemi.nrf.matter.ui.UiState
import no.nordicsemi.nrf.matter.ui.device.ClusterController

data class RvcCleanModeData(
    val supportedModes: List<ModeOption>,
    val currentMode: Int,
)

class RvcCleanModeController(
    private val cluster: RvcCleanModeCluster,
    scope: CoroutineScope,
) : ClusterController(scope) {

    private val _state = MutableStateFlow<UiState<RvcCleanModeData>>(UiState.Loading())
    val state = _state.asStateFlow()

    private val _supportedModes = MutableStateFlow<List<ModeOption>>(emptyList())

    init {
        execute { cluster.supportedModes() }
            .catch { emit(emptyList()) }
            .onEach { modes -> _supportedModes.update { modes } }
            .launchIn(scope)

        combine(_supportedModes, cluster.observeCurrentMode()) { supportedModes, currentMode ->
            RvcCleanModeData(supportedModes, currentMode.toInt())
        }
            .onEach { data -> _state.update { UiState.Success(data) } }
            .launchIn(scope)
    }

    fun changeToMode(mode: Int) {
        execute { cluster.changeToMode(mode) }
            .catch { it.printStackTrace() }
            .launchIn(scope)
    }
}
