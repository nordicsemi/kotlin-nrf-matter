@file:OptIn(ExperimentalAtomicApi::class)

package no.nordicsemi.nrf.matter.api

import no.nordicsemi.nrf.matter.cluster.Cluster
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.model.DeviceId
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

    internal val matterDependencies: MatterDependencies by lazy {
        MatterDependenciesProvider.createMatterDependencies()
    }

    val matterClient: MatterClient
        get() = matterDependencies.matterClient

    private fun newFabric(existing: List<Fabric>): Fabric {
        val id = existing.maxOfOrNull { it.id }?.plus(1) ?: FabricId(1)

        return Fabric(id, matterDependencies)
    }

    private val _customClusters =
        AtomicReference<Map<Long, Pair<Long?, (DeviceId, Int, MatterClient) -> Cluster>>>(emptyMap())

    fun registerCustomCluster(clusterId: Long, deviceType: Long? = null, factory: (DeviceId, Int, MatterClient) -> Cluster) {
        while (true) {
            val current = _customClusters.load()
            val updated = current + (clusterId to (deviceType to factory))

            if (_customClusters.compareAndSet(current, updated)) return
        }
    }

    internal fun getCustomClusters(): Map<Long, Pair<Long?, (DeviceId, Int, MatterClient) -> Cluster>> =
        _customClusters.load()
}

@JvmInline
value class FabricId(val value: Int) : Comparable<FabricId> {

    override fun compareTo(other: FabricId): Int =
        value.compareTo(other.value)

    operator fun plus(other: Int): FabricId =
        FabricId(value + other)

    override fun toString(): String =
        value.toString()
}
