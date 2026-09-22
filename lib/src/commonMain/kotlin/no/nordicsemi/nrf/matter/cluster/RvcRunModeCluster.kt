package no.nordicsemi.nrf.matter.cluster

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
) : ModeBaseCluster(controller) {

    override val id: Long = RvcRunModeClusterInfo.ID
    override val supportedModesAttribute: Long = RvcRunModeClusterInfo.Attribute.SUPPORTED_MODES
    override val currentModeAttribute: Long = RvcRunModeClusterInfo.Attribute.CURRENT_MODE
    override val changeToModeCommand: Long = RvcRunModeClusterInfo.Command.CHANGE_TO_MODE
}
