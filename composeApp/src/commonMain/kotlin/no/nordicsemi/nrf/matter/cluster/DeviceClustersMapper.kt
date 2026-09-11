package no.nordicsemi.nrf.matter.cluster

import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.StandardDeviceType

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
                    factory.first?.let { customDeviceType ->
                        factory.second
                            .takeIf { customDeviceType != StandardDeviceType.UNSUPPORTED.value }
                            .takeIf { device.deviceType == customDeviceType }
                            ?.invoke(deviceId, endpoint.id, client)
                    }
                }
            }
        }
    }
}
