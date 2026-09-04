@file:OptIn(ExperimentalForeignApi::class)

package no.nordicsemi.nrf.matter.commission

import iosMatter.LocalMatterClusterDiscovery
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.adapters.IOSException
import no.nordicsemi.nrf.matter.adapters.toDomain
import no.nordicsemi.nrf.matter.adapters.toNSNumber
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Reads a commissioned device through `ios-matter`'s cluster discovery.
 */
internal class IosDeviceInfoProvider : DeviceInfoProvider {

    override suspend fun readDevice(deviceId: DeviceId): Device {
        val discovery = LocalMatterClusterDiscovery(nodeId = deviceId.toNSNumber())

        val device: iosMatter.Device = suspendCancellableCoroutine { continuation ->
            discovery.discoverClustersWithCompletionHandler { device, error ->
                val failure = error?.let { it.toCommissioningException() ?: IOSException(it) }

                when {
                    failure != null -> continuation.resumeWithException(failure)
                    device != null -> continuation.resume(device)
                    else -> continuation.resumeWithException(
                        CommissioningException.unknown(Stage.READ_BASIC_INFORMATION)
                    )
                }
            }
        }

        return device.toDomain()
    }
}
