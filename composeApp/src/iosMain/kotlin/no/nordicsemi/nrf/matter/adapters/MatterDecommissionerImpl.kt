@file:OptIn(ExperimentalForeignApi::class)

package no.nordicsemi.nrf.matter.adapters

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.model.DeviceId
import iosMatter.LocalMatterDecommissioner

internal class MatterDecommissionerImpl : MatterDecommissioner {
    private val controller = LocalMatterDecommissioner()

    override suspend fun decommission(deviceId: DeviceId) {
        return suspendCancellableCoroutine { continuation ->
            controller.decommissionWithDeviceId(deviceId.toNSNumber()) { error ->
                continuation.handleResult(error)
            }
        }
    }
}
