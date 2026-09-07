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

class BasicInformationCluster(
    override val deviceId: DeviceId,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = BasicInfoClusterInfo.ID
    override val endpoint = ROOT_ENDPOINT

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

    suspend fun readVendorName(): String =
        readAttribute<String>(BasicInfoClusterInfo.Attribute.VENDOR_NAME)

    suspend fun readVendorId(): Int? =
        readNumberOrNull(BasicInfoClusterInfo.Attribute.VENDOR_ID)?.toInt()

    suspend fun readProductName(): String? =
        readOrNull<String>(BasicInfoClusterInfo.Attribute.PRODUCT_NAME)

    suspend fun readProductId(): Int? =
        readNumberOrNull(BasicInfoClusterInfo.Attribute.PRODUCT_ID)?.toInt()

    suspend fun readSoftwareVersion(): String? =
        readOrNull<String>(BasicInfoClusterInfo.Attribute.SOFTWARE_VERSION_STRING)

    suspend fun readSerialNumber(): String? =
        readOrNull<String>(BasicInfoClusterInfo.Attribute.SERIAL_NUMBER)

    suspend fun readSpecificationVersion(): Long? =
        readNumberOrNull(BasicInfoClusterInfo.Attribute.SPECIFICATION_VERSION)

    suspend fun readUniqueId(): String? =
        readOrNull<String>(BasicInfoClusterInfo.Attribute.UNIQUE_ID)

    private suspend fun readNumberOrNull(attributeId: Long): Long? =
        readOrNull<Number>(attributeId)?.toLong()

    private suspend inline fun <reified T> readOrNull(attributeId: Long): T? = try {
        readAttribute<Any?>(attributeId) as? T
    } catch (c: CancellationException) {
        throw c
    } catch (t: Throwable) {
        NordicLogger.debug(
            message = "Device $deviceId did not report Basic Information attribute $attributeId: ${t.message}",
            tag = TAG,
        )
        null
    }

    companion object {
        private const val TAG = "BasicInformation"
    }
}
