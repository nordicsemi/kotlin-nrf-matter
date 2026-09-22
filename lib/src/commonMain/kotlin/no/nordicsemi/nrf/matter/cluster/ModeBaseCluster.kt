package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow

data class ModeOption(val label: String, val mode: Int)

object ModeOptionStruct {
    const val LABEL: Long = 0
    const val MODE: Long = 1
}

abstract class ModeBaseCluster(controller: MatterClient) : Cluster(controller) {

    protected abstract val supportedModesAttribute: Long
    protected abstract val currentModeAttribute: Long
    protected abstract val changeToModeCommand: Long

    suspend fun changeToMode(mode: Int) {
        executeCommand(commandId = changeToModeCommand, value = mode.toUByte())
    }

    fun observeCurrentMode(): Flow<Number> = observeAttribute(currentModeAttribute)

    suspend fun supportedModes(): List<ModeOption> =
        readAttribute<List<*>?>(supportedModesAttribute)
            .orEmpty()
            .filterIsInstance<MatterStruct>()
            .mapNotNull { struct ->
                val mode = struct.longOrNull(ModeOptionStruct.MODE) ?: return@mapNotNull null
                val label = struct[ModeOptionStruct.LABEL] as? String ?: return@mapNotNull null
                ModeOption(label, mode.toInt())
            }
}
