package no.nordicsemi.nrf.matter.cluster

data class MatterStruct(val fields: Map<Long, Any?>) {

    operator fun get(contextTag: Long): Any? = fields[contextTag]

    fun longOrNull(contextTag: Long): Long? = (fields[contextTag] as? Number)?.toLong()
}
