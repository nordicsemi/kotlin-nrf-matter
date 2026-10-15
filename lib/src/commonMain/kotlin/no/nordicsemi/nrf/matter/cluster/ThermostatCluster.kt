package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

object ThermostatClusterInfo {
    const val ID: Long = 0x0201

    object Attribute {
        const val LOCAL_TEMPERATURE: Long = 0x0000
        const val ABS_MIN_HEAT_SETPOINT_LIMIT: Long = 0x0003
        const val ABS_MAX_HEAT_SETPOINT_LIMIT: Long = 0x0004
        const val ABS_MIN_COOL_SETPOINT_LIMIT: Long = 0x0005
        const val ABS_MAX_COOL_SETPOINT_LIMIT: Long = 0x0006
        const val OCCUPIED_COOLING_SETPOINT: Long = 0x0011
        const val OCCUPIED_HEATING_SETPOINT: Long = 0x0012
        const val SYSTEM_MODE: Long = 0x001C
        const val MIN_HEAT_SETPOINT_LIMIT: Long = 0x0015
        const val MAX_HEAT_SETPOINT_LIMIT: Long = 0x0016
        const val MIN_COOL_SETPOINT_LIMIT: Long = 0x0017
        const val MAX_COOL_SETPOINT_LIMIT: Long = 0x0018
        const val FEATURE_MAP: Long = 0xFFFC
    }

    /** Bit positions of the Thermostat cluster FeatureMap. */
    object Feature {
        const val HEATING: Int = 1 shl 0
        const val COOLING: Int = 1 shl 1
        const val OCCUPANCY: Int = 1 shl 2
        const val SCHEDULE_CONFIGURATION: Int = 1 shl 3
        const val SETBACK: Int = 1 shl 4
        const val AUTO_MODE: Int = 1 shl 5
        const val LOCAL_TEMPERATURE_NOT_EXPOSED: Int = 1 shl 6
    }
}

class ThermostatCluster(
    deviceId: DeviceId,
    endpoint: Int,
    controller: MatterClient,
) : Cluster(deviceId, endpoint, ThermostatClusterInfo.ID, controller) {

    /** Reads the raw FeatureMap bitmap, see [ThermostatClusterInfo.Feature]. */
    suspend fun featureMap(): Int = readAttribute<Number>(ThermostatClusterInfo.Attribute.FEATURE_MAP).toInt()

    /** Emits the local temperature in 0.01 °C. Not supported with the LocalTemperatureNotExposed feature. */
    fun observeLocalTemperature(): Flow<Number> =
        observeAttribute(ThermostatClusterInfo.Attribute.LOCAL_TEMPERATURE)

    /** Emits the raw SystemMode value, see [no.nordicsemi.nrf.matter.model.ThermostatSystemMode]. */
    fun observeSystemMode(): Flow<Number> = observeAttribute(ThermostatClusterInfo.Attribute.SYSTEM_MODE)

    /** Emits the heating setpoint in 0.01 °C. Requires the Heating feature. */
    fun observeHeatingSetpoint(): Flow<Number> =
        observeAttribute(ThermostatClusterInfo.Attribute.OCCUPIED_HEATING_SETPOINT)

    /** Emits the cooling setpoint in 0.01 °C. Requires the Cooling feature. */
    fun observeCoolingSetpoint(): Flow<Number> =
        observeAttribute(ThermostatClusterInfo.Attribute.OCCUPIED_COOLING_SETPOINT)

    /** Reads the heating setpoint limits in 0.01 °C (min, max), preferring the user limits over the absolute ones. */
    suspend fun heatingSetpointLimits(): Pair<Int, Int> = readLimits(
        ThermostatClusterInfo.Attribute.MIN_HEAT_SETPOINT_LIMIT, ThermostatClusterInfo.Attribute.ABS_MIN_HEAT_SETPOINT_LIMIT,
        ThermostatClusterInfo.Attribute.MAX_HEAT_SETPOINT_LIMIT, ThermostatClusterInfo.Attribute.ABS_MAX_HEAT_SETPOINT_LIMIT,
    )

    /** Reads the cooling setpoint limits in 0.01 °C (min, max), preferring the user limits over the absolute ones. */
    suspend fun coolingSetpointLimits(): Pair<Int, Int> = readLimits(
        ThermostatClusterInfo.Attribute.MIN_COOL_SETPOINT_LIMIT, ThermostatClusterInfo.Attribute.ABS_MIN_COOL_SETPOINT_LIMIT,
        ThermostatClusterInfo.Attribute.MAX_COOL_SETPOINT_LIMIT, ThermostatClusterInfo.Attribute.ABS_MAX_COOL_SETPOINT_LIMIT,
    )

    private suspend fun readLimits(minAttr: Long, absMinAttr: Long, maxAttr: Long, absMaxAttr: Long): Pair<Int, Int> {
        suspend fun read(attr: Long, fallback: Long) =
            runCatching { readAttribute<Number>(attr).toInt() }.getOrNull()
                ?: readAttribute<Number>(fallback).toInt()
        return read(minAttr, absMinAttr) to read(maxAttr, absMaxAttr)
    }

    suspend fun setSystemMode(mode: Int) {
        writeAttribute(ThermostatClusterInfo.Attribute.SYSTEM_MODE, mode.toUByte())
    }

    /** Sets the heating setpoint, in 0.01 °C. */
    suspend fun setHeatingSetpoint(centiDegrees: Int) {
        writeAttribute(ThermostatClusterInfo.Attribute.OCCUPIED_HEATING_SETPOINT, centiDegrees.toShort())
    }

    /** Sets the cooling setpoint, in 0.01 °C. */
    suspend fun setCoolingSetpoint(centiDegrees: Int) {
        writeAttribute(ThermostatClusterInfo.Attribute.OCCUPIED_COOLING_SETPOINT, centiDegrees.toShort())
    }
}
