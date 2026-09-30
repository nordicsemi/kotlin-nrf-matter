package no.nordicsemi.nrf.matter.docs.demo

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.events.AppEvent
import no.nordicsemi.nrf.matter.events.AppEvents
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId

internal class WebBindingLogsProvider : BindingLogsProvider {
    val logFlow = MutableSharedFlow<String>(extraBufferCapacity = 64)
    override val bindingLogs: Flow<String> = logFlow
}

internal class WebBindingController(private val logs: WebBindingLogsProvider) : BindingController {
    override suspend fun bind(
        sourceNodeId: DeviceId,
        sourceEndpoint: Int,
        targetNodeId: DeviceId,
        targetEndpoint: Int,
        clusterId: Long,
    ) {
        AppEvents.emit(AppEvent.BindingStarted)
        logs.logFlow.emit("Granting operate privilege in target's Access Control List...")
        delay(500)
        logs.logFlow.emit("Writing Binding Table entry on source node $sourceNodeId...")
        delay(700)
        logs.logFlow.emit("Binding written successfully.")
        AppEvents.emit(AppEvent.BindingCompleted(sourceNodeId, targetNodeId))
    }
}

internal class WebMatterDecommissioner : MatterDecommissioner {
    override suspend fun decommission(deviceId: DeviceId) {
        NordicLogger.info("Decommissioning device $deviceId", tag = "Decommission")
        delay(500)
        AppEvents.emit(AppEvent.DeviceDecommissioned(deviceId))
    }
}
