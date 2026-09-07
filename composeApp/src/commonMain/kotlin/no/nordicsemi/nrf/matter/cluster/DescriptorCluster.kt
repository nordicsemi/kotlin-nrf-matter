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
        const val DEVICE_TYPE: Long = 0
    }
}

class DescriptorCluster(
    override val deviceId: DeviceId,
    override val endpoint: Int,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = DescriptorClusterInfo.ID

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

    suspend fun deviceTypes(): List<Long> {
        val entries = readRawList(DescriptorClusterInfo.Attribute.DEVICE_TYPE_LIST)

        return entries.filterIsInstance<MatterStruct>()
            .mapNotNull { it.longOrNull(DescriptorClusterInfo.DeviceTypeStruct.DEVICE_TYPE) }
            .ifEmpty { entries.filterIsInstance<Number>().map { it.toLong() } }
    }

    suspend fun serverClusters(): List<Long> =
        readClusterIds(DescriptorClusterInfo.Attribute.SERVER_LIST)

    suspend fun clientClusters(): List<Long> =
        readClusterIds(DescriptorClusterInfo.Attribute.CLIENT_LIST)

    suspend fun parts(): List<Int> =
        readList<Number>(DescriptorClusterInfo.Attribute.PARTS_LIST).map { it.toInt() }

    private suspend fun readClusterIds(attributeId: Long): List<Long> =
        readList<Number>(attributeId).map { it.toLong() }

    private suspend inline fun <reified T : Any> readList(attributeId: Long): List<T> =
        readRawList(attributeId).filterIsInstance<T>()

    private suspend fun readRawList(attributeId: Long): List<*> =
        readAttribute<List<*>?>(attributeId) ?: emptyList<Nothing>()

    private companion object {
        private const val TAG = "Descriptor"
    }
}
