package no.nordicsemi.nrf.matter.controller

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import no.nordicsemi.nrf.matter.controller.MatterDoorLockController
import no.nordicsemi.nrf.matter.matter.AttributeSubscriber
import no.nordicsemi.nrf.matter.matter.CommandExecutor
import no.nordicsemi.nrf.matter.matter.IntAttributeParser
import no.nordicsemi.nrf.matter.matter.StandardClusterIds
import no.nordicsemi.nrf.matter.matter.nsNumber
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.LockDeviceState
import platform.Foundation.NSNumber

/**
 * Mirrors `iosApp/iosApp/kotlin/local/LocalMatterDoorController.swift`, using raw
 * `MTRBaseDevice` calls instead of the typed `MTRBaseCluster*` wrapper classes (see
 * [StandardClusterIds]).
 */
@OptIn(ExperimentalForeignApi::class)
internal class IosMatterDoorLockController : MatterDoorLockController {

    override suspend fun lockUnlockDoor(deviceId: DeviceId, isLocked: Boolean, endpoint: Int) {
        CommandExecutor(deviceId.nsNumber()).executeCommand(
            endpoint = NSNumber(int = endpoint),
            cluster = NSNumber(int = StandardClusterIds.DoorLock.CLUSTER),
            command = NSNumber(
                int = if (isLocked) StandardClusterIds.DoorLock.CMD_LOCK_DOOR else StandardClusterIds.DoorLock.CMD_UNLOCK_DOOR,
            ),
        )
    }

    override suspend fun observeLockState(deviceId: DeviceId, endpoint: Int): Flow<LockDeviceState> {
        val flow = MutableSharedFlow<LockDeviceState>(replay = 1, extraBufferCapacity = 8)
        AttributeSubscriber(deviceId.nsNumber()).subscribe(
            endpoint = NSNumber(int = endpoint),
            cluster = NSNumber(int = StandardClusterIds.DoorLock.CLUSTER),
            attribute = NSNumber(int = StandardClusterIds.DoorLock.ATTR_LOCK_STATE),
            parser = IntAttributeParser,
            onUpdate = { raw -> flow.tryEmit(LockDeviceState.create(raw)) },
        )
        return flow
    }
}
