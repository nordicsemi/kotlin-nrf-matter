package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

object ContactSensorClusterInfo {
    const val ID: Long = 0x0045

    object Attribute {
        const val STATE_VALUE: Long = 0x0000
    }
}

class ContactSensorCluster(
    deviceId: DeviceId,
    endpoint: Int,
    controller: MatterClient,
) : Cluster(deviceId, endpoint, ContactSensorClusterInfo.ID, controller) {

    fun observeStateValue(): Flow<Boolean> = observeAttribute(ContactSensorClusterInfo.Attribute.STATE_VALUE)
}
