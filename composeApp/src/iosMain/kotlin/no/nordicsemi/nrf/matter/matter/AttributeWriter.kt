package no.nordicsemi.nrf.matter.matter

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSNumber
import platform.Matter.MTRBaseDevice
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/** Writes attribute values via `MTRBaseDevice`'s generic write API — see [StandardClusterIds]. */
@OptIn(ExperimentalForeignApi::class)
internal class AttributeWriter(deviceId: NSNumber) {

    private val baseDevice: MTRBaseDevice =
        MTRBaseDevice.deviceWithNodeID(nodeID = deviceId, controller = LocalControllerProviderImpl.getController("AttributeWriter"))

    suspend fun writeAttribute(endpoint: NSNumber, cluster: NSNumber, attribute: NSNumber, value: Map<String, Any>) {
        suspendCancellableCoroutine<Unit> { cont ->
            baseDevice.writeAttributeWithEndpointID(
                endpointID = endpoint,
                clusterID = cluster,
                attributeID = attribute,
                value = value,
                timedWriteTimeout = null,
                queue = defaultMatterQueue(),
                completion = { _, error ->
                    if (error != null) {
                        cont.resumeWithException(MatterCallException(error))
                    } else {
                        cont.resume(Unit)
                    }
                },
            )
        }
    }
}
