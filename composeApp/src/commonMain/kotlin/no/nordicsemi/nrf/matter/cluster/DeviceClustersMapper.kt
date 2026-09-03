package no.nordicsemi.nrf.matter.cluster

import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceType

/**
 * The clusters of this device that the library has a [Cluster] for.
 *
 * The [MatterClient] each one reads and writes through comes from the library's own graph, so a
 * caller never has to hold one.
 */
fun Device.toClusters(): List<Cluster> {
    val client = NordicMatters.matterClient

    val supported = deviceMatterInfo.flatMap { info ->
        info.serverClusters.mapNotNull { clusterId ->
            when (clusterId) {
                OnOffClusterInfo.ID -> OnOffCluster(deviceId, info.endpoint, client)
                LevelControlClusterInfo.ID -> LevelControlCluster(deviceId, info.endpoint, client)
                DoorLockClusterInfo.ID -> DoorLockCluster(deviceId, info.endpoint, client)
                ManufacturerSpecClusterInfo.ID -> ManufacturerSpecCluster(
                    deviceId,
                    info.endpoint,
                    client
                )

                else -> null
            }
        }
    }

    return supported + basicInfoExtensions(client)
}

private fun Device.basicInfoExtensions(client: MatterClient): List<Cluster> {
    val isManufacturerSpecificDevice = deviceMatterInfo.any {
        it.types
            .map { DeviceType.parse(it) }
            .any { it == DeviceType.MANUFACTURER_SPECIFIC_DEVICE }
    }

    if (!isManufacturerSpecificDevice) return emptyList()

    return listOf(BasicInfoExtCluster(deviceId, client))
}
