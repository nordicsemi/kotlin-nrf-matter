package no.nordicsemi.nrf.matter.ui.device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import no.nordicsemi.nrf.matter.cluster.BasicInfoExtCluster
import no.nordicsemi.nrf.matter.cluster.Cluster
import no.nordicsemi.nrf.matter.cluster.DoorLockCluster
import no.nordicsemi.nrf.matter.cluster.LevelControlCluster
import no.nordicsemi.nrf.matter.cluster.ManufacturerSpecCluster
import no.nordicsemi.nrf.matter.cluster.OnOffCluster
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.ui.infoext.BasicInfoExtViewModel
import no.nordicsemi.nrf.matter.ui.level.LevelControlViewModel
import no.nordicsemi.nrf.matter.ui.light.OnOffViewModel
import no.nordicsemi.nrf.matter.ui.lock.DoorLockViewModel
import no.nordicsemi.nrf.matter.ui.manspec.ManufacturerSpecViewModel

abstract class ClusterViewModel : ViewModel() {
    
    internal fun clear() {
        viewModelScope.cancel()
        onCleared()
    }

    protected fun <T> execute(action: suspend () -> T): Flow<T> {
        return flow { emit(action()) }
    }

    protected fun <T> Flow<T>.withUiState(): Flow<UiState<T>> {
        return this
            .map<T, UiState<T>> { UiState.Success(it) }
            .onStart { emit(UiState.Loading()) }
            .catch {
                it.printStackTrace()
                emit(UiState.Error("Failed to send command", it))
            }
    }
}

fun Cluster.toViewModel(): ClusterViewModel = when (this) {
    is OnOffCluster -> OnOffViewModel(this)
    is LevelControlCluster -> LevelControlViewModel(this)
    is DoorLockCluster -> DoorLockViewModel(this)
    is BasicInfoExtCluster -> BasicInfoExtViewModel(this)
    is ManufacturerSpecCluster -> ManufacturerSpecViewModel(this)
}
