package no.nordicsemi.nrf.matter.controller

import kotlinx.cinterop.ExperimentalForeignApi
import no.nordicsemi.nrf.matter.commission.Stage
import no.nordicsemi.nrf.matter.domain.ManufacturerSpecificData
import no.nordicsemi.nrf.matter.matter.AttributeReader
import no.nordicsemi.nrf.matter.matter.IntAttributeParser
import no.nordicsemi.nrf.matter.matter.RawAttributeParser
import no.nordicsemi.nrf.matter.matter.StandardClusterIds
import no.nordicsemi.nrf.matter.matter.StringAttributeParser
import no.nordicsemi.nrf.matter.matter.decodeStructArray
import no.nordicsemi.nrf.matter.matter.decodeValueArray
import no.nordicsemi.nrf.matter.matter.nsNumber
import no.nordicsemi.nrf.matter.matter.rawValue
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceMatterInfo
import no.nordicsemi.nrf.matter.model.DeviceType
import platform.Foundation.NSDate
import platform.Foundation.NSNumber
import platform.Foundation.timeIntervalSince1970

/** Basic Information cluster attribute IDs, from `MTRClusterConstants.h`. */
private object BasicInformationAttribute {
    const val VENDOR_NAME = 0x0001
    const val VENDOR_ID = 0x0002
    const val PRODUCT_NAME = 0x0003
    const val PRODUCT_ID = 0x0004
    const val SOFTWARE_VERSION_STRING = 0x000A
    const val SERIAL_NUMBER = 0x000F
    const val UNIQUE_ID = 0x0012
    const val SPECIFICATION_VERSION = 0x0015
}

/** Descriptor cluster attribute IDs, from `MTRClusterConstants.h`. */
private object DescriptorAttribute {
    const val DEVICE_TYPE_LIST = 0x0000
    const val SERVER_LIST = 0x0001
    const val CLIENT_LIST = 0x0002
    const val PARTS_LIST = 0x0003
}

/** `DeviceTypeStruct` field context tags (Descriptor cluster). */
private const val DEVICE_TYPE_STRUCT_FIELD_DEVICE_TYPE = 0

/**
 * Mirrors `iosApp/iosApp/kotlin/local/LocalMatterClusterDiscovery.swift`, using raw
 * `MTRBaseDevice` calls instead of the typed `MTRBaseCluster*` wrapper classes (see
 * [StandardClusterIds]). Called from the still-Swift `LocalMatterCommissioner` right after
 * `MatterAddDeviceRequest.perform()` succeeds — see the class doc on
 * [no.nordicsemi.nrf.matter.matter.LocalControllerProviderImpl] for why this (and not the old
 * Swift `LocalMatterClusterDiscovery`) must be the sole caller of the controller singleton in
 * the main app process from this point on.
 */
@OptIn(ExperimentalForeignApi::class)
class IosClusterDiscovery {

    var stage: Stage = Stage.READ_BASIC_INFORMATION

    suspend fun discoverClusters(deviceId: DeviceId): Device {
        val nodeId = deviceId.nsNumber()
        val reader = AttributeReader(nodeId)
        val rootEndpoint = NSNumber(int = 0)
        val basicInfoCluster = NSNumber(int = StandardClusterIds.BasicInformation.CLUSTER)

        stage = Stage.READ_BASIC_INFORMATION

        val vendorId = reader.readAttribute(rootEndpoint, basicInfoCluster, NSNumber(int = BasicInformationAttribute.VENDOR_ID), IntAttributeParser)
        val vendorName = reader.readAttribute(rootEndpoint, basicInfoCluster, NSNumber(int = BasicInformationAttribute.VENDOR_NAME), StringAttributeParser)
        val productId = reader.readAttribute(rootEndpoint, basicInfoCluster, NSNumber(int = BasicInformationAttribute.PRODUCT_ID), IntAttributeParser)
        val productName = reader.readAttribute(rootEndpoint, basicInfoCluster, NSNumber(int = BasicInformationAttribute.PRODUCT_NAME), StringAttributeParser)
        val uniqueId = reader.readAttribute(rootEndpoint, basicInfoCluster, NSNumber(int = BasicInformationAttribute.UNIQUE_ID), StringAttributeParser)
        val softwareVersion = reader.readAttribute(rootEndpoint, basicInfoCluster, NSNumber(int = BasicInformationAttribute.SOFTWARE_VERSION_STRING), StringAttributeParser)
        val specificationVersion = reader.readAttribute(rootEndpoint, basicInfoCluster, NSNumber(int = BasicInformationAttribute.SPECIFICATION_VERSION), IntAttributeParser)
        val serialNumber = runCatching {
            reader.readAttribute(rootEndpoint, basicInfoCluster, NSNumber(int = BasicInformationAttribute.SERIAL_NUMBER), StringAttributeParser)
        }.getOrNull()

        stage = Stage.READ_DESCRIPTOR_CLUSTER

        val descriptorCluster = NSNumber(int = StandardClusterIds.Descriptor.CLUSTER)
        val partsList = decodeValueArray(
            reader.readAttribute(rootEndpoint, descriptorCluster, NSNumber(int = DescriptorAttribute.PARTS_LIST), RawAttributeParser),
        ).mapNotNull { it as? NSNumber }

        val deviceMatterInfo = mutableListOf<DeviceMatterInfo>()
        for (endpointNumber in partsList) {
            val endpoint = NSNumber(int = endpointNumber.intValue)

            val deviceTypes = decodeStructArray(
                reader.readAttribute(endpoint, descriptorCluster, NSNumber(int = DescriptorAttribute.DEVICE_TYPE_LIST), RawAttributeParser),
            ).mapNotNull { struct -> (struct[DEVICE_TYPE_STRUCT_FIELD_DEVICE_TYPE].rawValue() as? NSNumber)?.longLongValue }

            val serverClusters = decodeValueArray(
                reader.readAttribute(endpoint, descriptorCluster, NSNumber(int = DescriptorAttribute.SERVER_LIST), RawAttributeParser),
            ).mapNotNull { (it as? NSNumber)?.longLongValue }

            val clientClusters = decodeValueArray(
                reader.readAttribute(endpoint, descriptorCluster, NSNumber(int = DescriptorAttribute.CLIENT_LIST), RawAttributeParser),
            ).mapNotNull { (it as? NSNumber)?.longLongValue }

            val manufacturerSpecificData: ManufacturerSpecificData? = if (0xFFF1FC01L in serverClusters) {
                runCatching { IosMatterManufacturerSpecificController().getData(deviceId, endpointNumber.intValue) }.getOrNull()
            } else {
                null
            }

            deviceMatterInfo.add(
                DeviceMatterInfo(
                    endpoint = endpointNumber.intValue,
                    types = deviceTypes,
                    serverClusters = serverClusters,
                    clientClusters = clientClusters,
                    manufacturerSpecificData = manufacturerSpecificData,
                ),
            )
        }

        val deviceType = mapDeviceType(deviceMatterInfo.flatMap { it.types }.firstOrNull())

        return Device(
            deviceId = deviceId,
            dateCommissioned = (NSDate().timeIntervalSince1970 * 1000).toLong(),
            vendorId = vendorId.toString(),
            productId = productId.toString(),
            deviceType = deviceType,
            name = "Matter device: ${deviceId.stringValue}",
            productName = productName,
            vendorName = vendorName,
            uniqueId = uniqueId,
            softwareVersion = softwareVersion,
            specificationVersion = specificationVersion.toLong(),
            serialNumer = serialNumber,
            deviceMatterInfo = deviceMatterInfo,
        )
    }

    private fun mapDeviceType(deviceType: Long?): DeviceType = when (deviceType) {
        10L -> DeviceType.DOOR_LOCK
        260L -> DeviceType.LIGHT_SWITCH
        257L -> DeviceType.LIGHT_ON_OFF
        0xFFF10001L -> DeviceType.MANUFACTURER_SPECIFIC_DEVICE
        else -> DeviceType.UNSUPPORTED
    }
}
