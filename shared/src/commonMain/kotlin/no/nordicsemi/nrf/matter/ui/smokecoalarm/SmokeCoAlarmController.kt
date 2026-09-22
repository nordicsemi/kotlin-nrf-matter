package no.nordicsemi.nrf.matter.ui.smokecoalarm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.cluster.SmokeCoAlarmCluster
import no.nordicsemi.nrf.matter.model.AlarmState
import no.nordicsemi.nrf.matter.ui.UiState
import no.nordicsemi.nrf.matter.ui.device.ClusterController

data class SmokeCoAlarmState(
    val smokeState: AlarmState = AlarmState.NORMAL,
    val coState: AlarmState = AlarmState.NORMAL,
    val batteryAlert: AlarmState = AlarmState.NORMAL,
    val isMuted: Boolean = false,
    val isTestInProgress: Boolean = false,
    val hasHardwareFault: Boolean = false,
    val isEndOfService: Boolean = false,
) {
    val isAlarmActive: Boolean
        get() = smokeState != AlarmState.NORMAL || coState != AlarmState.NORMAL
}

/**
 * Controls a device's Smoke CO Alarm cluster (0x005C). Not every attribute is supported by
 * every device (e.g. a CO-only alarm has no SmokeState), so each attribute is observed
 * independently and a device that doesn't support one simply keeps that field at its default.
 */
class SmokeCoAlarmController(
    private val cluster: SmokeCoAlarmCluster,
    scope: CoroutineScope,
) : ClusterController(scope) {

    private val _state = MutableStateFlow(SmokeCoAlarmState())
    val state = _state.asStateFlow()

    private val _selfTestState = MutableStateFlow<UiState<Unit>>(UiState.Idle())
    val selfTestState = _selfTestState.asStateFlow()

    init {
        observe(cluster.observeSmokeState()) { state, value -> state.copy(smokeState = value.toAlarmState()) }
        observe(cluster.observeCOState()) { state, value -> state.copy(coState = value.toAlarmState()) }
        observe(cluster.observeBatteryAlert()) { state, value -> state.copy(batteryAlert = value.toAlarmState()) }
        observe(cluster.observeDeviceMuted()) { state, value -> state.copy(isMuted = value.toInt() != 0) }
        observe(cluster.observeTestInProgress()) { state, value -> state.copy(isTestInProgress = value) }
        observe(cluster.observeHardwareFaultAlert()) { state, value -> state.copy(hasHardwareFault = value) }
        observe(cluster.observeEndOfServiceAlert()) { state, value -> state.copy(isEndOfService = value.toInt() != 0) }
    }

    private fun <T> observe(flow: Flow<T>, reducer: (SmokeCoAlarmState, T) -> SmokeCoAlarmState) {
        flow
            .onEach { value -> _state.update { reducer(it, value) } }
            .catch { /* attribute not supported by this device, keep the default */ }
            .launchIn(scope)
    }

    fun runSelfTest() {
        execute { cluster.selfTestRequest() }
            .withUiState()
            .onEach { newState -> _selfTestState.update { newState } }
            .launchIn(scope)
    }
}