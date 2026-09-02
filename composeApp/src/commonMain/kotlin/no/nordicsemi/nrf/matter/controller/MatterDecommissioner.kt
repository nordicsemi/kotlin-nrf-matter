package no.nordicsemi.nrf.matter.controller

import no.nordicsemi.nrf.matter.model.DeviceId

internal interface MatterDecommissioner {

    suspend fun decommission(deviceId: DeviceId)
}
