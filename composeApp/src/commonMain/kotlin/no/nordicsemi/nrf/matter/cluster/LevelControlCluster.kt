package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

object LevelControlClusterInfo {
    const val ID: Long = 0x0008

    object Attribute {
        const val CURRENT_LEVEL: Long = 0x0000
    }

    object Command {
        const val MOVE_TO_LEVEL: Long = 0x0000
    }
}

class LevelControlCluster(
    override val deviceId: DeviceId,
    override val endpoint: Int,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = LevelControlClusterInfo.ID

    /**
     * Sets the raw device level.
     */
    suspend fun setLevel(level: Int) {
        executeCommand(
            commandId = LevelControlClusterInfo.Command.MOVE_TO_LEVEL,
            value = level.toUByte()
        )
    }

    /** Emits the raw device level. */
    fun observeLevel(): Flow<Number> =
        observeAttribute(LevelControlClusterInfo.Attribute.CURRENT_LEVEL)
}
