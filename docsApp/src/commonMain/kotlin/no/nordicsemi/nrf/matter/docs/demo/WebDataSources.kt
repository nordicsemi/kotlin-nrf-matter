package no.nordicsemi.nrf.matter.docs.demo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.binding.BindingDataSource
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.model.DeviceBinding
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.Devices
import no.nordicsemi.nrf.matter.model.DevicesState

internal class WebDevicesDataSource : DevicesDataSource {
    private val state = MutableStateFlow(Devices())

    override val devicesFlow: Flow<Devices> = state.asStateFlow()

    override suspend fun update(transform: (Devices) -> Devices) {
        state.update(transform)
    }

    override suspend fun removeDevice(deviceId: DeviceId) {
        state.update { current ->
            current.copy(devicesList = current.devicesList.filterNot { it.deviceId == deviceId })
        }
    }
}

internal class WebDeviceStateDataSource : DeviceStateDataSource {
    private val state = MutableStateFlow(DevicesState())

    override val devicesFlow: Flow<DevicesState> = state.asStateFlow()

    override suspend fun update(transform: (DevicesState) -> DevicesState) {
        state.update(transform)
    }

    override suspend fun removeDevice(deviceId: DeviceId) {
        state.update { current ->
            current.copy(devicesStateList = current.devicesStateList.filterNot { it.deviceId == deviceId })
        }
    }
}

internal class WebBindingDataSource : BindingDataSource {
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
