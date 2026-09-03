package no.nordicsemi.nrf.matter.ui

import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceUiModel
import no.nordicsemi.nrf.matter.ui.device.DeviceViewModel

/**
 * Keeps one [DeviceViewModel] per device, so the cluster view models behind it - and the attribute
 * subscriptions they run - survive every emission of the device list rather than being rebuilt.
 */
class DeviceViewModelCache {

    private val cache = mutableMapOf<DeviceId, DeviceViewModel>()

    operator fun get(id: DeviceId): DeviceViewModel? {
        return cache[id]
    }

    fun create(device: DeviceUiModel): DeviceViewModel {
        return DeviceViewModel(device).also {
            cache[device.device.deviceId] = it
        }
    }

    /**
     * Drops everything cached for a device not in [ids] - decommissioned, or gone for any other
     * reason - cancelling its cluster scopes on the way out. Without this the map only ever grows.
     */
    fun retainOnly(ids: Set<DeviceId>) {
        val stale = cache.keys - ids

        stale.forEach { cache.remove(it)?.clear() }
    }
}
