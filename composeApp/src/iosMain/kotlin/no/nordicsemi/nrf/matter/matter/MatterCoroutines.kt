package no.nordicsemi.nrf.matter.matter

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_queue_t
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class MatterCallException(val nsError: NSError) : Exception(nsError.localizedDescription)

/**
 * The completion-handler-based `MTR*` selectors (e.g. `MTRDeviceResponseHandler`) are what
 * Kotlin/Native cinterop exposes — unlike Swift, it does not auto-generate `suspend`/`async`
 * overloads over them. This wraps them into a suspend call; the `core` interfaces are already
 * `suspend fun`, so this is purely an implementation detail.
 */
@OptIn(ExperimentalForeignApi::class)
internal suspend fun mtrCall(action: ((List<*>?, NSError?) -> Unit) -> Unit): List<*>? =
    suspendCancellableCoroutine { cont ->
        action { values, error ->
            if (error != null) {
                cont.resumeWithException(MatterCallException(error))
            } else {
                cont.resume(values)
            }
        }
    }

@OptIn(ExperimentalForeignApi::class)
internal fun defaultMatterQueue(): dispatch_queue_t =
    dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u)
