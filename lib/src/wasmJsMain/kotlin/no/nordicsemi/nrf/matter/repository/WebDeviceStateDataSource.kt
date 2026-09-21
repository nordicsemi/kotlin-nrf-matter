package no.nordicsemi.nrf.matter.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DevicesState

/**
 * Web/demo actual: an in-memory device-state list, no persistence needed for the
 * documentation-site demo this target exists for.
 */
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
