package no.nordicsemi.nrf.matter.domain

sealed interface OperationResult<out T> {

    data class Success<T>(val data: T) : OperationResult<T>

    data class Error(
        val t: Exception
    ) : OperationResult<Nothing>
}
