package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

object WindowCoveringClusterInfo {
    const val ID: Long = 0x0102

    object Attribute {
        const val CURRENT_POSITION_LIFT_PERCENT_100THS: Long = 0x000E
        const val CURRENT_POSITION_TILT_PERCENT_100THS: Long = 0x000F
        const val FEATURE_MAP: Long = 0xFFFC
    }

    object Command {
        const val UP_OR_OPEN: Long = 0x00
        const val DOWN_OR_CLOSE: Long = 0x01
        const val STOP_MOTION: Long = 0x02
        const val GO_TO_LIFT_PERCENTAGE: Long = 0x05
        const val GO_TO_TILT_PERCENTAGE: Long = 0x08
    }

    /** Bit positions of the Window Covering cluster FeatureMap. */
    object Feature {
        const val LIFT: Int = 1 shl 0
        const val TILT: Int = 1 shl 1
        const val POSITION_AWARE_LIFT: Int = 1 shl 2
        const val ABSOLUTE_POSITION: Int = 1 shl 3
        const val POSITION_AWARE_TILT: Int = 1 shl 4
    }
}

class WindowCoveringCluster(
    deviceId: DeviceId,
    endpoint: Int,
    controller: MatterClient,
) : Cluster(deviceId, endpoint, WindowCoveringClusterInfo.ID, controller) {

    /** Reads the raw FeatureMap bitmap, see [WindowCoveringClusterInfo.Feature]. */
    suspend fun featureMap(): Int = readAttribute<Number>(WindowCoveringClusterInfo.Attribute.FEATURE_MAP).toInt()

    /** Emits the lift position in 0.01 %, where 0 is fully open and 10000 fully closed. Null when unknown. */
    fun observeLiftPosition(): Flow<Number?> =
        observeAttribute(WindowCoveringClusterInfo.Attribute.CURRENT_POSITION_LIFT_PERCENT_100THS)

    /** Emits the tilt position in 0.01 %, where 0 is fully open and 10000 fully closed. Null when unknown. */
    fun observeTiltPosition(): Flow<Number?> =
        observeAttribute(WindowCoveringClusterInfo.Attribute.CURRENT_POSITION_TILT_PERCENT_100THS)

    suspend fun open() = executeCommand(WindowCoveringClusterInfo.Command.UP_OR_OPEN)

    suspend fun close() = executeCommand(WindowCoveringClusterInfo.Command.DOWN_OR_CLOSE)

    suspend fun stop() = executeCommand(WindowCoveringClusterInfo.Command.STOP_MOTION)

    /** Moves the lift to [percent100ths] (0 = open, 10000 = closed). */
    suspend fun goToLiftPercentage(percent100ths: Int) = executeCommand(
        commandId = WindowCoveringClusterInfo.Command.GO_TO_LIFT_PERCENTAGE,
        value = percent100ths.toUShort(),
    )

    /** Moves the tilt to [percent100ths] (0 = open, 10000 = closed). */
    suspend fun goToTiltPercentage(percent100ths: Int) = executeCommand(
        commandId = WindowCoveringClusterInfo.Command.GO_TO_TILT_PERCENTAGE,
        value = percent100ths.toUShort(),
    )
}
