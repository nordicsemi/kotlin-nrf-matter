package no.nordicsemi.nrf.matter.controller

import kotlinx.coroutines.delay
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.events.AppEvent
import no.nordicsemi.nrf.matter.events.AppEvents

internal class WebBindingController(private val logs: WebBindingLogsProvider) : BindingController {
    override suspend fun bind(
        sourceNodeId: DeviceId,
        sourceEndpoint: Int,
        targetNodeId: DeviceId,
        targetEndpoint: Int,
        clusterId: Long,
    ) {
        AppEvents.publish(AppEvent.BindingStarted)
        logs.logFlow.emit("Granting operate privilege in target's Access Control List...")
        delay(500)
        logs.logFlow.emit("Writing Binding Table entry on source node $sourceNodeId...")
        delay(700)
        logs.logFlow.emit("Binding written successfully.")
        AppEvents.publish(AppEvent.BindingCompleted(sourceNodeId, targetNodeId))
    }
}
