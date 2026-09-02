@file:OptIn(ExperimentalForeignApi::class)

package no.nordicsemi.nrf.matter.adapters

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.model.DeviceId
import iosMatter.LocalMatterBinder

internal class BindingControllerImpl : BindingController {
    private val binder = LocalMatterBinder()

    override suspend fun bind(
        sourceNodeId: DeviceId,
        sourceEndpoint: Int,
        targetNodeId: DeviceId,
        targetEndpoint: Int,
        clusterId: Long
    ) {
        return suspendCancellableCoroutine { continuation ->
            binder.bindWithSource(
                source = sourceNodeId.toNSNumber(),
                sourceEndpoint = sourceEndpoint.toNSNumber(),
                target = targetNodeId.toNSNumber(),
                targetEndpoint = targetEndpoint.toNSNumber(),
                cluster = clusterId.toNSNumber()
            ) { error ->
                continuation.handleResult(error)
            }
        }
    }
}
