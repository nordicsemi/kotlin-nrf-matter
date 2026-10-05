package no.nordicsemi.nrf.matter.nordic

import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceType

const val NORDIC_MANUFACTURER_SPECIFIC_DEVICE_TYPE: Long = 0xFFF10001
val NordicDeviceType = DeviceType(NORDIC_MANUFACTURER_SPECIFIC_DEVICE_TYPE, "Nordic Semi Device")

fun Device.isNordicManufacturerSpecific(): Boolean =
    endpoints.any { !it.isRoot && NORDIC_MANUFACTURER_SPECIFIC_DEVICE_TYPE in it.types }

fun registerNordicClusters() {
    NordicMatters.registerCustomCluster(
        clusterId = ManufacturerSpecClusterInfo.ID,
        clusterName = "Manufacturer Specific Cluster",
        deviceType = NordicDeviceType,
        factory = ::ManufacturerSpecCluster,
    )
    NordicMatters.registerCustomCluster(
        clusterId = BasicInfoExtClusterInfo.ID,
        clusterName = "Basic Information Cluster + ext",
        deviceType = NordicDeviceType,
        factory = ::BasicInfoExtCluster,
    )
}
