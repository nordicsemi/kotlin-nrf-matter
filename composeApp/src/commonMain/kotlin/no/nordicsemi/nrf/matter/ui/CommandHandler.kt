package no.nordicsemi.nrf.matter.ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import no.nordicsemi.nrf.matter.domain.DeviceOfflineException
import no.nordicsemi.nrf.matter.domain.OperationResult
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.model.Device

private const val GENERIC_ERROR_MESSAGE = "Error during executing operation."

/**
 * Resolves a user-facing error message for [this] throwable, mapping a [DeviceOfflineException]
 * to its "device is offline" message and falling back to a generic message otherwise.
 */
internal fun Throwable.toUiErrorMessage(): String =
    (this as? DeviceOfflineException)?.message ?: GENERIC_ERROR_MESSAGE

interface CommandHandler {
    fun resolveEndpoint(device: Device, clusterId: Long): Int {
        return device.deviceMatterInfo
            .firstOrNull { it.serverClusters.contains(clusterId) }
            ?.endpoint ?: 0 // TODO: change to exception and handle from UI.
    }

    fun <T> Flow<T>.withUiState(): Flow<UiState<T>> {
        return this
            .map<T, UiState<T>> { UiState.Success(it) }
            .onStart { emit(UiState.Loading()) }
            .catch { emit(UiState.Error(it.toUiErrorMessage(), it)) }
    }

    /**
     * For flows that already report their own success/error per emission (e.g. a device
     * observation flow), rather than throwing to signal failure.
     */
    fun <T> Flow<OperationResult<T>>.toUiState(): Flow<UiState<T>> {
        return this
            .map<OperationResult<T>, UiState<T>> {
                when (it) {
                    is OperationResult.Success -> UiState.Success(it.data)
                    is OperationResult.Error -> UiState.Error(it.t.toUiErrorMessage(), it.t)
                }
            }
            .onStart { emit(UiState.Loading()) }
            .catch { emit(UiState.Error(it.toUiErrorMessage(), it)) }
    }

    fun <T> withUiState(block: suspend () -> T): Flow<UiState<T>> {
        return flow {
            try {
                emit(UiState.Loading())
                emit(UiState.Success(block()))
            } catch (t: Throwable) {
                t.printStackTrace()
                emit(UiState.Error(t.toUiErrorMessage(), t))
            }
        }
    }
}
