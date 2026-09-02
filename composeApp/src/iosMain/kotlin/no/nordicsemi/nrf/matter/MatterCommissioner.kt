package no.nordicsemi.nrf.matter

import no.nordicsemi.nrf.matter.model.DeviceId

internal interface MatterCommissioner {

    /**
     * Runs the system add-device flow and pairs the device it finds as [deviceId].
     *
     * Reading the device back is a separate step - see
     * [no.nordicsemi.nrf.matter.commission.DeviceInfoProvider].
     *
     * @throws no.nordicsemi.nrf.matter.commission.CommissioningException if the flow fails or the
     * user cancels it.
     */
    suspend fun commission(deviceId: DeviceId)
}
