package no.nordicsemi.nrf.matter.cluster

import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.model.Device

/**
 * The clusters of this device that the library has a [Cluster] for.
 *
 * The clusters the Matter specification defines are recognised here, by the ids an endpoint reports
 * in its server list. A vendor's own are not named in this `when` at all - the library does not know
 * them - but come from whatever the app registered through [NordicMatters.registerCustomCluster].
 *
 * The [MatterClient] each one reads and writes through comes from the library's own graph, so a
 * caller never has to hold one.
 */
fun Device.toClusters(): List<Cluster> {
    val client = NordicMatters.matterClient

    return endpoints.flatMap { endpoint ->
        endpoint.serverClusters.mapNotNull { clusterId ->
            when (clusterId) {
                OnOffClusterInfo.ID -> OnOffCluster(deviceId, endpoint.id, client)
                LevelControlClusterInfo.ID -> LevelControlCluster(deviceId, endpoint.id, client)
                DoorLockClusterInfo.ID -> DoorLockCluster(deviceId, endpoint.id, client)

                else -> NordicMatters.getCustomClusters()[clusterId]?.invoke(deviceId, endpoint.id, client)
            }
        }
    }
}
