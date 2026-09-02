package no.nordicsemi.nrf.matter.commission

import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId

/**
 * Reads back everything the app records about a commissioned device.
 *
 * Implemented per platform, because the reads go through the platform's Matter stack: the CHIP
 * device controller on Android, `MTRBaseDevice` on iOS.
 */
internal interface DeviceInfoProvider {

    /**
     * Reads the Basic Information cluster and the Descriptor cluster of every endpoint of the
     * device paired as [deviceId].
     *
     * @throws CommissioningException if the device does not answer, carrying the [Stage] that
     * failed.
     */
    suspend fun readDevice(deviceId: DeviceId): Device
}
