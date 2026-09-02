package no.nordicsemi.nrf.matter.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import no.nordicsemi.nrf.matter.api.Fabric
import no.nordicsemi.nrf.matter.commission.CommissioningErrorScreen
import no.nordicsemi.nrf.matter.commission.CommissioningException
import no.nordicsemi.nrf.matter.commission.CommissioningInProgressScreen
import no.nordicsemi.nrf.matter.commission.rememberCommissioningTask
import no.nordicsemi.nrf.matter.logger.NordicLogger
import org.koin.compose.koinInject

sealed interface CommissioningScreenState {
    data object InProgress : CommissioningScreenState
    data class Error(val error: CommissioningException) : CommissioningScreenState
}

@Composable
fun CommissioningScreen(onBack: () -> Unit, navigateToLogs: () -> Unit) {
    val fabric: Fabric = koinInject()
    val state =
        remember { mutableStateOf<CommissioningScreenState>(CommissioningScreenState.InProgress) }

    val commissioningTask = rememberCommissioningTask(
        fabric = fabric,
        onSuccess = { deviceId ->
            fabric.commissionDevice(deviceId)
            onBack()
        },
        onError = {
            state.value = CommissioningScreenState.Error(it)
        },
    )

    // The screen is opened to commission a device, so the flow starts with it rather than on a
    // press.
    LaunchedEffect(commissioningTask) {
        commissioningTask.startCommissioning()
    }

    NordicLogger.info("State: ${state.value}")

    when (val state = state.value) {
        CommissioningScreenState.InProgress -> CommissioningInProgressScreen()
        is CommissioningScreenState.Error -> CommissioningErrorScreen(
            state.error,
            onBack,
            navigateToLogs
        )
    }
}
