package no.nordicsemi.nrf.matter.docs.demo

import no.nordicsemi.nrf.matter.cluster.BasicInfoClusterInfo
import no.nordicsemi.nrf.matter.cluster.DescriptorClusterInfo
import no.nordicsemi.nrf.matter.cluster.DoorLockClusterInfo
import no.nordicsemi.nrf.matter.cluster.LevelControlClusterInfo
import no.nordicsemi.nrf.matter.cluster.OnOffClusterInfo
import no.nordicsemi.nrf.matter.model.DeviceId

private const val FUNCTIONAL_ENDPOINT = 1

internal enum class WebDeviceProfile(
    val displayName: String,
    val vendorName: String,
    val productName: String,
    val deviceTypeId: Long,
    val serverClusters: List<Long>,
    val clientClusters: List<Long>,
) {
    ON_OFF_LIGHT(
        displayName = "Kitchen Light",
        vendorName = "Nordic Semiconductor",
        productName = "nRF52840 DK -- Light Bulb",
        deviceTypeId = 256L,
        serverClusters = listOf(OnOffClusterInfo.ID),
        clientClusters = emptyList(),
    ),
    LIGHT_SWITCH(
        displayName = "Hallway Switch",
        vendorName = "Nordic Semiconductor",
        productName = "nRF52840 DK -- Light Switch",
        deviceTypeId = 259L,
        serverClusters = emptyList(),
        clientClusters = listOf(OnOffClusterInfo.ID),
    ),
    DIMMABLE_LIGHT(
        displayName = "Living Room Light",
        vendorName = "Nordic Semiconductor",
        productName = "nRF52840 DK -- Light Bulb",
        deviceTypeId = 257L,
        serverClusters = listOf(OnOffClusterInfo.ID, LevelControlClusterInfo.ID),
        clientClusters = emptyList(),
    ),
    DOOR_LOCK(
        displayName = "Front Door Lock",
        vendorName = "Nordic Semiconductor",
        productName = "nRF52840 DK -- Door Lock",
        deviceTypeId = 10L,
        serverClusters = listOf(DoorLockClusterInfo.ID),
        clientClusters = emptyList(),
    ),
    MANUFACTURER_SPECIFIC(
        displayName = "nRF54L15 DK",
        vendorName = "Nordic Semiconductor",
        productName = "nRF54L15 DK -- Manufacturer Specific Sample",
        deviceTypeId = 0xFFF10001L,
        serverClusters = listOf(MANUFACTURER_SPEC_CLUSTER_ID, BasicInfoClusterInfo.ID),
        clientClusters = emptyList(),
    ),
    UNSUPPORTED(
        displayName = "Unknown Accessory",
        vendorName = "Acme Corp",
        productName = "Generic Sensor",
        deviceTypeId = 0x0999L,
        serverClusters = emptyList(),
        clientClusters = emptyList(),
    ),
}

internal object WebDeviceCatalog {
    private var nextIndex = 0

    fun next(): WebDeviceProfile {
        val profile = WebDeviceProfile.entries[nextIndex % WebDeviceProfile.entries.size]
        nextIndex++
        return profile
    }
}

internal fun WebMatterClient.seedDevice(deviceId: DeviceId, profile: WebDeviceProfile) {
    val rootEndpoint = 0

    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.VENDOR_NAME, profile.vendorName)
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.VENDOR_ID, 0x1481)
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.PRODUCT_NAME, profile.productName)
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.PRODUCT_ID, 0x0001)
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.SOFTWARE_VERSION_STRING, "3.3.0")
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.SERIAL_NUMBER, "SN-${deviceId.stringValue}")
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.SPECIFICATION_VERSION, 0x0105_0000L)
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.UNIQUE_ID, "UID-${deviceId.stringValue}")

    seed(deviceId, rootEndpoint, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.DEVICE_TYPE_LIST, emptyList<Long>())
    seed(deviceId, rootEndpoint, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.SERVER_LIST, emptyList<Long>())
    seed(deviceId, rootEndpoint, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.CLIENT_LIST, emptyList<Long>())
    seed(deviceId, rootEndpoint, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.PARTS_LIST, listOf(FUNCTIONAL_ENDPOINT))

    seed(deviceId, FUNCTIONAL_ENDPOINT, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.DEVICE_TYPE_LIST, listOf(profile.deviceTypeId))
    seed(deviceId, FUNCTIONAL_ENDPOINT, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.SERVER_LIST, profile.serverClusters)
    seed(deviceId, FUNCTIONAL_ENDPOINT, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.CLIENT_LIST, profile.clientClusters)
    seed(deviceId, FUNCTIONAL_ENDPOINT, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.PARTS_LIST, emptyList<Int>())

    if (OnOffClusterInfo.ID in profile.serverClusters) {
        seed(deviceId, FUNCTIONAL_ENDPOINT, OnOffClusterInfo.ID, OnOffClusterInfo.Attribute.ON_OFF, false)
    }
    if (LevelControlClusterInfo.ID in profile.serverClusters) {
        seed(deviceId, FUNCTIONAL_ENDPOINT, LevelControlClusterInfo.ID, LevelControlClusterInfo.Attribute.CURRENT_LEVEL, 150)
    }
    if (DoorLockClusterInfo.ID in profile.serverClusters) {
        seed(deviceId, FUNCTIONAL_ENDPOINT, DoorLockClusterInfo.ID, DoorLockClusterInfo.Attribute.LOCK_STATE, 1)
    }
    if (MANUFACTURER_SPEC_CLUSTER_ID in profile.serverClusters) {
        seed(deviceId, FUNCTIONAL_ENDPOINT, MANUFACTURER_SPEC_CLUSTER_ID, MANUFACTURER_SPEC_LED_ATTRIBUTE_ID, false)
        seed(deviceId, FUNCTIONAL_ENDPOINT, MANUFACTURER_SPEC_CLUSTER_ID, MANUFACTURER_SPEC_BUTTON_ATTRIBUTE_ID, false)
        seed(deviceId, FUNCTIONAL_ENDPOINT, MANUFACTURER_SPEC_CLUSTER_ID, MANUFACTURER_SPEC_NAME_ATTRIBUTE_ID, profile.displayName)
        seed(deviceId, FUNCTIONAL_ENDPOINT, BasicInfoClusterInfo.ID, BASIC_INFO_EXT_RANDOM_NUMBER_ATTRIBUTE_ID, 0L)
    }
}
