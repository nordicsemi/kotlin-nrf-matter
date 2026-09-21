package no.nordicsemi.nrf.matter.webdemo

import no.nordicsemi.nrf.matter.cluster.BasicInfoClusterInfo
import no.nordicsemi.nrf.matter.cluster.DescriptorClusterInfo
import no.nordicsemi.nrf.matter.cluster.DoorLockClusterInfo
import no.nordicsemi.nrf.matter.cluster.LevelControlClusterInfo
import no.nordicsemi.nrf.matter.cluster.MANUFACTURER_SPEC_BUTTON_ATTRIBUTE_ID
import no.nordicsemi.nrf.matter.cluster.MANUFACTURER_SPEC_CLUSTER_ID
import no.nordicsemi.nrf.matter.cluster.MANUFACTURER_SPEC_LED_ATTRIBUTE_ID
import no.nordicsemi.nrf.matter.cluster.MANUFACTURER_SPEC_NAME_ATTRIBUTE_ID
import no.nordicsemi.nrf.matter.cluster.OnOffClusterInfo
import no.nordicsemi.nrf.matter.cluster.WebMatterClient
import no.nordicsemi.nrf.matter.cluster.BASIC_INFO_EXT_RANDOM_NUMBER_ATTRIBUTE_ID
import no.nordicsemi.nrf.matter.model.DeviceId

/** The endpoint every fake device's functional cluster lives on; endpoint 0 is the root. */
private const val FUNCTIONAL_ENDPOINT = 1

/**
 * One fake device profile, matching the exact rows of `overview.md`'s device-type table so that
 * every kind the docs describe is reachable in the demo. `deviceTypeId`/`serverClusters`/
 * `clientClusters` are what the real `DescriptorCluster`/`ClusterControllerMapper` need to attach
 * the correct real controller.
 */
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
        deviceTypeId = 256L, // StandardDeviceType.LIGHT_ON_OFF
        serverClusters = listOf(OnOffClusterInfo.ID),
        clientClusters = emptyList(),
    ),
    LIGHT_SWITCH(
        displayName = "Hallway Switch",
        vendorName = "Nordic Semiconductor",
        productName = "nRF52840 DK -- Light Switch",
        deviceTypeId = 259L, // StandardDeviceType.LIGHT_SWITCH
        serverClusters = emptyList(),
        clientClusters = listOf(OnOffClusterInfo.ID),
    ),
    DIMMABLE_LIGHT(
        displayName = "Living Room Light",
        vendorName = "Nordic Semiconductor",
        productName = "nRF52840 DK -- Light Bulb",
        deviceTypeId = 257L, // StandardDeviceType.DIMMABLE_LIGHT
        serverClusters = listOf(OnOffClusterInfo.ID, LevelControlClusterInfo.ID),
        clientClusters = emptyList(),
    ),
    DOOR_LOCK(
        displayName = "Front Door Lock",
        vendorName = "Nordic Semiconductor",
        productName = "nRF52840 DK -- Door Lock",
        deviceTypeId = 10L, // StandardDeviceType.DOOR_LOCK
        serverClusters = listOf(DoorLockClusterInfo.ID),
        clientClusters = emptyList(),
    ),
    MANUFACTURER_SPECIFIC(
        displayName = "nRF54L15 DK",
        vendorName = "Nordic Semiconductor",
        productName = "nRF54L15 DK -- Manufacturer Specific Sample",
        deviceTypeId = 0xFFF10001L, // NordicDeviceType, from :shared's NordicClusters.kt
        serverClusters = listOf(MANUFACTURER_SPEC_CLUSTER_ID, BasicInfoClusterInfo.ID),
        clientClusters = emptyList(),
    ),
    UNSUPPORTED(
        displayName = "Unknown Accessory",
        vendorName = "Acme Corp",
        productName = "Generic Sensor",
        deviceTypeId = 0x0999L, // not a StandardDeviceType, deliberately -- exercises the unsupported path
        serverClusters = emptyList(),
        clientClusters = emptyList(),
    ),
}

internal object WebDeviceCatalog {
    private var nextIndex = 0

    /** Cycles through every profile so repeated commissioning demos all five device kinds. */
    fun next(): WebDeviceProfile {
        val profile = WebDeviceProfile.entries[nextIndex % WebDeviceProfile.entries.size]
        nextIndex++
        return profile
    }
}

/**
 * Seeds everything [no.nordicsemi.nrf.matter.commission.FinaliseCommissioningUseCase] will read
 * during commissioning (root Basic Information + a two-level Descriptor walk), plus the
 * functional cluster attributes' initial values so the real device-detail controls have
 * something sensible to show the moment the card expands.
 */
internal fun WebMatterClient.seedDevice(deviceId: DeviceId, profile: WebDeviceProfile) {
    val rootEndpoint = 0

    // Basic Information cluster, always read at the root endpoint.
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.VENDOR_NAME, profile.vendorName)
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.VENDOR_ID, 0x1481)
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.PRODUCT_NAME, profile.productName)
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.PRODUCT_ID, 0x0001)
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.SOFTWARE_VERSION_STRING, "3.3.0")
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.SERIAL_NUMBER, "SN-${deviceId.stringValue}")
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.SPECIFICATION_VERSION, 0x0105_0000L)
    seed(deviceId, rootEndpoint, BasicInfoClusterInfo.ID, BasicInfoClusterInfo.Attribute.UNIQUE_ID, "UID-${deviceId.stringValue}")

    // Descriptor cluster at the root: one child endpoint, no clusters of its own.
    seed(deviceId, rootEndpoint, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.DEVICE_TYPE_LIST, emptyList<Long>())
    seed(deviceId, rootEndpoint, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.SERVER_LIST, emptyList<Long>())
    seed(deviceId, rootEndpoint, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.CLIENT_LIST, emptyList<Long>())
    seed(deviceId, rootEndpoint, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.PARTS_LIST, listOf(FUNCTIONAL_ENDPOINT))

    // Descriptor cluster at the functional endpoint: the profile's device type and clusters.
    seed(deviceId, FUNCTIONAL_ENDPOINT, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.DEVICE_TYPE_LIST, listOf(profile.deviceTypeId))
    seed(deviceId, FUNCTIONAL_ENDPOINT, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.SERVER_LIST, profile.serverClusters)
    seed(deviceId, FUNCTIONAL_ENDPOINT, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.CLIENT_LIST, profile.clientClusters)
    seed(deviceId, FUNCTIONAL_ENDPOINT, DescriptorClusterInfo.ID, DescriptorClusterInfo.Attribute.PARTS_LIST, emptyList<Int>())

    // Functional attributes' starting values, at the functional endpoint.
    if (OnOffClusterInfo.ID in profile.serverClusters) {
        seed(deviceId, FUNCTIONAL_ENDPOINT, OnOffClusterInfo.ID, OnOffClusterInfo.Attribute.ON_OFF, false)
    }
    if (LevelControlClusterInfo.ID in profile.serverClusters) {
        seed(deviceId, FUNCTIONAL_ENDPOINT, LevelControlClusterInfo.ID, LevelControlClusterInfo.Attribute.CURRENT_LEVEL, 150)
    }
    if (DoorLockClusterInfo.ID in profile.serverClusters) {
        seed(deviceId, FUNCTIONAL_ENDPOINT, DoorLockClusterInfo.ID, DoorLockClusterInfo.Attribute.LOCK_STATE, 1) // LOCKED
    }
    if (MANUFACTURER_SPEC_CLUSTER_ID in profile.serverClusters) {
        seed(deviceId, FUNCTIONAL_ENDPOINT, MANUFACTURER_SPEC_CLUSTER_ID, MANUFACTURER_SPEC_LED_ATTRIBUTE_ID, false)
        seed(deviceId, FUNCTIONAL_ENDPOINT, MANUFACTURER_SPEC_CLUSTER_ID, MANUFACTURER_SPEC_BUTTON_ATTRIBUTE_ID, false)
        seed(deviceId, FUNCTIONAL_ENDPOINT, MANUFACTURER_SPEC_CLUSTER_ID, MANUFACTURER_SPEC_NAME_ATTRIBUTE_ID, profile.displayName)
        seed(deviceId, FUNCTIONAL_ENDPOINT, BasicInfoClusterInfo.ID, BASIC_INFO_EXT_RANDOM_NUMBER_ATTRIBUTE_ID, 0L)
    }
}
