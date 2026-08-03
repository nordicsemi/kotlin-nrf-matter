package no.nordicsemi.nrf.matter.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import no.nordicsemi.nrf.matter.HomeViewModel
import no.nordicsemi.nrf.matter.commission.CommissioningErrorScreen
import no.nordicsemi.nrf.matter.commission.CommissioningException
import no.nordicsemi.nrf.matter.commission.CommissioningInProgressScreen
import no.nordicsemi.nrf.matter.model.CommissioningInput
import no.nordicsemi.nrf.matter.commission.CommissioningSetupScreen
import no.nordicsemi.nrf.matter.commission.CommissioningTask
import no.nordicsemi.nrf.matter.logger.NordicLogger
import org.koin.compose.viewmodel.koinViewModel

sealed interface CommissioningScreenState {
    /** Collecting the setup code and network credentials from the user. */
    data object CollectingInput : CommissioningScreenState

    /** Commissioning is running with the collected [input]. */
    data class InProgress(val input: CommissioningInput) : CommissioningScreenState

    data class Error(val error: CommissioningException) : CommissioningScreenState
}

@Composable
fun CommissioningScreen(onBack: () -> Unit, navigateToLogs: () -> Unit) {
    val homeViewModel: HomeViewModel = koinViewModel()
    val state = remember {
        mutableStateOf<CommissioningScreenState>(CommissioningScreenState.CollectingInput)
    }

    NordicLogger.info("State: ${state.value}")

    when (val current = state.value) {
        CommissioningScreenState.CollectingInput -> CommissioningSetupScreen(
            onCommission = { input ->
                state.value = CommissioningScreenState.InProgress(input)
            },
        )

        is CommissioningScreenState.InProgress -> {
            CommissioningTask(
                input = current.input,
                onSuccess = {
                    homeViewModel.addCommissionedDevice(device = it, true, false)
                    onBack()
                },
                onError = {
                    homeViewModel.commissioningFailed(1) //TODO result code
                    state.value = CommissioningScreenState.Error(it)
                },
            )
            CommissioningInProgressScreen()
        }

        is CommissioningScreenState.Error -> CommissioningErrorScreen(
            current.error,
            onBack,
            navigateToLogs
        )
    }
}
