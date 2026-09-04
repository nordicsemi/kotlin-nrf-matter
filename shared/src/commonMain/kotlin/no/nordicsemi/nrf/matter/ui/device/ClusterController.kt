package no.nordicsemi.nrf.matter.ui.device

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import no.nordicsemi.nrf.matter.domain.UiState

abstract class ClusterController(protected val scope: CoroutineScope) {

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
