package no.nordicsemi.nrf.matter.ui

import androidx.compose.runtime.Composable
import no.nordicsemi.nrf.matter.model.DeviceId

interface MatterController {

    @Composable
    fun Item(onDecommission: (DeviceId) -> Unit)
}
