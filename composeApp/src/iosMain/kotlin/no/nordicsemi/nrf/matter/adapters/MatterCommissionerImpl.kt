@file:OptIn(ExperimentalForeignApi::class)

package no.nordicsemi.nrf.matter.adapters

import iosMatter.LocalMatterCommissioner
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.MatterCommissioner
import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.api.commissioningRooms
import no.nordicsemi.nrf.matter.model.DeviceId

internal class MatterCommissionerImpl : MatterCommissioner {

    private val localMatterCommissioner = LocalMatterCommissioner()

    /**
     * Runs the system "Add Device" flow for [deviceId], then remembers the name the user chose in
     * it.
     *
     * The name is applied here, in the app's process, rather than by the extension that collected
     * it: the extension's own [no.nordicsemi.nrf.matter.api.Fabric] writes to a container the app
     * never reads. This mirrors what Android's `CommissioningTask` does with the name its own
     * commissioning service returns, and leaves the name to be picked up by the
     * `Fabric.commissionDevice` call the caller makes next.
     */
    override suspend fun commission(deviceId: DeviceId) {
        val name = suspendCancellableCoroutine { continuation ->
            localMatterCommissioner.startIosCommissioningWithDeviceId(
                deviceId = deviceId.toNSNumber(),
                rooms = NordicMatters.commissioningRooms,
            ) { name, error ->
                continuation.handleNullableResult(error, name)
            }
        }

        NordicMatters.matterDependencies.finaliseCommissioningUseCase.rememberName(deviceId, name)
    }
}
