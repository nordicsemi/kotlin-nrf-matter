package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

object RvcCleanModeClusterInfo {
    const val ID: Long = 0x0055

    object Attribute {
        const val SUPPORTED_MODES: Long = 0x0000
        const val CURRENT_MODE: Long = 0x0001
    }

    object Command {
        const val CHANGE_TO_MODE: Long = 0x0000
    }
}

class RvcCleanModeCluster(
    override val deviceId: DeviceId,
    override val endpoint: Int,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = RvcCleanModeClusterInfo.ID

    suspend fun changeToMode(mode: Int) {
        executeCommand(commandId = RvcCleanModeClusterInfo.Command.CHANGE_TO_MODE, value = mode.toUByte())
    }

    fun observeCurrentMode(): Flow<Number> =
        observeAttribute(RvcCleanModeClusterInfo.Attribute.CURRENT_MODE)

    suspend fun supportedModes(): List<ModeOption> =
        readAttribute<List<*>?>(RvcCleanModeClusterInfo.Attribute.SUPPORTED_MODES).toModeOptions()
}
