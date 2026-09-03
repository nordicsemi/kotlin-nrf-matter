@file:OptIn(ExperimentalAtomicApi::class)

package no.nordicsemi.nrf.matter.api

import no.nordicsemi.nrf.matter.cluster.MatterClient
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.jvm.JvmInline

/**
 * Entry point of the library.
 *
 * Everything the app does with Matter devices goes through a [Fabric] obtained from here, so the
 * repositories and the platform controllers behind them stay an implementation detail.
 *
 * The library builds its own object graph and has no dependency injection framework of its own -
 * an app is free to bring any, or none. Nothing has to be initialized on either platform: the
 * graph is built on first use, from an application context that the library's App Startup
 * initializer - merged into the app's manifest - hands it before any app component runs.
 *
 * The fabrics are held in an atomic rather than behind a lock, so this object is safe to touch
 * from any thread: two threads racing for [defaultFabric] get the same fabric back.
 */
object NordicMatters {

    private val _fabrics = AtomicReference<List<Fabric>>(emptyList())

    val fabrics: List<Fabric>
        get() = _fabrics.load()

    /**
     * The fabric the app commissions into, created on first access.
     *
     * The device store and the platform controllers are single fabric today, so this is the only
     * fabric an app normally uses; [createNewFabric] exists for when that changes.
     */
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

    /**
     * Reads and writes attributes and invokes commands on commissioned devices, for the cluster
     * level work the [Fabric] API does not cover.
     */
    val matterClient: MatterClient
        get() = matterDependencies.matterClient

    fun createNewFabric(): Fabric {
        while (true) {
            val current = _fabrics.load()
            val fabric = newFabric(current)

            if (_fabrics.compareAndSet(current, current + fabric)) return fabric
        }
    }

    /**
     * A fabric that would follow [existing], discarded by the caller if another thread got there
     * first. Cheap to build and inert until used, so losing the race costs nothing.
     */
    private fun newFabric(existing: List<Fabric>): Fabric {
        val id = existing.maxOfOrNull { it.id }?.plus(1) ?: FabricId(1)

        return Fabric(id, matterDependencies)
    }
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
