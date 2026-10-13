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
    val isEnabled: Boolean = true,
) {
    fun supports(feature: Int) = featureMap and feature != 0

    val supportsHeating get() = supports(Feature.HEATING)
    val supportsCooling get() = supports(Feature.COOLING)
    val supportsAuto get() = supports(Feature.AUTO_MODE)

    /** Modes the user can select, derived from the supported features. Off is always available. */
    val selectableModes: List<ThermostatSystemMode>
        get() = buildList {
            add(ThermostatSystemMode.OFF)
            if (supportsAuto) add(ThermostatSystemMode.AUTO)
            if (supportsCooling) add(ThermostatSystemMode.COOL)
            if (supportsHeating) add(ThermostatSystemMode.HEAT)
        }

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
        send({ s -> s.copy(systemMode = mode) }) { cluster.setSystemMode(mode.value) }
    }

    fun adjustHeatingSetpoint(deltaCelsius: Float) {
        val target = ((_state.value.heatingSetpointCelsius ?: return) + deltaCelsius).coerceInRange()
        send({ s -> s.copy(heatingSetpointCelsius = target) }) { cluster.setHeatingSetpoint(target.toCentiDegrees()) }
    }

    fun adjustCoolingSetpoint(deltaCelsius: Float) {
        val target = ((_state.value.coolingSetpointCelsius ?: return) + deltaCelsius).coerceInRange()
        send({ s -> s.copy(coolingSetpointCelsius = target) }) { cluster.setCoolingSetpoint(target.toCentiDegrees()) }
    }

    /** Shows the new value right away, then writes it; the subscription restores the real value on failure. */
    private fun send(optimistic: (ThermostatState) -> ThermostatState, action: suspend () -> Unit) {
        _state.update(optimistic)
        scope.launch { runCatching { action() } }
    }

    companion object {
        const val SETPOINT_STEP_CELSIUS = 0.5f
        private const val MIN_SETPOINT_CELSIUS = 7f
        private const val MAX_SETPOINT_CELSIUS = 35f

        private fun Float.coerceInRange() = coerceIn(MIN_SETPOINT_CELSIUS, MAX_SETPOINT_CELSIUS)
    }
}

private fun Number.toCelsius(): Float = toFloat() / 100f

private fun Float.toCentiDegrees(): Int = kotlin.math.round(this * 100f).toInt()
