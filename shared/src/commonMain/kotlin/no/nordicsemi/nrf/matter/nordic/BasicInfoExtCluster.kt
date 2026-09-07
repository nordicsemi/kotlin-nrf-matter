package no.nordicsemi.nrf.matter.nordic

import no.nordicsemi.nrf.matter.cluster.BasicInfoClusterInfo
import no.nordicsemi.nrf.matter.cluster.Cluster
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.model.DeviceId

object BasicInfoExtClusterInfo {

    const val ID: Long = BasicInfoClusterInfo.ID

    object Attribute {
        const val RANDOM_NUMBER: Long = 0x17
    }

    object Command {
        const val GENERATE_RANDOM_NUMBER: Long = 0x00
    }
}

class BasicInfoExtCluster(
    override val deviceId: DeviceId,
    override val endpoint: Int,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = BasicInfoExtClusterInfo.ID

    suspend fun generateRandomNumber(): Long {
        executeCommand(commandId = BasicInfoExtClusterInfo.Command.GENERATE_RANDOM_NUMBER)
        return readAttribute<Number>(BasicInfoExtClusterInfo.Attribute.RANDOM_NUMBER).toLong()
    }
}
