package no.nordicsemi.nrf.matter.commission

import androidx.compose.runtime.Composable
import no.nordicsemi.nrf.matter.model.CommissioningInput
import no.nordicsemi.nrf.matter.model.Device

@Composable
expect fun CommissioningTask(
    input: CommissioningInput,
    onSuccess: (Device) -> Unit,
    onError: (CommissioningException) -> Unit,
)
