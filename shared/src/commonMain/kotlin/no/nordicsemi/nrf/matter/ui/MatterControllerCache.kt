package no.nordicsemi.nrf.matter.ui

import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceUiModel
import no.nordicsemi.nrf.matter.ui.device.DeviceViewModel
import org.koin.core.component.KoinComponent

class MatterControllerCache : KoinComponent {

    private val controllerCache = mutableMapOf<DeviceId, MatterController>()

    operator fun get(id: DeviceId): MatterController? {
        return controllerCache[id]
    }

    fun create(device: DeviceUiModel): MatterController {
        return DeviceViewModel(device).also {
            controllerCache[device.device.deviceId] = it
        }
    }
}
