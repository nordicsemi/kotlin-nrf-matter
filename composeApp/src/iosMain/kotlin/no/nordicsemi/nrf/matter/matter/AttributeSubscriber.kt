package no.nordicsemi.nrf.matter.matter

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber
import platform.Matter.MTRBaseDevice
import platform.Matter.MTRSubscribeParams

/**
 * Mirrors `iosApp/iosApp/kotlin/local/AttributeSubscriber.swift`.
 *
 * Unlike the Swift version, no boxing wrapper (`IosFlowWrapper`/`KotlinBoolean`) is needed:
 * that machinery existed only to cross the Swift-to-Kotlin ObjC-generics boundary. Pure
 * Kotlin-to-Kotlin, calling into a plain `MutableSharedFlow`, has no such boundary.
 */
@OptIn(ExperimentalForeignApi::class)
internal class AttributeSubscriber(deviceId: NSNumber) {

    private val baseDevice: MTRBaseDevice = MTRBaseDevice.deviceWithNodeID(
        nodeID = deviceId,
        controller = LocalControllerProviderImpl.getController("AttributeSubscriber"),
    )

    fun <T> subscribe(
        endpoint: NSNumber,
        cluster: NSNumber,
        attribute: NSNumber,
        parser: AttributeParser<T>,
        onUpdate: (T) -> Unit,
    ) {
        baseDevice.subscribeToAttributesWithEndpointID(
            endpointID = endpoint,
            clusterID = cluster,
            attributeID = attribute,
            params = defaultSubscribeParams(),
            queue = defaultMatterQueue(),
            reportHandler = { values, error ->
                if (error == null) {
                    val report = values?.firstOrNull() as? Map<*, *>
                    val data = report?.get("data") as? Map<*, *>
                    if (data != null && data.containsKey("value")) {
                        val parsed = runCatching { parser.parse(data["value"]) }
                        parsed.getOrNull()?.let(onUpdate)
                    }
                }
            },
            subscriptionEstablished = {},
        )
    }

    private fun defaultSubscribeParams(): MTRSubscribeParams =
        MTRSubscribeParams(minInterval = NSNumber(int = 0), maxInterval = NSNumber(int = 0))
}
