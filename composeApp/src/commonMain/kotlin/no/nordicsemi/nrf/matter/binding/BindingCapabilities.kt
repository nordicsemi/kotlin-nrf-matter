package no.nordicsemi.nrf.matter.binding

import no.nordicsemi.nrf.matter.cluster.OnOffClusterInfo
import no.nordicsemi.nrf.matter.model.Device

fun Device.isBindingCapable(): Int? =
    endpoints.firstOrNull { OnOffClusterInfo.ID in it.clientClusters }?.id

fun Device.isBindingSource(): Int? =
    endpoints.firstOrNull { OnOffClusterInfo.ID in it.serverClusters }?.id
