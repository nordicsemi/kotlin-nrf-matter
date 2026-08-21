package no.nordicsemi.nrf.matter.cluster

import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceType


fun Device.toClusters(client: MatterClient): List<Cluster> {
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
