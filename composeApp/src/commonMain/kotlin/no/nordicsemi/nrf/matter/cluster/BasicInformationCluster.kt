package no.nordicsemi.nrf.matter.cluster

import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId
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

        /** Nordic's extension to this cluster, not part of the Matter specification. */
        const val RANDOM_NUMBER: Long = 0x17
    }

    object Command {
        /** Nordic's extension to this cluster, not part of the Matter specification. */
        const val GENERATE_RANDOM_NUMBER: Long = 0x00
    }
}

/** What a device says about itself: who made it, what it is, and which version it runs. */
data class BasicInformation(
    val vendorId: Int? = null,
    val vendorName: String? = null,
    val productId: Int? = null,
    val productName: String? = null,
    val softwareVersion: String? = null,
    val serialNumber: String? = null,
    val specificationVersion: Long? = null,
    val uniqueId: String? = null,
)

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
    override val endpoint = 0

    /**
     * Everything the app records about the device, read one attribute at a time.
     *
     * Sequentially rather than concurrently: reads are dispatched into the platform Matter stack,
     * and issuing them in parallel has deadlocked the CHIP JNI event loop on Android.
     *
     * Every attribute here is optional in the specification or may be rejected by a device, so a
     * failed read leaves its field `null` instead of failing the whole commissioning. A device
     * that cannot be reached at all fails on the first read.
     */
    suspend fun read(): BasicInformation {
        val vendorName = readAttribute<String>(BasicInfoClusterInfo.Attribute.VENDOR_NAME)
        val vendorId = readNumberOrNull(BasicInfoClusterInfo.Attribute.VENDOR_ID)
        val productName = readOrNull<String>(BasicInfoClusterInfo.Attribute.PRODUCT_NAME)
        val productId = readNumberOrNull(BasicInfoClusterInfo.Attribute.PRODUCT_ID)
        val softwareVersion =
            readOrNull<String>(BasicInfoClusterInfo.Attribute.SOFTWARE_VERSION_STRING)
        val serialNumber = readOrNull<String>(BasicInfoClusterInfo.Attribute.SERIAL_NUMBER)
        val specificationVersion =
            readNumberOrNull(BasicInfoClusterInfo.Attribute.SPECIFICATION_VERSION)
        val uniqueId = readOrNull<String>(BasicInfoClusterInfo.Attribute.UNIQUE_ID)

        return BasicInformation(
            vendorId = vendorId?.toInt(),
            vendorName = vendorName,
            productId = productId?.toInt(),
            productName = productName,
            softwareVersion = softwareVersion,
            serialNumber = serialNumber,
            specificationVersion = specificationVersion,
            uniqueId = uniqueId,
        )
    }

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
