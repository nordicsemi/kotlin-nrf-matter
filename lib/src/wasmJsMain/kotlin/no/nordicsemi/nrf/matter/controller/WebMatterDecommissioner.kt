package no.nordicsemi.nrf.matter.controller

import kotlinx.coroutines.delay
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.webdemo.WebDemoAction
import no.nordicsemi.nrf.matter.webdemo.WebDemoEvents

/**
 * Web/demo actual: simulates the short delay of unlinking a fabric from a real accessory.
 */
internal class WebMatterDecommissioner : MatterDecommissioner {
    override suspend fun decommission(deviceId: DeviceId) {
        NordicLogger.info("Decommissioning device $deviceId", tag = "Decommission")
        delay(500)
        WebDemoEvents.publish(WebDemoAction.DeviceDecommissioned(deviceId))
    }
}
