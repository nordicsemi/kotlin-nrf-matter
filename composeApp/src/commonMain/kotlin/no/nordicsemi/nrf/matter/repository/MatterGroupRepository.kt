package no.nordicsemi.nrf.matter.repository

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.binding.MatterGroupDataSource
import no.nordicsemi.nrf.matter.model.MatterGroup

class MatterGroupRepository(
    private val localDataSource: MatterGroupDataSource,
) {
    suspend fun save(group: MatterGroup) {
        localDataSource.save(group)
    }

    fun getAll(): Flow<List<MatterGroup>> {
        return localDataSource.getAll()
    }

    suspend fun delete(groupId: Int) {
        localDataSource.delete(groupId)
    }
}
