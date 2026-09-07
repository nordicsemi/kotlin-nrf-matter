package no.nordicsemi.nrf.matter.cluster

import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.Endpoint
import kotlin.coroutines.cancellation.CancellationException

object DescriptorClusterInfo {
    const val ID: Long = 0x001D

    object Attribute {
        const val DEVICE_TYPE_LIST: Long = 0x0000
        const val SERVER_LIST: Long = 0x0001
        const val CLIENT_LIST: Long = 0x0002
        const val PARTS_LIST: Long = 0x0003
    }

    object DeviceTypeStruct {
        /** Context tag of the `DeviceType` field. */
        const val DEVICE_TYPE: Long = 0
    }
}

/**
 * What an endpoint is and what it implements: its device types, the clusters it serves, the
 * clusters it can drive as a client, and the endpoints beneath it.
 *
 * Every device answers this cluster on every endpoint, which is what makes walking a device
 * possible: endpoint 0 is the root node, and [parts] leads to the rest.
 */
class DescriptorCluster(
    override val deviceId: DeviceId,
    override val endpoint: Int,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = DescriptorClusterInfo.ID

    /**
     * This endpoint and every endpoint beneath it, one [Endpoint] each.
     *
     * Walking from a [DescriptorCluster] on the root node - the endpoint present on every device -
     * therefore describes the whole device, root node included, which is what
     * [no.nordicsemi.nrf.matter.model.root] returns.
     *
     * An endpoint listed in `PartsList` may not answer this cluster itself, and one unreadable
     * endpoint should not cost the caller the rest of the device, so a child that cannot be read is
     * logged and left out.
     */
    suspend fun endpoints(): List<Endpoint> {
        val collected = mutableListOf<Endpoint>()
        collectInto(collected)

        return collected
    }

    private suspend fun collectInto(into: MutableList<Endpoint>) {
        if (into.any { it.id == endpoint }) return

        val serverClusters = serverClusters()
        val clientClusters = clientClusters()
        val deviceTypes = deviceTypes()
        val parts = parts()

        into += Endpoint(
            id = endpoint,
            types = deviceTypes,
            serverClusters = serverClusters,
            clientClusters = clientClusters,
        )

        parts.forEach { child ->
            try {
                DescriptorCluster(deviceId, child, controller).collectInto(into)
            } catch (c: CancellationException) {
                throw c
            } catch (t: Throwable) {
                NordicLogger.error(
                    "Endpoint $child of device $deviceId could not be read, skipping...",
                    t,
                    tag = TAG,
                )
            }
        }
    }

    /**
     * The Matter device type ids of this endpoint.
     *
     * `DeviceTypeList` is a list of structures; only the `DeviceType` field is read, the revision
     * being of no use to the app. A device that reports the list as bare ids rather than as
     * structures is read that way instead, from the same single read.
     */
    suspend fun deviceTypes(): List<Long> {
        val entries = readRawList(DescriptorClusterInfo.Attribute.DEVICE_TYPE_LIST)

        return entries.filterIsInstance<MatterStruct>()
            .mapNotNull { it.longOrNull(DescriptorClusterInfo.DeviceTypeStruct.DEVICE_TYPE) }
            .ifEmpty { entries.filterIsInstance<Number>().map { it.toLong() } }
    }

    /** The clusters this endpoint implements as a server, and so can be asked to act on. */
    suspend fun serverClusters(): List<Long> =
        readClusterIds(DescriptorClusterInfo.Attribute.SERVER_LIST)

    /** The clusters this endpoint implements as a client, and so can drive on another device. */
    suspend fun clientClusters(): List<Long> =
        readClusterIds(DescriptorClusterInfo.Attribute.CLIENT_LIST)

    /** The endpoints beneath this one, empty for a leaf endpoint. */
    suspend fun parts(): List<Int> =
        readList<Number>(DescriptorClusterInfo.Attribute.PARTS_LIST).map { it.toInt() }

    private suspend fun readClusterIds(attributeId: Long): List<Long> =
        readList<Number>(attributeId).map { it.toLong() }

    /**
     * Reads a list attribute, keeping the entries of the type the caller expects.
     *
     * Entries cross the platform boundary untyped, so this is where they are narrowed: an entry a
     * device reports at an unexpected type is dropped rather than trusted, which costs the caller
     * that entry rather than the whole read.
     */
    private suspend inline fun <reified T : Any> readList(attributeId: Long): List<T> =
        readRawList(attributeId).filterIsInstance<T>()

    /**
     * A list attribute as the platform Matter stack decoded it, entries not yet narrowed.
     *
     * Every attribute of this cluster is a list, and an empty one is normal - a leaf endpoint's
     * `PartsList`, or the `ClientList` of anything that drives nothing - as is a device that
     * reports no value at all.
     */
    private suspend fun readRawList(attributeId: Long): List<*> =
        readAttribute<List<*>?>(attributeId) ?: emptyList<Nothing>()

    private companion object {
        private const val TAG = "Descriptor"
    }
}
