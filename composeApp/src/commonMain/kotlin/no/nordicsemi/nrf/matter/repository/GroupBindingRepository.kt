package no.nordicsemi.nrf.matter.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import no.nordicsemi.nrf.matter.binding.GroupBindingDataSource
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.GroupBinding

class GroupBindingRepository(
    private val localDataSource: GroupBindingDataSource,
) {
    suspend fun save(binding: GroupBinding) {
        localDataSource.save(binding)
    }

    fun getTargetsForDevice(deviceId: DeviceId): Flow<List<GroupBinding>> {
        return localDataSource.getBindingsForDevice(deviceId)
    }

    fun getAllBinding(): Flow<List<GroupBinding>> {
        return localDataSource.getAll()
    }

    suspend fun delete(deviceId: DeviceId) {
        localDataSource.getBindingsForDevice(deviceId)
            .first()
            .forEach {
                localDataSource.delete(it)
            }
    }
}
