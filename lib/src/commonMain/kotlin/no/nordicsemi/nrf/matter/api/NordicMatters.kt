@file:OptIn(ExperimentalAtomicApi::class)

package no.nordicsemi.nrf.matter.api

import no.nordicsemi.nrf.matter.cluster.Cluster
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.logger.NordicLoggerBackend
import no.nordicsemi.nrf.matter.model.ClusterType
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceType
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.jvm.JvmInline

object NordicMatters {

    private val _fabrics = AtomicReference<List<Fabric>>(emptyList())

    val fabrics: List<Fabric>
        get() = _fabrics.load()

    //TODO for now only one Fabric is supported and it's always Fabric with id = 1
    val defaultFabric: Fabric
        get() {
            while (true) {
                val current = _fabrics.load()
                current.firstOrNull()?.let { return it }

                val fabric = newFabric(current)
                if (_fabrics.compareAndSet(current, listOf(fabric))) return fabric
            }
        }

    private var configuredPlatform: MatterPlatformDependencies? = null

    fun initialize(platform: MatterPlatformDependencies, loggerBackend: NordicLoggerBackend) {
        NordicLogger.setBackend(loggerBackend)
        configuredPlatform = platform
    }

    internal val platform: MatterPlatformDependencies
        get() = checkNotNull(configuredPlatform) { "NordicMatters.initialize() must be called before use." }

    internal val matterDependencies: MatterDependencies by lazy {
        MatterDependencies(platform)
    }

    val matterClient: MatterClient
        get() = matterDependencies.matterClient

    private fun newFabric(existing: List<Fabric>): Fabric {
        val id = existing.maxOfOrNull { it.id }?.plus(1) ?: FabricId(1)

        return Fabric(id, matterDependencies)
    }

    private val _customClusters = AtomicReference<Map<Long, ClusterDefinition>>(emptyMap())

    fun registerCustomCluster(clusterId: Long, clusterName: String, deviceType: DeviceType? = null, factory: (DeviceId, Int, MatterClient) -> Cluster) {
        val definition = ClusterDefinition(
            name = clusterName,
            deviceType = deviceType,
            factory = factory
        )

        val current = _customClusters.load()
        val updated = current + (clusterId to definition)

        val _ = _customClusters.compareAndSet(current, updated)
    }

    internal fun getCustomClusters(): Map<Long, ClusterDefinition> = _customClusters.load()

    fun parseDeviceType(type: Long): DeviceType {
        val clusterDefinition = _customClusters.load()
            .values
            .firstOrNull { it.deviceType?.id == type }

        return clusterDefinition?.deviceType ?: DeviceType.parse(type)
    }

    fun parseClusterName(id: Long): String {
        val clusterDefinition = _customClusters.load()[id]

        return clusterDefinition?.name ?: ClusterType.parse(id).name
    }
}

data class ClusterDefinition(
    val name: String,
    val deviceType: DeviceType?,
    val factory: (DeviceId, Int, MatterClient) -> Cluster,
)

@JvmInline
value class FabricId(val value: Int) : Comparable<FabricId> {

    override fun compareTo(other: FabricId): Int =
        value.compareTo(other.value)

    operator fun plus(other: Int): FabricId =
        FabricId(value + other)

    override fun toString(): String =
        value.toString()
}
