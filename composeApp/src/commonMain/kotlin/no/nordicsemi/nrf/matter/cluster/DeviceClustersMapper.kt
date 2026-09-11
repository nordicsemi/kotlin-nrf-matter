package no.nordicsemi.nrf.matter.cluster

import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceType

fun Device.toClusters(): List<Cluster> {
    val device = this
    val client = NordicMatters.matterClient

    return endpoints.flatMap { endpoint ->
        endpoint.serverClusters.mapNotNull { clusterId ->
            when (clusterId) {
                OnOffClusterInfo.ID -> OnOffCluster(deviceId, endpoint.id, client)
                LevelControlClusterInfo.ID -> LevelControlCluster(deviceId, endpoint.id, client)
                DoorLockClusterInfo.ID -> DoorLockCluster(deviceId, endpoint.id, client)

                else -> NordicMatters.getCustomClusters()[clusterId]?.let { factory ->
                    factory.first?.let { deviceTypeId ->
                        val deviceType = DeviceType.parse(deviceTypeId)
                        factory.second
                            .takeIf { deviceType != DeviceType.UNSUPPORTED }
                            .takeIf { device.deviceType == deviceType }
                            ?.invoke(deviceId, endpoint.id, client)
                    } ?: factory.second.invoke(deviceId, endpoint.id, client)
                }
            }
        }
    }
}
