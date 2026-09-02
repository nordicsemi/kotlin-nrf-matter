package no.nordicsemi.nrf.matter.controller

import no.nordicsemi.nrf.matter.model.DeviceId

internal interface BindingController {

    suspend fun bind(
        sourceNodeId: DeviceId,
        sourceEndpoint: Int,
        targetNodeId: DeviceId,
        targetEndpoint: Int,
        clusterId: Long
    )
}