package no.nordicsemi.nrf.matter.adapters

import kotlinx.coroutines.CancellableContinuation
import no.nordicsemi.nrf.matter.commission.toCommissioningException
import no.nordicsemi.nrf.matter.logger.NordicLogger
import platform.Foundation.NSError
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun <T> CancellableContinuation<T>.handleResult(error: NSError?, result: T? = null) {
    NordicLogger.debug("Handle operation result: $error, $result")
    val commissioningException = error?.toCommissioningException()

    if (commissioningException != null) {
        resumeWithException(commissioningException)
    } else if (error != null) {
        resumeWithException(IOSException(error))
    } else if (result != null) {
        resume(result)
    } else {
        // Can occur for successful operations without a result.
        @Suppress("UNCHECKED_CAST")
        resume(Unit as T)
    }
}

/**
 * Resumes with [result] on success, mapping [error] to an exception the same way [handleResult]
 * does.
 *
 * Separate from [handleResult] because that one reads a `null` result as "this operation has no
 * result" and resumes with `Unit`, which fails for any `T` that is not `Unit`. Use this one where
 * `null` is a legitimate success value.
 */
fun <T> CancellableContinuation<T>.handleNullableResult(error: NSError?, result: T) {
    NordicLogger.debug("Handle operation result: $error, $result")
    val commissioningException = error?.toCommissioningException()

    when {
        commissioningException != null -> resumeWithException(commissioningException)
        error != null -> resumeWithException(IOSException(error))
        else -> resume(result)
    }
}
