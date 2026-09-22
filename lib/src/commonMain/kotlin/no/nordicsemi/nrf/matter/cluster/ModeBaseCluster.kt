package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow

data class ModeOption(val label: String, val mode: Int, val modeTags: List<Int>)

// The RVC Run Mode cluster's standard "Cleaning" mode tag (Matter spec, Mode Tags table). Every
// compliant device exposes at least one mode carrying this tag, used to start a cleaning run.
const val CLEANING_MODE_TAG: Int = 0x4001

fun List<ModeOption>.cleaningMode(): ModeOption? = firstOrNull { CLEANING_MODE_TAG in it.modeTags }

object ModeOptionStruct {
    const val LABEL: Long = 0
    const val MODE: Long = 1
    const val MODE_TAGS: Long = 2
}

object ModeTagStruct {
    const val VALUE: Long = 1
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
                val modeTags = (struct[ModeOptionStruct.MODE_TAGS] as? List<*>)
                    .orEmpty()
                    .filterIsInstance<MatterStruct>()
                    .mapNotNull { it.longOrNull(ModeTagStruct.VALUE)?.toInt() }
                ModeOption(label, mode.toInt(), modeTags)
            }
}
