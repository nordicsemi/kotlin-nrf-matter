package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

object DoorLockClusterInfo {
    const val ID: Long = 0x0101

    object Attribute {
        const val LOCK_STATE: Long = 0x0000
    }

    object Command {
        const val LOCK: Long = 0x00
        const val UNLOCK: Long = 0x01
    }
}

class DoorLockCluster(
    override val deviceId: DeviceId,
    override val endpoint: Int,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = DoorLockClusterInfo.ID

    /**
     * Locks or unlocks the door. The optional PIN code field is never sent.
     */
    suspend fun setLocked(isLocked: Boolean) {
        executeCommand(
            commandId = if (isLocked) DoorLockClusterInfo.Command.LOCK else DoorLockClusterInfo.Command.UNLOCK,
            timedInvokeTimeoutMs = 10_000,
        )
    }

    /** Emits the raw LockState value, see [no.nordicsemi.nrf.matter.model.LockDeviceState]. */
    fun observeLockState(): Flow<Number> =
        observeAttribute(DoorLockClusterInfo.Attribute.LOCK_STATE)
}
