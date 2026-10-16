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
) {
    fun supports(feature: Int) = featureMap and feature != 0

    // Until the FeatureMap is read, offer whatever the device reports a position for.
    val supportsLift get() = supports(Feature.LIFT) || (featureMap == 0 && liftPercent != null)
    val supportsTilt get() = supports(Feature.TILT) || (featureMap == 0 && tiltPercent != null)

    val isOpen: Boolean
        get() = (liftPercent ?: 0) < 100 || (tiltPercent ?: 0) < 100

    val summary: String
        get() = listOfNotNull(
            liftPercent?.let { "Lift: $it% closed" },
            tiltPercent?.let { "Tilt: $it% closed" },
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
        observe(cluster.observeLiftPosition()) { s, v -> s.copy(liftPercent = v?.toPercent()) }
        observe(cluster.observeTiltPosition()) { s, v -> s.copy(tiltPercent = v?.toPercent()) }
    }

    private fun <T> observe(flow: Flow<T>, reducer: (WindowCoveringState, T) -> WindowCoveringState) {
        flow
            .onEach { value -> _state.update { reducer(it, value) } }
            .catch { /* attribute not supported by this device, keep the default */ }
            .launchIn(scope)
    }

    fun open() = send { cluster.open() }

    fun close() = send { cluster.close() }

    fun stop() = send { cluster.stop() }

    fun setLift(percent: Int) {
        _state.update { it.copy(liftPercent = percent) }
        send { cluster.goToLiftPercentage(percent * 100) }
    }

    fun setTilt(percent: Int) {
        _state.update { it.copy(tiltPercent = percent) }
        send { cluster.goToTiltPercentage(percent * 100) }
    }

    private fun send(action: suspend () -> Unit) {
        scope.launch { runCatching { action() } }
    }
}

private fun Number.toPercent(): Int = (toInt() / 100).coerceIn(0, 100)
