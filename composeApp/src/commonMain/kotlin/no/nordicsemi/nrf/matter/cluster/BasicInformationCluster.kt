package no.nordicsemi.nrf.matter.cluster

import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.BasicInformation
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.ROOT_ENDPOINT
import kotlin.coroutines.cancellation.CancellationException

object BasicInfoClusterInfo {
    const val ID: Long = 0x28

    object Attribute {
        const val VENDOR_NAME: Long = 0x0001
        const val VENDOR_ID: Long = 0x0002
        const val PRODUCT_NAME: Long = 0x0003
        const val PRODUCT_ID: Long = 0x0004
        const val SOFTWARE_VERSION_STRING: Long = 0x000A
        const val SERIAL_NUMBER: Long = 0x000F
        const val UNIQUE_ID: Long = 0x0012
        const val SPECIFICATION_VERSION: Long = 0x0015
    }
}

/**
 * The Basic Information cluster, always on endpoint 0.
 *
 * Read right after commissioning, to record what the device is.
 */
class BasicInformationCluster(
    override val deviceId: DeviceId,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = BasicInfoClusterInfo.ID
    override val endpoint = ROOT_ENDPOINT

    /**
     * Everything the app records about the device, read one attribute at a time.
     *
     * Sequentially rather than concurrently: reads are dispatched into the platform Matter stack,
     * and issuing them in parallel has deadlocked the CHIP JNI event loop on Android.
     *
     * [readVendorName] goes first because it is the one read allowed to fail, so a device that
     * cannot be reached at all fails here rather than yielding an empty [BasicInformation].
     */
    suspend fun read(): BasicInformation = BasicInformation(
        vendorName = readVendorName(),
        vendorId = readVendorId(),
        productName = readProductName(),
        productId = readProductId(),
        softwareVersion = readSoftwareVersion(),
        serialNumber = readSerialNumber(),
        specificationVersion = readSpecificationVersion(),
        uniqueId = readUniqueId(),
    )

    /**
     * The name of the vendor that made the device.
     *
     * Mandatory in the specification, and the only read here that is not softened to `null` on
     * failure - it doubles as the check that the device answers at all.
     */
    suspend fun readVendorName(): String =
        readAttribute<String>(BasicInfoClusterInfo.Attribute.VENDOR_NAME)

    /** The vendor's Matter-assigned id, or `null` if the device would not give it. */
    suspend fun readVendorId(): Int? =
        readNumberOrNull(BasicInfoClusterInfo.Attribute.VENDOR_ID)?.toInt()

    /** The vendor's name for the product, or `null` if the device would not give it. */
    suspend fun readProductName(): String? =
        readOrNull<String>(BasicInfoClusterInfo.Attribute.PRODUCT_NAME)

    /** The vendor's id for the product, or `null` if the device would not give it. */
    suspend fun readProductId(): Int? =
        readNumberOrNull(BasicInfoClusterInfo.Attribute.PRODUCT_ID)?.toInt()

    /** The firmware version as the vendor writes it, or `null` if the device would not give it. */
    suspend fun readSoftwareVersion(): String? =
        readOrNull<String>(BasicInfoClusterInfo.Attribute.SOFTWARE_VERSION_STRING)

    /** The serial number of this unit, or `null` if the device would not give it. */
    suspend fun readSerialNumber(): String? =
        readOrNull<String>(BasicInfoClusterInfo.Attribute.SERIAL_NUMBER)

    /** The version of the Matter specification the device implements, or `null`. */
    suspend fun readSpecificationVersion(): Long? =
        readNumberOrNull(BasicInfoClusterInfo.Attribute.SPECIFICATION_VERSION)

    /** The device's unique id, or `null` if the device would not give it. */
    suspend fun readUniqueId(): String? =
        readOrNull<String>(BasicInfoClusterInfo.Attribute.UNIQUE_ID)

    private suspend fun readNumberOrNull(attributeId: Long): Long? =
        readOrNull<Number>(attributeId)?.toLong()

    /**
     * Reads one attribute, or `null` if the device would not give it.
     *
     * The value is cast rather than checked, so a device reporting an unexpected type for an
     * attribute yields `null` here too.
     */
    private suspend inline fun <reified T> readOrNull(attributeId: Long): T? = try {
        readAttribute<Any?>(attributeId) as? T
    } catch (c: CancellationException) {
        throw c
    } catch (t: Throwable) {
        NordicLogger.debug(
            "Device $deviceId did not report Basic Information attribute $attributeId: " +
                    "${t.message}",
            tag = TAG,
        )
        null
    }

    companion object {
        private const val TAG = "BasicInformation"
    }
}
