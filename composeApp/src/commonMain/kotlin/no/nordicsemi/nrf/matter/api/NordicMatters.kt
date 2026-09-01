package no.nordicsemi.nrf.matter.api

import kotlin.jvm.JvmInline

object NordicMatters {

    private var _fabrics = mutableListOf<Fabric>()
    val fabrics: List<Fabric>
        get() {
            return _fabrics.toList()
        }

    fun createNewFabric(): Fabric {
        return Fabric(getNextFabricId())
            .also { _fabrics.add(it) }
    }

    private fun getNextFabricId(): FabricId {
        return _fabrics.maxOf { it.id } + 1
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


@JvmInline
value class NodeId(val value: Int) : Comparable<NodeId> {

    override fun compareTo(other: NodeId): Int =
        value.compareTo(other.value)

    operator fun plus(other: Int): NodeId =
        NodeId(value + other)

    override fun toString(): String =
        value.toString()
}

class Fabric(val id: FabricId) {

    fun commissionDevice(nodeId: NodeId) {

    }
}

