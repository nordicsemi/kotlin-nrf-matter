package no.nordicsemi.nrf.matter.nordic

import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.model.Device

const val NORDIC_MANUFACTURER_SPECIFIC_DEVICE_TYPE: Long = 0xFFF10001

fun Device.isNordicManufacturerSpecific(): Boolean =
    endpoints.any { !it.isRoot && NORDIC_MANUFACTURER_SPECIFIC_DEVICE_TYPE in it.types }

fun registerNordicClusters() {
    NordicMatters.registerCustomCluster(
        clusterId = ManufacturerSpecClusterInfo.ID,
        deviceType = NORDIC_MANUFACTURER_SPECIFIC_DEVICE_TYPE,
        factory = ::ManufacturerSpecCluster,
    )
    NordicMatters.registerCustomCluster(
        clusterId = BasicInfoExtClusterInfo.ID,
        deviceType = NORDIC_MANUFACTURER_SPECIFIC_DEVICE_TYPE,
        factory = ::BasicInfoExtCluster,
    )
}
