package no.nordicsemi.nrf.matter.ui.light

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.cluster.OnOffCluster
import no.nordicsemi.nrf.matter.ui.device.ClusterViewModel

data class OnOffState(
    val isOn: Boolean = false,
    val isEnabled: Boolean = true,
)

class OnOffViewModel(
    private val cluster: OnOffCluster,
) : ClusterViewModel() {

    private val _state = MutableStateFlow(OnOffState())
    val state = _state.asStateFlow()

    init {
        cluster.observeOnOff()
            .onEach { isOn -> _state.update { it.copy(isOn = isOn, isEnabled = true) } }
            .launchIn(viewModelScope)
    }

    fun setOn(isOn: Boolean) {
        execute { cluster.setOn(isOn) }
            .onStart { _state.update { it.copy(isOn = isOn, isEnabled = false) } }
            .onCompletion { _state.update { it.copy(isEnabled = true) } }
            .catch { _state.update { it.copy(isOn = !isOn, isEnabled = true) } }
            .launchIn(viewModelScope)
    }
}
