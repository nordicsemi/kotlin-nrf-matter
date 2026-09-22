package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

object RvcOperationalStateClusterInfo {
    const val ID: Long = 0x0061

    object Attribute {
        const val CURRENT_PHASE: Long = 0x0001
        const val COUNTDOWN_TIME: Long = 0x0002
        const val OPERATIONAL_STATE: Long = 0x0004
        const val OPERATIONAL_ERROR: Long = 0x0005
    }

    object Command {
        const val PAUSE: Long = 0x00
        const val RESUME: Long = 0x03
        const val GO_HOME: Long = 0x80
    }

    object ErrorStateStruct {
        const val ERROR_STATE_ID: Long = 0
    }
}

class RvcOperationalStateCluster(
    override val deviceId: DeviceId,
    override val endpoint: Int,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = RvcOperationalStateClusterInfo.ID

    suspend fun pause() = executeCommand(commandId = RvcOperationalStateClusterInfo.Command.PAUSE)
    suspend fun resume() = executeCommand(commandId = RvcOperationalStateClusterInfo.Command.RESUME)
    suspend fun goHome() = executeCommand(commandId = RvcOperationalStateClusterInfo.Command.GO_HOME)

    fun observeOperationalState(): Flow<Number> =
        observeAttribute(RvcOperationalStateClusterInfo.Attribute.OPERATIONAL_STATE)

    fun observeCurrentPhase(): Flow<Number?> =
        observeAttribute(RvcOperationalStateClusterInfo.Attribute.CURRENT_PHASE)

    fun observeCountdownTime(): Flow<Number?> =
        observeAttribute(RvcOperationalStateClusterInfo.Attribute.COUNTDOWN_TIME)

    suspend fun operationalError(): Int? {
        val struct = readAttribute<MatterStruct?>(RvcOperationalStateClusterInfo.Attribute.OPERATIONAL_ERROR)
        return struct?.longOrNull(RvcOperationalStateClusterInfo.ErrorStateStruct.ERROR_STATE_ID)?.toInt()
    }
}
