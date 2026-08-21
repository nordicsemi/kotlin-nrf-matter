package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

object OnOffClusterInfo {
    const val ID: Long = 0x0006

    object Attribute {
        const val ON_OFF: Long = 0x0000
    }
}

class OnOffCluster(
    override val deviceId: DeviceId,
    override val endpoint: Int,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = OnOffClusterInfo.ID

    /** Turns the device on or off. The OnOff attribute itself is read only, hence the commands. */
    suspend fun setOn(isOn: Boolean) {
        executeCommand(commandId = if (isOn) ON_COMMAND_ID else OFF_COMMAND_ID)
    }

    fun observeOnOff(): Flow<Boolean> = observeAttribute(OnOffClusterInfo.Attribute.ON_OFF)

    companion object {
        private const val OFF_COMMAND_ID: Long = 0x00
        private const val ON_COMMAND_ID: Long = 0x01
    }
}
