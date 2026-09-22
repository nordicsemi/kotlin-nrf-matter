package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

object SmokeCoAlarmClusterInfo {
    const val ID: Long = 0x005C

    object Attribute {
        const val EXPRESSED_STATE: Long = 0x0000
        const val SMOKE_STATE: Long = 0x0001
        const val CO_STATE: Long = 0x0002
        const val BATTERY_ALERT: Long = 0x0003
        const val DEVICE_MUTED: Long = 0x0004
        const val TEST_IN_PROGRESS: Long = 0x0005
        const val HARDWARE_FAULT_ALERT: Long = 0x0006
        const val END_OF_SERVICE_ALERT: Long = 0x0007
    }

    object Command {
        const val SELF_TEST_REQUEST: Long = 0x00
    }
}

class SmokeCoAlarmCluster(
    override val deviceId: DeviceId,
    override val endpoint: Int,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = SmokeCoAlarmClusterInfo.ID

    /** Emits the raw ExpressedState value, see [no.nordicsemi.nrf.matter.model.AlarmState]. */
    fun observeExpressedState(): Flow<Number> = observeAttribute(SmokeCoAlarmClusterInfo.Attribute.EXPRESSED_STATE)

    /** Emits the raw SmokeState value. Not supported by CO-only alarms. */
    fun observeSmokeState(): Flow<Number> = observeAttribute(SmokeCoAlarmClusterInfo.Attribute.SMOKE_STATE)

    /** Emits the raw COState value. Not supported by smoke-only alarms. */
    fun observeCOState(): Flow<Number> = observeAttribute(SmokeCoAlarmClusterInfo.Attribute.CO_STATE)

    /** Emits the raw BatteryAlert value, see [no.nordicsemi.nrf.matter.model.AlarmState]. */
    fun observeBatteryAlert(): Flow<Number> = observeAttribute(SmokeCoAlarmClusterInfo.Attribute.BATTERY_ALERT)

    /** Emits the raw DeviceMuted value (MuteStateEnum: 0 = NotMuted, 1 = Muted). */
    fun observeDeviceMuted(): Flow<Number> = observeAttribute(SmokeCoAlarmClusterInfo.Attribute.DEVICE_MUTED)

    fun observeTestInProgress(): Flow<Boolean> = observeAttribute(SmokeCoAlarmClusterInfo.Attribute.TEST_IN_PROGRESS)

    fun observeHardwareFaultAlert(): Flow<Boolean> =
        observeAttribute(SmokeCoAlarmClusterInfo.Attribute.HARDWARE_FAULT_ALERT)

    /** Emits the raw EndOfServiceAlert value (ExpiryStateEnum: 0 = Normal, 1 = Expired). */
    fun observeEndOfServiceAlert(): Flow<Number> =
        observeAttribute(SmokeCoAlarmClusterInfo.Attribute.END_OF_SERVICE_ALERT)

    /** Triggers the alarm's self-test routine. */
    suspend fun selfTestRequest() {
        executeCommand(commandId = SmokeCoAlarmClusterInfo.Command.SELF_TEST_REQUEST)
    }
}