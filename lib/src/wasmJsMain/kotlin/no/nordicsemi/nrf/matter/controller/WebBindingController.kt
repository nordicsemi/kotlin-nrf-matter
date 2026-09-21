package no.nordicsemi.nrf.matter.controller

import kotlinx.coroutines.delay
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.webdemo.WebDemoAction
import no.nordicsemi.nrf.matter.webdemo.WebDemoEvents

/**
 * Web/demo actual: simulates writing an Access Control List entry and a Binding Table entry,
 * narrating the steps into [logs] the way the real chip log stream would.
 */
internal class WebBindingController(private val logs: WebBindingLogsProvider) : BindingController {
    override suspend fun bind(
        sourceNodeId: DeviceId,
        sourceEndpoint: Int,
        targetNodeId: DeviceId,
        targetEndpoint: Int,
        clusterId: Long,
    ) {
        WebDemoEvents.publish(WebDemoAction.BindingStarted)
        logs.logFlow.emit("Granting operate privilege in target's Access Control List...")
        delay(500)
        logs.logFlow.emit("Writing Binding Table entry on source node $sourceNodeId...")
        delay(700)
        logs.logFlow.emit("Binding written successfully.")
        WebDemoEvents.publish(WebDemoAction.BindingCompleted(sourceNodeId, targetNodeId))
    }
}
