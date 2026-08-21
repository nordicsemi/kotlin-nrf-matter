package no.nordicsemi.nrf.matter.binding

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.MatterGroup

interface MatterGroupDataSource {
    suspend fun save(group: MatterGroup)
    fun getAll(): Flow<List<MatterGroup>>
    suspend fun delete(groupId: Int)
}
