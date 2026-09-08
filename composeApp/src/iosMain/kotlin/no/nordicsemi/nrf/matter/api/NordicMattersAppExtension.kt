@file:OptIn(ExperimentalForeignApi::class)

package no.nordicsemi.nrf.matter.api

import iosMatter.MatterCommissioner
import iosMatter.SharedConsts
import iosMatter.SharedStorage
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.adapters.IOSException
import no.nordicsemi.nrf.matter.adapters.IOSLoggerImpl
import no.nordicsemi.nrf.matter.adapters.handleResult
import no.nordicsemi.nrf.matter.adapters.toDeviceId
import no.nordicsemi.nrf.matter.commission.CommissioningException
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId

// iOS-only entry points for the Matter "Add Device" system extension (`RequestHandler` in the
// iosApp target).
//
// The extension runs in its own process, separate from the main app, so it talks to the native
// Matter extension bridge (`MatterCommissioner`) and to `SharedStorage` (`UserDefaults` over an app
// group) directly — neither is used by the main app's own commissioning flow, which goes through
// `commission/CommissioningTask.kt` instead. Both natives are process-wide, so one instance of each
// covers the extension's whole lifetime.
//
// These are added as extensions on `NordicMatters` and `Fabric` rather than a standalone class so
// that `RequestHandler` can drive the same shared fabric the rest of the app uses, instead of a
// private copy of the commissioning logic.

private const val TAG = "AppExtension"

private val appExtensionCommissioner = MatterCommissioner()
private val appExtensionStorage = SharedStorage()

/**
 * Sets up Kotlin-side logging for the app extension process.
 *
 * Call this once — e.g. right after creating `RequestHandler` — before any other call in this
 * file; every process running Kotlin code has to install its own logger.
 */
fun NordicMatters.initializeAppExtension() {
    NordicLogger.setLogger(IOSLoggerImpl())
}

/**
 * The rooms offered by the "Add Device" app extension.
 *
 * The extension's UI runs ahead of the rest of the app, so — unlike the app's own room picker —
 * it works from a fixed list rather than one backed by a [Fabric].
 */
fun NordicMatters.appExtensionRooms(): List<String> {
    NordicLogger.info("Getting rooms.", tag = TAG)
    return listOf("Living Room", "Bedroom", "Office", "Kitchen", "Dining Room")
}

/**
 * Logs the Thread networks found while scanning for the new device's network during the
 * "Add Device" extension flow.
 */
fun NordicMatters.onAppExtensionThreadNetworksDetected(names: List<String>) {
    NordicLogger.info("Selecting Thread network from ${names.size} scan results", tag = TAG)

    names.forEach {
        NordicLogger.info("Detected thread network: $it.", tag = TAG)
    }
}

/**
 * Commissions onto this [Fabric] the device named by the node ID the main app wrote to shared
 * storage before starting the "Add Device" extension flow, using [payload] read from the
 * commissioning QR code.
 *
 * Returns the commissioned [DeviceId] so [configureAppExtensionDevice] can register it once the
 * user has chosen its name — declared with [Throws] so that a failure is handed to the extension
 * as an `NSError` instead of an unhandled Kotlin exception, which would take the extension process
 * down and leave the system add-device flow waiting forever.
 */
@Throws(
    CommissioningException::class,
    IOSException::class,
    IllegalStateException::class,
)
suspend fun Fabric.commissionAppExtensionDevice(payload: String): DeviceId {
    NordicLogger.info("Commission Matter device with payload: $payload", tag = TAG)

    val nodeId = appExtensionStorage.getNumberWithKey(SharedConsts.nodeIdKey)
        ?: error("No node ID found in shared storage.")
    val deviceId = nodeId.toDeviceId()

    suspendCancellableCoroutine<Unit> { continuation ->
        appExtensionCommissioner.commissionWithPayload(payload, nodeId) { error ->
            continuation.handleResult(error)
        }
    }

    return deviceId
}

/**
 * Finishes configuring [deviceId] with its chosen [name]: registers it with this [Fabric] so the
 * main app's device list picks it up, then records the success flag the main app reads once the
 * extension closes, and releases the native Matter controller.
 *
 * Registration failures are logged, not thrown: the device is already commissioned onto the fabric
 * by this point, so failing to read it back here shouldn't be reported to the system as a failed
 * "Add Device" flow. The app can still pick it up on its next sync.
 */
suspend fun Fabric.configureAppExtensionDevice(deviceId: DeviceId, name: String?) {
    try {
        NordicMatters.matterDependencies.finaliseCommissioningUseCase.rememberName(deviceId, name)
        commissionDevice(deviceId)
        NordicLogger.info("Registered device $deviceId with NordicMatters.", tag = TAG)
    } catch (t: Throwable) {
        NordicLogger.error(
            "Failed to register device $deviceId with NordicMatters: ${t.message}",
            t,
            tag = TAG,
        )
    }

    NordicLogger.info(
        "Device configured. Storing result and releasing commissioner...",
        tag = TAG,
    )
    appExtensionStorage.storeBoolWithKey(SharedConsts.resultKey, true)
    appExtensionCommissioner.releaseCommissioner()
}
