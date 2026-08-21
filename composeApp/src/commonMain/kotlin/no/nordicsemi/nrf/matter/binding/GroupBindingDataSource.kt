package no.nordicsemi.nrf.matter.binding

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.GroupBinding

interface GroupBindingDataSource {
    suspend fun save(binding: GroupBinding)

    fun getBindingsForDevice(deviceId: DeviceId): Flow<List<GroupBinding>>

    fun getAll(): Flow<List<GroupBinding>>

    suspend fun delete(binding: GroupBinding)
}
