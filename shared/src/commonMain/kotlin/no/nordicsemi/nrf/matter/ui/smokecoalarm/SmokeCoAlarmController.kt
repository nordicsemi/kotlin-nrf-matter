package no.nordicsemi.nrf.matter.ui.smokecoalarm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.cluster.SmokeCoAlarmCluster
import no.nordicsemi.nrf.matter.model.AlarmState
import no.nordicsemi.nrf.matter.model.ExpressedState
import no.nordicsemi.nrf.matter.ui.UiState
import no.nordicsemi.nrf.matter.ui.device.ClusterController
import kotlin.time.Duration.Companion.seconds

data class SmokeCoAlarmState(
    val expressedState: ExpressedState = ExpressedState.NORMAL,
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

    /** The device expresses Testing for as long as its self-test runs. */
    val isSelfTesting: Boolean
        get() = expressedState == ExpressedState.TESTING
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
        observe(cluster.observeExpressedState()) { state, value -> state.copy(expressedState = value.toExpressedState()) }
        observe(cluster.observeSmokeState()) { state, value -> state.copy(smokeState = value.toAlarmState()) }
        observe(cluster.observeCOState()) { state, value -> state.copy(coState = value.toAlarmState()) }
        observe(cluster.observeBatteryAlert()) { state, value -> state.copy(batteryAlert = value.toAlarmState()) }
        observe(cluster.observeDeviceMuted()) { state, value -> state.copy(isMuted = value.toInt() != 0) }
        observe(cluster.observeTestInProgress()) { state, value -> state.copy(isTestInProgress = value) }
        observe(cluster.observeHardwareFaultAlert()) { state, value -> state.copy(hasHardwareFault = value) }
        observe(cluster.observeEndOfServiceAlert()) { state, value -> state.copy(isEndOfService = value.toInt() != 0) }

        watchSelfTestDuration()
    }

    /**
     * Follows the self-test through the device's ExpressedState, which moves to Testing while the
     * test runs and back once it's done. Every change starts the self-test over from Idle, clearing
     * any earlier error so it can be run again. If the device stays in Testing for longer than
     * [SELF_TEST_TIMEOUT], give up so the user isn't stuck.
     */
    private fun watchSelfTestDuration() {
        scope.launch {
            _state.map { it.expressedState }
                .distinctUntilChanged()
                .collectLatest { expressedState ->
                    // A command still in flight will report its own result.
                    _selfTestState.update { it as? UiState.Loading ?: UiState.Idle() }

                    if (expressedState == ExpressedState.TESTING) {
                        delay(SELF_TEST_TIMEOUT)
                        _selfTestState.update { selfTestTimedOut }
                    }
                }
        }
    }

    private fun <T> observe(flow: Flow<T>, reducer: (SmokeCoAlarmState, T) -> SmokeCoAlarmState) {
        flow
            .onEach { value -> _state.update { reducer(it, value) } }
            .catch { /* attribute not supported by this device, keep the default */ }
            .launchIn(scope)
    }

    fun runSelfTest() {
        // Don't let an earlier timeout hide the fresh attempt's spinner.
        _selfTestState.update { UiState.Idle() }
        execute { cluster.selfTestRequest() }
            .withUiState()
            .map { if (it is UiState.Error) it.copy(message = it.cause.toSelfTestErrorMessage()) else it }
            .onEach { newState -> _selfTestState.update { newState } }
            .launchIn(scope)
    }

    companion object {
        val SELF_TEST_TIMEOUT = 30.seconds

        private val selfTestTimedOut = UiState.Error(
            "The self-test didn't finish within ${SELF_TEST_TIMEOUT.inWholeSeconds} seconds. Check the alarm, then try again."
        )
    }
}
