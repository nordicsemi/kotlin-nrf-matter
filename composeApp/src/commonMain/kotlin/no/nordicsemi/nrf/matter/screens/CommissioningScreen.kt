package no.nordicsemi.nrf.matter.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import no.nordicsemi.nrf.matter.HomeViewModel
import no.nordicsemi.nrf.matter.commission.CommissioningErrorScreen
import no.nordicsemi.nrf.matter.commission.CommissioningException
import no.nordicsemi.nrf.matter.commission.CommissioningInProgressScreen
import no.nordicsemi.nrf.matter.commission.CommissioningTask
import no.nordicsemi.nrf.matter.logger.NordicLogger
import org.koin.compose.viewmodel.koinViewModel

sealed interface CommissioningScreenState {
    data object InProgress : CommissioningScreenState
    data class Error(val error: CommissioningException) : CommissioningScreenState
}

@Composable
fun CommissioningScreen(onBack: () -> Unit, navigateToLogs: () -> Unit) {
    val homeViewModel: HomeViewModel = koinViewModel()
    val state =
        remember { mutableStateOf<CommissioningScreenState>(CommissioningScreenState.InProgress) }

    CommissioningTask(
        onSuccess = {
            homeViewModel.addCommissionedDevice(device = it, true, false)
            onBack()
        },
        onError = {
            homeViewModel.commissioningFailed(1) //TODO result code
            state.value = CommissioningScreenState.Error(it)
        },
    )

    when (val state = state.value) {
        CommissioningScreenState.InProgress -> CommissioningInProgressScreen()
        is CommissioningScreenState.Error -> {
            NordicLogger.error(
                "Commission Error: ${state.error.displayMessage}",
                tag = "Commissioning"
            )
            CommissioningErrorScreen(
                state.error,
                onBack,
                navigateToLogs
            )
        }
    }
}
