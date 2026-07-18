package no.nordicsemi.nrf.matter.controller

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import no.nordicsemi.nrf.matter.controller.MatterLightController
import no.nordicsemi.nrf.matter.matter.AttributeSubscriber
import no.nordicsemi.nrf.matter.matter.BoolAttributeParser
import no.nordicsemi.nrf.matter.matter.CommandExecutor
import no.nordicsemi.nrf.matter.matter.IntAttributeParser
import no.nordicsemi.nrf.matter.matter.StandardClusterIds
import no.nordicsemi.nrf.matter.matter.nsNumber
import no.nordicsemi.nrf.matter.model.DeviceId
import platform.Foundation.NSNumber
import platform.Matter.MTRUnsignedIntegerValueType

/**
 * Mirrors `iosApp/iosApp/kotlin/local/LocalMatterLightController.swift`, using raw
 * `MTRBaseDevice` calls instead of the typed `MTRBaseCluster*` wrapper classes (see
 * [StandardClusterIds]).
 */
@OptIn(ExperimentalForeignApi::class)
internal class IosMatterLightController : MatterLightController {

    override suspend fun setDeviceOnOff(deviceId: DeviceId, isOn: Boolean, endpoint: Int) {
        CommandExecutor(deviceId.nsNumber()).executeCommand(
            endpoint = NSNumber(int = endpoint),
            cluster = NSNumber(int = StandardClusterIds.OnOff.CLUSTER),
            command = NSNumber(int = if (isOn) StandardClusterIds.OnOff.CMD_ON else StandardClusterIds.OnOff.CMD_OFF),
        )
    }

    override suspend fun setBrightnessLevel(deviceId: DeviceId, brightnessLevel: Int, endpoint: Int) {
        CommandExecutor(deviceId.nsNumber()).executeCommand(
            endpoint = NSNumber(int = endpoint),
            cluster = NSNumber(int = StandardClusterIds.LevelControl.CLUSTER),
            command = NSNumber(int = StandardClusterIds.LevelControl.CMD_MOVE_TO_LEVEL_WITH_ON_OFF),
            type = MTRUnsignedIntegerValueType,
            value = NSNumber(int = brightnessLevel),
        )
    }

    override suspend fun observeLightState(deviceId: DeviceId, endpoint: Int): Flow<Boolean> {
        val flow = MutableSharedFlow<Boolean>(replay = 1, extraBufferCapacity = 8)
        AttributeSubscriber(deviceId.nsNumber()).subscribe(
            endpoint = NSNumber(int = endpoint),
            cluster = NSNumber(int = StandardClusterIds.OnOff.CLUSTER),
            attribute = NSNumber(int = StandardClusterIds.OnOff.ATTR_ON_OFF),
            parser = BoolAttributeParser,
            onUpdate = { flow.tryEmit(it) },
        )
        return flow
    }

    override suspend fun observeBrightnessState(deviceId: DeviceId, endpoint: Int): Flow<Float> {
        val flow = MutableSharedFlow<Float>(replay = 1, extraBufferCapacity = 8)
        AttributeSubscriber(deviceId.nsNumber()).subscribe(
            endpoint = NSNumber(int = endpoint),
            cluster = NSNumber(int = StandardClusterIds.LevelControl.CLUSTER),
            attribute = NSNumber(int = StandardClusterIds.LevelControl.ATTR_CURRENT_LEVEL),
            parser = IntAttributeParser,
            onUpdate = { rawLevel ->
                val percent = ((rawLevel - 1).toFloat() / 253f).coerceIn(0f, 1f)
                flow.tryEmit(percent)
            },
        )
        return flow
    }
}
