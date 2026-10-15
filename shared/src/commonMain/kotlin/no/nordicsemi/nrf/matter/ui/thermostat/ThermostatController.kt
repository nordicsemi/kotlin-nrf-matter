package no.nordicsemi.nrf.matter.ui.thermostat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.cluster.ThermostatCluster
import no.nordicsemi.nrf.matter.cluster.ThermostatClusterInfo.Feature
import no.nordicsemi.nrf.matter.model.ThermostatSystemMode
import no.nordicsemi.nrf.matter.ui.device.ClusterController

data class ThermostatState(
    val featureMap: Int = 0,
    val localTemperatureCelsius: Float? = null,
    val heatingSetpointCelsius: Float? = null,
    val coolingSetpointCelsius: Float? = null,
    val systemMode: ThermostatSystemMode = ThermostatSystemMode.OFF,
    val heatingLimitsCelsius: ClosedFloatingPointRange<Float> = ThermostatController.DEFAULT_LIMITS_CELSIUS,
    val coolingLimitsCelsius: ClosedFloatingPointRange<Float> = ThermostatController.DEFAULT_LIMITS_CELSIUS,
    val isEnabled: Boolean = true,
) {
    fun supports(feature: Int) = featureMap and feature != 0

    val supportsHeating get() = supports(Feature.HEATING)
    val supportsCooling get() = supports(Feature.COOLING)
    val supportsAuto get() = supports(Feature.AUTO_MODE)

    /** Modes the user can select: every defined SystemMode value. */
    val selectableModes: List<ThermostatSystemMode>
        get() = ThermostatSystemMode.entries.filter { it != ThermostatSystemMode.OTHER }

    val isActive: Boolean
        get() = systemMode != ThermostatSystemMode.OFF
}

/**
 * Controls a device's Thermostat cluster (0x0201). The FeatureMap decides which controls are
 * offered; attributes a device doesn't support are simply left at their default.
 */
class ThermostatController(
    private val cluster: ThermostatCluster,
    scope: CoroutineScope,
) : ClusterController(scope) {

    private val _state = MutableStateFlow(ThermostatState())
    val state = _state.asStateFlow()

    init {
        scope.launch {
            runCatching { cluster.featureMap() }
                .onSuccess { map -> _state.update { it.copy(featureMap = map) } }
        }
        scope.launch {
            runCatching { cluster.heatingSetpointLimits().toRange() }
                .onSuccess { r -> _state.update { it.copy(heatingLimitsCelsius = r) } }
        }
        scope.launch {
            runCatching { cluster.coolingSetpointLimits().toRange() }
                .onSuccess { r -> _state.update { it.copy(coolingLimitsCelsius = r) } }
        }
        observe(cluster.observeLocalTemperature()) { s, v -> s.copy(localTemperatureCelsius = v.toCelsius()) }
        observe(cluster.observeHeatingSetpoint()) { s, v -> s.copy(heatingSetpointCelsius = v.toCelsius()) }
        observe(cluster.observeCoolingSetpoint()) { s, v -> s.copy(coolingSetpointCelsius = v.toCelsius()) }
        observe(cluster.observeSystemMode()) { s, v -> s.copy(systemMode = ThermostatSystemMode.from(v.toInt())) }
    }

    private fun <T> observe(flow: Flow<T>, reducer: (ThermostatState, T) -> ThermostatState) {
        flow
            .onEach { value -> _state.update { reducer(it, value) } }
            .catch { /* attribute not supported by this device, keep the default */ }
            .launchIn(scope)
    }

    fun setSystemMode(mode: ThermostatSystemMode) {
        val previous = _state.value.systemMode
        send(
            optimistic = { it.copy(systemMode = mode) },
            revert = { s -> if (s.systemMode == mode) s.copy(systemMode = previous) else s },
        ) { cluster.setSystemMode(mode.value) }
    }

    fun adjustHeatingSetpoint(deltaCelsius: Float) {
        val current = _state.value.heatingSetpointCelsius ?: return
        val target = (current + deltaCelsius).coerceIn(_state.value.heatingLimitsCelsius)
        if (target == current) return
        send(
            optimistic = { it.copy(heatingSetpointCelsius = target) },
            revert = { s -> if (s.heatingSetpointCelsius == target) s.copy(heatingSetpointCelsius = current) else s },
        ) { cluster.setHeatingSetpoint(target.toCentiDegrees()) }
    }

    fun adjustCoolingSetpoint(deltaCelsius: Float) {
        val current = _state.value.coolingSetpointCelsius ?: return
        val target = (current + deltaCelsius).coerceIn(_state.value.coolingLimitsCelsius)
        if (target == current) return
        send(
            optimistic = { it.copy(coolingSetpointCelsius = target) },
            revert = { s -> if (s.coolingSetpointCelsius == target) s.copy(coolingSetpointCelsius = current) else s },
        ) { cluster.setCoolingSetpoint(target.toCentiDegrees()) }
    }

    /**
     * Shows the new value right away, then writes it. If the write fails the previous value is restored,
     * unless a newer value (e.g. from the subscription) has replaced the optimistic one in the meantime.
     */
    private fun send(
        optimistic: (ThermostatState) -> ThermostatState,
        revert: (ThermostatState) -> ThermostatState,
        action: suspend () -> Unit,
    ) {
        _state.update(optimistic)
        scope.launch {
            runCatching { action() }.onFailure { _state.update(revert) }
        }
    }

    companion object {
        const val SETPOINT_STEP_CELSIUS = 0.5f
        val DEFAULT_LIMITS_CELSIUS = 7f..35f
    }
}

private fun Pair<Int, Int>.toRange(): ClosedFloatingPointRange<Float> = (first / 100f)..(second / 100f)

private fun Float.coerceIn(range: ClosedFloatingPointRange<Float>) = coerceIn(range.start, range.endInclusive)

private fun Number.toCelsius(): Float = toFloat() / 100f

private fun Float.toCentiDegrees(): Int = kotlin.math.round(this * 100f).toInt()
