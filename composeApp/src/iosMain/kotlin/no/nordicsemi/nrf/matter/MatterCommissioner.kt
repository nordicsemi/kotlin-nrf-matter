package no.nordicsemi.nrf.matter

import no.nordicsemi.nrf.matter.model.DeviceId

internal interface MatterCommissioner {

    suspend fun commission(deviceId: DeviceId)
}
