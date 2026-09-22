package no.nordicsemi.nrf.matter.ui.rvc

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.cluster.RvcOperationalStateCluster
import no.nordicsemi.nrf.matter.model.RvcOperationalState
import no.nordicsemi.nrf.matter.ui.UiState
import no.nordicsemi.nrf.matter.ui.device.ClusterController

data class RvcOperationalStateData(
    val state: RvcOperationalState?,
    val currentPhase: Int?,
    val countdownTimeSeconds: Int?,
)

class RvcOperationalStateController(
    private val cluster: RvcOperationalStateCluster,
    scope: CoroutineScope,
) : ClusterController(scope) {

    private val _state = MutableStateFlow<UiState<RvcOperationalStateData>>(UiState.Loading())
    val state = _state.asStateFlow()

    init {
        combine(
            cluster.observeOperationalState(),
            cluster.observeCurrentPhase(),
            cluster.observeCountdownTime(),
        ) { operationalState, currentPhase, countdownTime ->
            RvcOperationalStateData(
                state = operationalState.toInt().toRvcOperationalState(),
                currentPhase = currentPhase?.toInt(),
                countdownTimeSeconds = countdownTime?.toInt(),
            )
        }
            .onEach { data -> _state.update { UiState.Success(data) } }
            .launchIn(scope)
    }

    fun pause() = runCommand { cluster.pause() }
    fun resume() = runCommand { cluster.resume() }
    fun start() = runCommand { cluster.start() }
    fun stop() = runCommand { cluster.stop() }
    fun goHome() = runCommand { cluster.goHome() }

    private fun runCommand(action: suspend () -> Unit) {
        execute(action).catch { it.printStackTrace() }.launchIn(scope)
    }
}
