package no.nordicsemi.nrf.matter.controller

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.domain.OperationResult
import no.nordicsemi.nrf.matter.model.DeviceId

interface MatterLightController {

    /**
     * Writes the current brightness level to the Level Control cluster.
     *
     * The command used is "Move to Level with On/Off", which sets the brightness level and turns on
     * or off the device based on the brightness level (if [brightnessLevel] > 0, the device will be
     * turned on; if [brightnessLevel] == 0, the device will be turned off). The transition is
     * instantaneous (no fade).
     *
     * @param deviceId the commissioned device to control.
     * @param brightnessLevel target level in the device's raw Level Control range (typically 1-254).
     * @param endpoint the Matter endpoint exposing the Level Control cluster.
     * @throws Exception if the underlying cluster command fails (e.g. device unreachable, command
     * rejected).
     */
    suspend fun setBrightnessLevel(
        deviceId: DeviceId,
        brightnessLevel: Int,
        endpoint: Int
    )

    /**
     * Turns the light on or off via the On/Off cluster.
     *
     * @param deviceId the commissioned device to control.
     * @param isOn `true` to send the On command, `false` to send the Off command.
     * @param endpoint the Matter endpoint exposing the On/Off cluster.
     * @throws Exception if the underlying cluster command fails (e.g. device unreachable, command
     * rejected).
     */
    suspend fun setDeviceOnOff(deviceId: DeviceId, isOn: Boolean, endpoint: Int)

    /**
     * Subscribes to the On/Off attribute of a light endpoint and emits its state as it changes.
     *
     * The subscription reports changes instantly and otherwise sends a heartbeat every 10 seconds;
     * establishing the underlying session is subject to a 10 second timeout. Once the subscription
     * is established, a report error is delivered as an [OperationResult.Error] rather than
     * terminating the flow, so later reports can still arrive. The returned [Flow] only closes
     * with an exception if the subscription cannot be established in the first place.
     *
     * @param deviceId the commissioned device to observe.
     * @param endpoint the Matter endpoint exposing the On/Off cluster.
     * @return a cold [Flow] emitting [OperationResult.Success] with `true` when the light is on,
     * `false` when it is off, or [OperationResult.Error] when a report could not be read.
     */
    suspend fun observeLightState(deviceId: DeviceId, endpoint: Int): Flow<OperationResult<Boolean>>

    /**
     * Subscribes to the CurrentLevel attribute of a light endpoint and emits its brightness as it
     * changes.
     *
     * The raw device level (1-254) is normalized to a 0f-1f percentage before being emitted. The
     * subscription reports changes instantly and otherwise sends a heartbeat every 10 seconds;
     * establishing the underlying session is subject to a 10 second timeout. Once the subscription
     * is established, a report error is delivered as an [OperationResult.Error] rather than
     * terminating the flow, so later reports can still arrive. The returned [Flow] only closes
     * with an exception if the subscription cannot be established in the first place.
     *
     * @param deviceId the commissioned device to observe.
     * @param endpoint the Matter endpoint exposing the Level Control cluster.
     * @return a cold [Flow] emitting [OperationResult.Success] with brightness as a fraction
     * between 0f (off) and 1f (max), or [OperationResult.Error] when a report could not be read.
     */
    suspend fun observeBrightnessState(deviceId: DeviceId, endpoint: Int): Flow<OperationResult<Float>>
}