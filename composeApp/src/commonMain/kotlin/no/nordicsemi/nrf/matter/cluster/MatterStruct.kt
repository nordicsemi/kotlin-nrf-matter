package no.nordicsemi.nrf.matter.cluster

/**
 * A Matter structure, as it arrives from a cluster agnostic attribute read.
 *
 * Matter carries no field names on the wire: a field is addressed by the context tag its cluster's
 * schema gives it - 0 for `DeviceType` and 1 for `Revision` in a `DeviceTypeStruct`, say. Both
 * platform [MatterClient] implementations normalize a structure into this, so a [Cluster] reading
 * one does not have to know how the platform decoded it.
 */
data class MatterStruct(val fields: Map<Long, Any?>) {

    /** The field with this context tag, or `null` if the device did not report it. */
    operator fun get(contextTag: Long): Any? = fields[contextTag]

    /**
     * The field with this context tag as a [Long].
     *
     * Matter integers arrive with their signedness known but not their width, so every numeric
     * field is widened to [Long] rather than read at its schema type.
     */
    fun longOrNull(contextTag: Long): Long? = (fields[contextTag] as? Number)?.toLong()
}
