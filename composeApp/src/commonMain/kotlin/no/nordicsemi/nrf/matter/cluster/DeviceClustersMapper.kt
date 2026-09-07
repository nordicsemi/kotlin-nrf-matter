package no.nordicsemi.nrf.matter.cluster

import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceType
import no.nordicsemi.nrf.matter.model.deviceTypes

/**
 * The clusters of this device that the library has a [Cluster] for.
 *
 * The [MatterClient] each one reads and writes through comes from the library's own graph, so a
 * caller never has to hold one.
 */
fun Device.toClusters(): List<Cluster> {
    val client = NordicMatters.matterClient

    val supported = endpoints.flatMap { endpoint ->
        endpoint.serverClusters.mapNotNull { clusterId ->
            when (clusterId) {
                OnOffClusterInfo.ID -> OnOffCluster(deviceId, endpoint.id, client)
                LevelControlClusterInfo.ID -> LevelControlCluster(deviceId, endpoint.id, client)
                DoorLockClusterInfo.ID -> DoorLockCluster(deviceId, endpoint.id, client)
                ManufacturerSpecClusterInfo.ID -> ManufacturerSpecCluster(
                    deviceId,
                    endpoint.id,
                    client
                )

                else -> null
            }
        }
    }

    return supported + basicInfoExtensions(client)
}

private fun Device.basicInfoExtensions(client: MatterClient): List<Cluster> {
    val isManufacturerSpecificDevice = endpoints
        .deviceTypes()
        .any { it == DeviceType.MANUFACTURER_SPECIFIC_DEVICE }

    if (!isManufacturerSpecificDevice) return emptyList()

    return listOf(BasicInfoExtCluster(deviceId, client))
}
