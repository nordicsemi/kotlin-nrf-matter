package no.nordicsemi.nrf.matter.ui.level

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.cluster.LevelControlCluster
import no.nordicsemi.nrf.matter.ui.device.ClusterController

data class LevelControlState(
    val brightness: Float = 0f,
    val isEnabled: Boolean = true,
)

class LevelControlController(
    private val cluster: LevelControlCluster,
    scope: CoroutineScope,
) : ClusterController(scope) {

    private val _state = MutableStateFlow(LevelControlState())
    val state = _state.asStateFlow()

    init {
        cluster.observeLevel()
            .onEach { value ->
                _state.update { it.copy(brightness = value.toBrightness()) }
            }
            .launchIn(scope)
    }

    /** Moves the slider without touching the device. The value is sent by [commitBrightness]. */
    fun setBrightness(brightness: Float) {
        _state.update { it.copy(brightness = brightness) }
    }

    fun commitBrightness() {
        execute { cluster.setLevel(_state.value.brightness.toLevel()) }
            .onStart { _state.update { it.copy(isEnabled = false) } }
            .onCompletion { _state.update { it.copy(isEnabled = true) } }
            .launchIn(scope)
    }
}
