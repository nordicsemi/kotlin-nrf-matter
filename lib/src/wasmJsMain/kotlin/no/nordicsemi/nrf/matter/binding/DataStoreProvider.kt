package no.nordicsemi.nrf.matter.binding

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.model.DeviceBinding
import no.nordicsemi.nrf.matter.model.DeviceId

/**
 * Web/demo actual: an in-memory binding store, no persistence needed for the documentation-site
 * demo this target exists for.
 */
actual class DataStoreProvider {
    internal actual fun createBindingDataSource(): BindingDataSource = InMemoryBindingDataSource()
}

private class InMemoryBindingDataSource : BindingDataSource {
    private val bindings = MutableStateFlow<List<DeviceBinding>>(emptyList())

    override suspend fun save(binding: DeviceBinding) {
        bindings.update { current -> current.filterNot { it.id == binding.id } + binding }
    }

    override fun getBindingsForDevice(deviceId: DeviceId): Flow<List<DeviceBinding>> =
        bindings.map { list -> list.filter { it.sourceNodeId == deviceId || it.targetNodeId == deviceId } }

    override fun getAll(): Flow<List<DeviceBinding>> = bindings.asStateFlow()

    override suspend fun delete(binding: DeviceBinding) {
        bindings.update { current -> current.filterNot { it.id == binding.id } }
    }
}
