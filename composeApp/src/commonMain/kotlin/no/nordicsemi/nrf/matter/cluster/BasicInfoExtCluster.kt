package no.nordicsemi.nrf.matter.cluster

import no.nordicsemi.nrf.matter.model.DeviceId

/**
 * Nordic's extension to the Basic Information cluster: the same cluster as
 * [BasicInformationCluster], with manufacturer specific attributes and commands beyond the ones
 * the Matter specification defines.
 */
class BasicInfoExtCluster(
    override val deviceId: DeviceId,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = BasicInfoClusterInfo.ID
    override val endpoint = 0

    /** Asks the device for a new random number and reads the generated value back. */
    suspend fun generateRandomNumber(): Long {
        executeCommand(commandId = BasicInfoClusterInfo.Command.GENERATE_RANDOM_NUMBER)
        return readAttribute<Number>(BasicInfoClusterInfo.Attribute.RANDOM_NUMBER).toLong()
    }
}
