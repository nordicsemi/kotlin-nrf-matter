package no.nordicsemi.nrf.matter.iosdeps

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.MatterCommissioner
import no.nordicsemi.nrf.matter.SwiftCodeProvider
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.MatterClusterExtensionController
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.controller.MatterDoorLockController
import no.nordicsemi.nrf.matter.controller.MatterLightController
import no.nordicsemi.nrf.matter.controller.MatterManufacturerSpecificController
import no.nordicsemi.nrf.matter.device.OperationResult
import no.nordicsemi.nrf.matter.logger.IOSLogger
import no.nordicsemi.nrf.matter.logger.LogEntity
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.LockDeviceState
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Adapters wrapping iosDeps's cinterop-bound Swift objects behind `:core`'s real interfaces.
 *
 * This is the seam described in composeApp/build.gradle.kts: iosDeps's classes are constructed
 * directly here (via cinterop), and every method call converts primitives/DTOs to and from
 * `:core`'s compiled types (see IosDepsConversions.kt).
 */
@OptIn(ExperimentalForeignApi::class)
private class IosDepsMatterLightController(
    private val delegate: LocalMatterLightController
) : MatterLightController {

    override suspend fun setBrightnessLevel(deviceId: DeviceId, brightnessLevel: Int, endpoint: Int) {
        suspendCancellableCoroutine { cont ->
            delegate.setBrightnessLevelWithDeviceId(deviceId.stringValue, brightnessLevel, endpoint) { error ->
                if (error != null) cont.resumeWithException(Throwable(error.localizedDescription)) else cont.resume(Unit)
            }
        }
    }

    override suspend fun setDeviceOnOff(deviceId: DeviceId, isOn: Boolean, endpoint: Int) {
        suspendCancellableCoroutine { cont ->
            delegate.setDeviceOnOffWithDeviceId(deviceId.stringValue, isOn, endpoint) { error ->
                if (error != null) cont.resumeWithException(Throwable(error.localizedDescription)) else cont.resume(Unit)
            }
        }
    }

    override suspend fun observeLightState(deviceId: DeviceId, endpoint: Int): Flow<Boolean> = callbackFlow {
        delegate.observeLightStateWithDeviceId(deviceId.stringValue, endpoint, onValue = { trySend(it) }) { error ->
            if (error != null) close(Throwable(error.localizedDescription))
        }
        awaitClose { }
    }

    override suspend fun observeBrightnessState(deviceId: DeviceId, endpoint: Int): Flow<Float> = callbackFlow {
        delegate.observeBrightnessStateWithDeviceId(deviceId.stringValue, endpoint, onValue = { trySend(it) }) { error ->
            if (error != null) close(Throwable(error.localizedDescription))
        }
        awaitClose { }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class IosDepsMatterDoorLockController(
    private val delegate: LocalMatterDoorController
) : MatterDoorLockController {

    override suspend fun lockUnlockDoor(deviceId: DeviceId, isLocked: Boolean, endpoint: Int) {
        suspendCancellableCoroutine { cont ->
            delegate.lockUnlockDoorWithDeviceId(deviceId.stringValue, isLocked, endpoint) { error ->
                if (error != null) cont.resumeWithException(Throwable(error.localizedDescription)) else cont.resume(Unit)
            }
        }
    }

    override suspend fun observeLockState(deviceId: DeviceId, endpoint: Int): Flow<LockDeviceState> = callbackFlow {
        delegate.observeLockStateWithDeviceId(deviceId.stringValue, endpoint, onValue = { trySend(LockDeviceState.create(it)) }) { error ->
            if (error != null) close(Throwable(error.localizedDescription))
        }
        awaitClose { }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class IosDepsMatterManufacturerSpecificController(
    private val delegate: LocalMatterCustomClusterController
) : MatterManufacturerSpecificController {

    override suspend fun setLed(deviceId: DeviceId, isOn: Boolean, endpoint: Int) {
        suspendCancellableCoroutine { cont ->
            delegate.setLedWithDeviceId(deviceId.stringValue, isOn, endpoint) { error ->
                if (error != null) cont.resumeWithException(Throwable(error.localizedDescription)) else cont.resume(Unit)
            }
        }
    }

    override fun observeButtonChanges(deviceId: DeviceId, endpoint: Int): Flow<Boolean> = callbackFlow {
        delegate.observeButtonChangesWithDeviceId(deviceId.stringValue, endpoint) { trySend(it) }
        awaitClose { }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class IosDepsMatterClusterExtensionController(
    private val delegate: LocalMatterClusterExtController
) : MatterClusterExtensionController {

    override suspend fun generateRandomNumber(deviceId: DeviceId, endpoint: Int): Long? =
        suspendCancellableCoroutine { cont ->
            delegate.generateRandomNumberWithDeviceId(deviceId.stringValue, endpoint) { number, error ->
                if (error != null) cont.resumeWithException(Throwable(error.localizedDescription)) else cont.resume(number?.longValue)
            }
        }
}

@OptIn(ExperimentalForeignApi::class)
private class IosDepsMatterDecommissioner(
    private val delegate: LocalMatterDecommissioner
) : MatterDecommissioner {

    override suspend fun decommission(deviceId: DeviceId) {
        suspendCancellableCoroutine { cont ->
            delegate.decommissionWithDeviceId(deviceId.stringValue) { error ->
                if (error != null) cont.resumeWithException(Throwable(error.localizedDescription)) else cont.resume(Unit)
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class IosDepsBindingController(
    private val delegate: LocalMatterBinder
) : BindingController {

    override suspend fun bind(sourceNodeId: DeviceId, sourceEndpoint: Int, targetNodeId: DeviceId, targetEndpoint: Int, clusterId: Long) {
        suspendCancellableCoroutine { cont ->
            delegate.bindWithSourceNodeId(
                sourceNodeId.stringValue, sourceEndpoint, targetNodeId.stringValue, targetEndpoint, clusterId
            ) { error ->
                if (error != null) cont.resumeWithException(Throwable(error.localizedDescription)) else cont.resume(Unit)
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class IosDepsMatterCommissioner(
    private val delegate: LocalMatterCommissioner
) : MatterCommissioner {

    override suspend fun startIosCommissioning(deviceId: DeviceId): OperationResult<Device> =
        suspendCancellableCoroutine { cont ->
            delegate.startIosCommissioningWithDeviceId(deviceId.stringValue) { device, error ->
                val commissioningError = error as? SwiftCommissioningError
                when {
                    device != null -> cont.resume(OperationResult.Success(device.toCore()))
                    commissioningError != null -> cont.resume(OperationResult.Error(commissioningError.toCore(deviceId)))
                    error != null -> cont.resume(OperationResult.Error(Throwable(error.localizedDescription)))
                    else -> cont.resumeWithException(IllegalStateException("Commissioning returned neither a device nor an error"))
                }
            }
        }
}

@OptIn(ExperimentalForeignApi::class)
private class IosDepsLogger(
    private val delegate: IOSLoggerImpl
) : IOSLogger() {

    init {
        delegate.setOnLogLine { line -> line?.let { logsChannel.trySend(it) } }
    }

    override fun getLogs(onReady: (List<LogEntity>) -> Unit) {
        delegate.getLogsOnReady { entries -> onReady(entries.orEmpty().map { (it as SwiftLogEntity).toCore() }) }
    }

    override fun info(tag: String, message: String) = delegate.infoWithTag(tag, message)

    override fun debug(tag: String, message: String) = delegate.debugWithTag(tag, message)

    override fun error(tag: String, message: String) = delegate.errorWithTag(tag, message)
}

/**
 * Constructs every native Matter implementation via cinterop and wraps it behind `:core`'s
 * interfaces, so the rest of composeApp never needs to know iosDeps exists.
 */
@OptIn(ExperimentalForeignApi::class)
internal fun createSwiftCodeProvider(): SwiftCodeProvider = object : SwiftCodeProvider {
    override fun getMatterCommissioner(): MatterCommissioner = IosDepsMatterCommissioner(LocalMatterCommissioner())
    override fun getMatterOnOffController(): MatterLightController = IosDepsMatterLightController(LocalMatterLightController())
    override fun getDecommissioner(): MatterDecommissioner = IosDepsMatterDecommissioner(LocalMatterDecommissioner())
    override fun getMatterBinder(): BindingController = IosDepsBindingController(LocalMatterBinder())
    override fun getMatterDoorController(): MatterDoorLockController = IosDepsMatterDoorLockController(LocalMatterDoorController())
    override fun getMatterManufacturerCustomDataController(): MatterManufacturerSpecificController =
        IosDepsMatterManufacturerSpecificController(LocalMatterCustomClusterController())
    override fun getMatterClusterExtensionController(): MatterClusterExtensionController =
        IosDepsMatterClusterExtensionController(LocalMatterClusterExtController())
    override fun getLogger(): IOSLogger = IosDepsLogger(IOSLoggerImpl())
}
