package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

object ManufacturerSpecClusterInfo {
    const val ID: Long = 0xFFF1FC01

    object Attribute {
        const val NAME: Long = 0xFFF10000
        const val LED: Long = 0xFFF10001
        const val BUTTON: Long = 0xFFF10002
    }

    object Command {
        const val SET_LET: Long = 0xFFF10000
    }
}

class ManufacturerSpecCluster(
    override val deviceId: DeviceId,
    override val endpoint: Int,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = ManufacturerSpecClusterInfo.ID

    suspend fun setLed(isOn: Boolean) {
        executeCommand(
            commandId = ManufacturerSpecClusterInfo.Command.SET_LET,
            value = if (isOn) ON_VALUE else OFF_VALUE
        )
    }

    fun observeLed(): Flow<Boolean> = observeAttribute(ManufacturerSpecClusterInfo.Attribute.LED)

    fun observeButton(): Flow<Boolean> =
        observeAttribute(ManufacturerSpecClusterInfo.Attribute.BUTTON)

    suspend fun readName(): String = readAttribute(ManufacturerSpecClusterInfo.Attribute.NAME)

    companion object {
        private val ON_VALUE: UByte = 1u
        private val OFF_VALUE: UByte = 0u
    }
}
