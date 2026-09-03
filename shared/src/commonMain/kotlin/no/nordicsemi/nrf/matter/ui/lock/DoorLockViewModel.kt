package no.nordicsemi.nrf.matter.ui.lock

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.cluster.DoorLockCluster
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.model.LockDeviceState
import no.nordicsemi.nrf.matter.ui.device.ClusterViewModel

class DoorLockViewModel(
    private val cluster: DoorLockCluster,
) : ClusterViewModel() {

    private val _state = MutableStateFlow<UiState<LockDeviceState>>(UiState.Loading())
    val state = _state.asStateFlow()

    init {
        cluster.observeLockState()
            .mapNotNull { it.toLockDeviceState()?.toUiState() }
            .onEach { newValue -> _state.update { newValue } }
            .launchIn(viewModelScope)
    }

    fun setLocked(isLocked: Boolean) {
        execute { cluster.setLocked(isLocked) }
            .onStart { _state.update { UiState.Loading() } }
            .catch { _state.update { UiState.Error("Could not change the lock state.") } }
            .launchIn(viewModelScope)
    }
}
