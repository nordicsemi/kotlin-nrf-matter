package no.nordicsemi.nrf.matter.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.Devices

/**
 * Web/demo actual: an in-memory device list, no persistence needed for the documentation-site
 * demo this target exists for.
 */
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
