package no.nordicsemi.nrf.matter.chip

import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.model.DeviceId

internal class MatterDecommissionerImpl(
    private val chipClient: ChipClient,
) : MatterDecommissioner {

    override suspend fun decommission(deviceId: DeviceId) {
        chipClient.decommissionDevice(deviceId.longValue)
    }
}
