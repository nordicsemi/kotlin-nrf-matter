@file:OptIn(ExperimentalForeignApi::class)

package no.nordicsemi.nrf.matter.adapters

import iosMatter.LocalMatterCommissioner
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.MatterCommissioner
import no.nordicsemi.nrf.matter.model.DeviceId

internal class MatterCommissionerImpl : MatterCommissioner {

    private val localMatterCommissioner = LocalMatterCommissioner()

    override suspend fun commission(deviceId: DeviceId) {
        return suspendCancellableCoroutine { continuation ->
            localMatterCommissioner.startIosCommissioningWithDeviceId(deviceId.toNSNumber()) { error ->
                continuation.handleResult(error)
            }
        }
    }
}
