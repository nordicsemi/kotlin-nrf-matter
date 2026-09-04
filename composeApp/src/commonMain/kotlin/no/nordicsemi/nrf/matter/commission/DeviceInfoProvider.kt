package no.nordicsemi.nrf.matter.commission

import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId

internal interface DeviceInfoProvider {

    suspend fun readDevice(deviceId: DeviceId): Device
}
