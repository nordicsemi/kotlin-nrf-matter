package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

object RvcRunModeClusterInfo {
    const val ID: Long = 0x0054

    object Attribute {
        const val SUPPORTED_MODES: Long = 0x0000
        const val CURRENT_MODE: Long = 0x0001
    }

    object Command {
        const val CHANGE_TO_MODE: Long = 0x0000
    }
}

class RvcRunModeCluster(
    override val deviceId: DeviceId,
    override val endpoint: Int,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = RvcRunModeClusterInfo.ID

    suspend fun changeToMode(mode: Int) {
        executeCommand(commandId = RvcRunModeClusterInfo.Command.CHANGE_TO_MODE, value = mode.toUByte())
    }

    fun observeCurrentMode(): Flow<Number> =
        observeAttribute(RvcRunModeClusterInfo.Attribute.CURRENT_MODE)

    suspend fun supportedModes(): List<ModeOption> =
        readAttribute<List<*>?>(RvcRunModeClusterInfo.Attribute.SUPPORTED_MODES)
            .orEmpty()
            .filterIsInstance<MatterStruct>()
            .mapNotNull { struct ->
                val mode = struct.longOrNull(ModeOptionStruct.MODE) ?: return@mapNotNull null
                val label = struct[ModeOptionStruct.LABEL] as? String ?: return@mapNotNull null
                val modeTags = (struct[ModeOptionStruct.MODE_TAGS] as? List<*>)
                    .orEmpty()
                    .filterIsInstance<MatterStruct>()
                    .mapNotNull { it.longOrNull(ModeTagStruct.VALUE)?.toInt() }
                ModeOption(label, mode.toInt(), modeTags)
            }
}
