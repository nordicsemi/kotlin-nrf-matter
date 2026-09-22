package no.nordicsemi.nrf.matter.cluster

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
) : ModeBaseCluster(controller) {

    override val id: Long = RvcCleanModeClusterInfo.ID
    override val supportedModesAttribute: Long = RvcCleanModeClusterInfo.Attribute.SUPPORTED_MODES
    override val currentModeAttribute: Long = RvcCleanModeClusterInfo.Attribute.CURRENT_MODE
    override val changeToModeCommand: Long = RvcCleanModeClusterInfo.Command.CHANGE_TO_MODE
}
