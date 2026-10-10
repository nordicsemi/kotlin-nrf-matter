package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

object TemperatureMeasurementClusterInfo {
    const val ID: Long = 0x0402

    object Attribute {
        const val MEASURED_VALUE: Long = 0x0000
    }
}

class TemperatureMeasurementCluster(
    deviceId: DeviceId,
    endpoint: Int,
    controller: MatterClient,
) : Cluster(deviceId, endpoint, TemperatureMeasurementClusterInfo.ID, controller) {

    fun observeMeasuredValue(): Flow<Number> = observeAttribute(TemperatureMeasurementClusterInfo.Attribute.MEASURED_VALUE)
}
