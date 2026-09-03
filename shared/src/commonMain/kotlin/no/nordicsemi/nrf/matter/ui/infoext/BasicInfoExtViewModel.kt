package no.nordicsemi.nrf.matter.ui.infoext

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.cluster.BasicInfoExtCluster
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.ui.device.ClusterViewModel

class BasicInfoExtViewModel(
    private val cluster: BasicInfoExtCluster,
) : ClusterViewModel() {

    private val _randomNumber = MutableStateFlow<UiState<Long>>(UiState.Idle())
    val randomNumber = _randomNumber.asStateFlow()

    fun generateRandomNumber() {
        execute { cluster.generateRandomNumber() }
            .withUiState()
            .onEach { newState -> _randomNumber.update { newState } }
            .launchIn(viewModelScope)
    }
}
