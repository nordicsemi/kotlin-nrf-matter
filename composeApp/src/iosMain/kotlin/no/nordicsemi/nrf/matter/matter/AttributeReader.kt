package no.nordicsemi.nrf.matter.matter

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber
import platform.Matter.MTRBaseDevice

internal class MissingAttributeException(message: String) : Exception(message)

/**
 * Mirrors `iosApp/iosApp/kotlin/local/AttributeReader.swift`.
 */
@OptIn(ExperimentalForeignApi::class)
internal class AttributeReader(deviceId: NSNumber) {

    private val baseDevice: MTRBaseDevice = MTRBaseDevice.deviceWithNodeID(
        nodeID = deviceId,
        controller = LocalControllerProviderImpl.getController("AttributeReader"),
    )

    suspend fun <T> readAttribute(
        endpoint: NSNumber,
        cluster: NSNumber,
        attribute: NSNumber,
        parser: AttributeParser<T>,
    ): T {
        val values = mtrCall { completion ->
            baseDevice.readAttributesWithEndpointID(
                endpointID = endpoint,
                clusterID = cluster,
                attributeID = attribute,
                params = null,
                queue = defaultMatterQueue(),
                completion = completion,
            )
        }
        val raw = readAny(values)
        return parser.parse(raw)
    }

    private fun readAny(values: List<*>?): Any? {
        val report = values?.firstOrNull() as? Map<*, *> ?: throw MissingAttributeException("No attribute report returned.")
        val data = report["data"] as? Map<*, *> ?: throw MissingAttributeException("Attribute report has no 'data' entry.")
        if (!data.containsKey("value")) throw MissingAttributeException("Attribute report has no 'value' entry.")
        return data["value"]
    }
}
