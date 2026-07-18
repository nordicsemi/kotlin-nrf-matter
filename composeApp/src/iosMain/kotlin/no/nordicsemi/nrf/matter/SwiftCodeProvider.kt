package no.nordicsemi.nrf.matter

import no.nordicsemi.nrf.matter.device.OperationResult
import no.nordicsemi.nrf.matter.logger.IOSLogger
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId

interface SwiftCodeProvider {

    fun getMatterCommissioner(): MatterCommissioner

    fun getLogger(): IOSLogger
}

interface MatterCommissioner {

    suspend fun startIosCommissioning(deviceId: DeviceId): OperationResult<Device>
}
