package no.nordicsemi.nrf.matter.device

sealed interface OperationResult<out T> {

    data class Success<T>(val data: T) : OperationResult<T>

    data class Error(
        val t: Throwable
    ) : OperationResult<Nothing>
}
