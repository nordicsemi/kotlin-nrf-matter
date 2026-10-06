package no.nordicsemi.nrf.matter.cluster

import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.SupportedDeviceType
import no.nordicsemi.nrf.matter.model.deviceTypes

fun Device.toClusters(): List<Cluster> {
    val client = NordicMatters.matterClient
    val deviceTypes = endpoints.deviceTypes()

    return endpoints.flatMap { endpoint ->
        endpoint.serverClusters.mapNotNull { clusterId ->
            when (clusterId) {
                OnOffClusterInfo.ID -> OnOffCluster(deviceId, endpoint.id, client)
                LevelControlClusterInfo.ID -> LevelControlCluster(deviceId, endpoint.id, client)
                DoorLockClusterInfo.ID -> DoorLockCluster(deviceId, endpoint.id, client)
                ContactSensorClusterInfo.ID -> ContactSensorCluster(deviceId, endpoint.id, client)
                TemperatureMeasurementClusterInfo.ID -> TemperatureMeasurementCluster(deviceId, endpoint.id, client)
                RvcRunModeClusterInfo.ID -> RvcRunModeCluster(deviceId, endpoint.id, client)
                RvcCleanModeClusterInfo.ID -> RvcCleanModeCluster(deviceId, endpoint.id, client)
                RvcOperationalStateClusterInfo.ID -> RvcOperationalStateCluster(deviceId, endpoint.id, client)
                SmokeCoAlarmClusterInfo.ID -> SmokeCoAlarmCluster(deviceId, endpoint.id, client)

                else -> NordicMatters.getCustomClusters()[clusterId]
                    .takeIf { it?.deviceType != SupportedDeviceType.UNKNOWN.value }
                    .takeIf { it?.deviceType in deviceTypes }
                    ?.factory
                    ?.invoke(deviceId, endpoint.id, client)
            }
        }
    }
}
