package no.nordicsemi.nrf.matter.ui.windowcovering

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.cluster.WindowCoveringCluster
import no.nordicsemi.nrf.matter.cluster.WindowCoveringClusterInfo.Feature
import no.nordicsemi.nrf.matter.ui.device.ClusterController

data class WindowCoveringState(
    val featureMap: Int = 0,
    /** 0 = fully open, 100 = fully closed. Null while unknown. */
    val liftPercent: Int? = null,
    /** 0 = fully open (horizontal), 100 = fully closed (vertical). Null while unknown. */
    val tiltPercent: Int? = null,
    /** Position the device was told to move to. Null when none is set. */
    val liftTargetPercent: Int? = null,
    val tiltTargetPercent: Int? = null,
) {
    fun supports(feature: Int) = featureMap and feature != 0

    // Until the FeatureMap is read, offer whatever the device reports a position for.
    val supportsLift get() = supports(Feature.LIFT) || (featureMap == 0 && liftPercent != null)
    val supportsTilt get() = supports(Feature.TILT) || (featureMap == 0 && tiltPercent != null)

    /** Where the covering is, or is heading to when it has a target. */
    val liftDestination get() = liftTargetPercent ?: liftPercent
    val tiltDestination get() = tiltTargetPercent ?: tiltPercent

    val isOpen: Boolean
        get() = (liftDestination ?: 0) < 100 || (tiltDestination ?: 0) < 100

    val summary: String
        get() = listOfNotNull(
            liftPercent?.let { "Lift: $it% closed" + movingSuffix(it, liftTargetPercent) },
            tiltPercent?.let { "Tilt: $it% closed" + movingSuffix(it, tiltTargetPercent) },
        ).joinToString(" · ").ifEmpty { "Window covering" }
}

/** Controls a device's Window Covering cluster (0x0102). */
class WindowCoveringController(
    private val cluster: WindowCoveringCluster,
    scope: CoroutineScope,
) : ClusterController(scope) {

    private val _state = MutableStateFlow(WindowCoveringState())
    val state = _state.asStateFlow()

    init {
        scope.launch {
            runCatching { cluster.featureMap() }
                .onSuccess { map -> _state.update { it.copy(featureMap = map) } }
        }
        observe(cluster.observeLiftPercentage()) { s, v -> s.copy(liftPercent = v?.toInt()?.coerceIn(0, 100)) }
        observe(cluster.observeTiltPercentage()) { s, v -> s.copy(tiltPercent = v?.toInt()?.coerceIn(0, 100)) }
        observe(cluster.observeTargetLiftPosition()) { s, v -> s.copy(liftTargetPercent = v?.toPercent()) }
        observe(cluster.observeTargetTiltPosition()) { s, v -> s.copy(tiltTargetPercent = v?.toPercent()) }
        observe(cluster.observeLiftPosition()) { s, v -> s.copy(liftPercent = v?.toPercent()) }
        observe(cluster.observeTiltPosition()) { s, v -> s.copy(tiltPercent = v?.toPercent()) }
    }

    private fun <T> observe(flow: Flow<T>, reducer: (WindowCoveringState, T) -> WindowCoveringState) {
        flow
            .onEach { value -> _state.update { reducer(it, value) } }
            .catch { /* attribute not supported by this device, keep the default */ }
            .launchIn(scope)
    }

    fun open() {
        _state.update { it.copy(liftTargetPercent = 0, tiltTargetPercent = 0) }
        send { cluster.open() }
    }

    fun close() {
        _state.update { it.copy(liftTargetPercent = 100, tiltTargetPercent = 100) }
        send { cluster.close() }
    }

    fun stop() {
        _state.update { it.copy(liftTargetPercent = it.liftPercent, tiltTargetPercent = it.tiltPercent) }
        send { cluster.stop() }
    }

    fun setLift(percent: Int) {
        _state.update { it.copy(liftTargetPercent = percent) }
        send { cluster.goToLiftPercentage(percent * 100) }
    }

    fun setTilt(percent: Int) {
        _state.update { it.copy(tiltTargetPercent = percent) }
        send { cluster.goToTiltPercentage(percent * 100) }
    }

    private fun send(action: suspend () -> Unit) {
        scope.launch { runCatching { action() } }
    }
}

private fun movingSuffix(current: Int, target: Int?) =
    if (target != null && target != current) " → $target%" else ""

private fun Number.toPercent(): Int = (toInt() / 100).coerceIn(0, 100)
