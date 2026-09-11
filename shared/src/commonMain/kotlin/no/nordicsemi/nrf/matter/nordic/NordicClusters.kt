package no.nordicsemi.nrf.matter.nordic

import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceType

const val NORDIC_MANUFACTURER_SPECIFIC_DEVICE_TYPE: Long = 0xFFF10001
private val NordicDeviceType = DeviceType(NORDIC_MANUFACTURER_SPECIFIC_DEVICE_TYPE, "Nordic Semi Device")

fun Device.isNordicManufacturerSpecific(): Boolean =
    endpoints.any { !it.isRoot && NORDIC_MANUFACTURER_SPECIFIC_DEVICE_TYPE in it.types }

fun registerNordicClusters() {
    NordicMatters.registerCustomCluster(
        clusterId = ManufacturerSpecClusterInfo.ID,
        deviceType = NordicDeviceType,
        factory = ::ManufacturerSpecCluster,
    )
    NordicMatters.registerCustomCluster(
        clusterId = BasicInfoExtClusterInfo.ID,
        deviceType = NordicDeviceType,
        factory = ::BasicInfoExtCluster,
    )
}
