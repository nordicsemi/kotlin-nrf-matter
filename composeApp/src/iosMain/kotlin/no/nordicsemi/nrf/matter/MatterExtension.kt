@file:OptIn(ExperimentalForeignApi::class)

package no.nordicsemi.nrf.matter

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.iosdeps.IosAppInitializer
import no.nordicsemi.nrf.matter.iosdeps.IosExtensionCommissioner
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Entry point the iOS app + app extension consume through `import shared`.
 *
 * These functions wrap iosDeps's cinterop-bound Swift facades ([IosExtensionCommissioner],
 * [IosAppInitializer]) behind a primitives-only Kotlin API, so `iosApp`/`nrfMatter` never compile
 * `SharedCode`/`iosDeps` source directly — they call into the published library instead. The live
 * Matter controller/storage/keypair stay entirely inside the Swift layer (see
 * IosExtensionCommissioner); only `String`s and completion callbacks cross this boundary.
 */
object MatterExtension {

    private val commissioner = IosExtensionCommissioner()

    /**
     * Commissions the device described by the onboarding [payload] into the local fabric.
     *
     * The target node ID is read from shared storage inside the Swift layer.
     */
    suspend fun commissionDevice(payload: String) {
        suspendCancellableCoroutine { cont ->
            commissioner.commissionDeviceWithPayload(payload) { error ->
                if (error != null) {
                    cont.resumeWithException(Throwable(error.localizedDescription))
                } else {
                    cont.resume(Unit)
                }
            }
        }
    }

    /** Records that configuration completed successfully and releases the underlying controller. */
    fun finishConfigure() = commissioner.finishConfigure()

    /** The display names of the rooms a newly added device may be placed in. */
    @Suppress("UNCHECKED_CAST")
    fun roomNames(): List<String> = commissioner.roomNames() as List<String>

    /** Logs an info-level message through the shared logger. */
    fun log(message: String) = commissioner.log(message)
}

/**
 * App-launch initialization the main app consumes through `import shared`, replacing its former
 * direct call into `SharedCode`'s `KeypairInitializer`.
 */
object MatterAppInit {

    /** Clears any leftover keychain data on first launch after a fresh install. */
    fun prepareKeychain() = IosAppInitializer.initKeychain()
}
