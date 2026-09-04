package no.nordicsemi.nrf.matter.cluster

import no.nordicsemi.nrf.matter.model.DeviceId

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
     * The Matter device type ids of this endpoint.
     *
     * `DeviceTypeList` is a list of structures; only the `DeviceType` field is read, the revision
     * being of no use to the app.
     */
    suspend fun deviceTypes(): List<Long> =
        readList(DescriptorClusterInfo.Attribute.DEVICE_TYPE_LIST).mapNotNull { entry ->
            when (entry) {
                is MatterStruct ->
                    entry.longOrNull(DescriptorClusterInfo.DeviceTypeStruct.DEVICE_TYPE)
                // A device that reports the list as bare ids rather than as structures.
                is Number -> entry.toLong()
                else -> null
            }
        }

    /** The clusters this endpoint implements as a server, and so can be asked to act on. */
    suspend fun serverClusters(): List<Long> =
        readClusterIds(DescriptorClusterInfo.Attribute.SERVER_LIST)

    /** The clusters this endpoint implements as a client, and so can drive on another device. */
    suspend fun clientClusters(): List<Long> =
        readClusterIds(DescriptorClusterInfo.Attribute.CLIENT_LIST)

    /** The endpoints beneath this one, empty for a leaf endpoint. */
    suspend fun parts(): List<Int> =
        readList(DescriptorClusterInfo.Attribute.PARTS_LIST)
            .mapNotNull { (it as? Number)?.toInt() }

    private suspend fun readClusterIds(attributeId: Long): List<Long> =
        readList(attributeId).mapNotNull { (it as? Number)?.toLong() }

    /**
     * Reads a list attribute, treating a device that reports nothing as reporting an empty list.
     *
     * Every attribute of this cluster is a list, and an empty one is normal - a leaf endpoint's
     * `PartsList`, or the `ClientList` of anything that drives nothing.
     */
    private suspend fun readList(attributeId: Long): List<Any?> =
        readAttribute<List<Any?>?>(attributeId) ?: emptyList()
}
