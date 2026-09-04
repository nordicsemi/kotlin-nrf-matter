package no.nordicsemi.nrf.matter.binding

import no.nordicsemi.nrf.matter.cluster.OnOffClusterInfo
import no.nordicsemi.nrf.matter.model.Device

fun Device.isBindingCapable(): Int? =
    deviceMatterInfo.firstOrNull { OnOffClusterInfo.ID in it.clientClusters }?.endpoint

fun Device.isBindingSource(): Int? =
    deviceMatterInfo.firstOrNull { OnOffClusterInfo.ID in it.serverClusters }?.endpoint
