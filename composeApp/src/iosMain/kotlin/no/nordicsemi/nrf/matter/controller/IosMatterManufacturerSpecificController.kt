package no.nordicsemi.nrf.matter.controller

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import no.nordicsemi.nrf.matter.controller.MatterManufacturerSpecificController
import no.nordicsemi.nrf.matter.domain.ManufacturerSpecificData
import no.nordicsemi.nrf.matter.matter.AttributeReader
import no.nordicsemi.nrf.matter.matter.AttributeSubscriber
import no.nordicsemi.nrf.matter.matter.BoolAttributeParser
import no.nordicsemi.nrf.matter.matter.CommandExecutor
import no.nordicsemi.nrf.matter.matter.StringAttributeParser
import no.nordicsemi.nrf.matter.matter.nsNumber
import no.nordicsemi.nrf.matter.matter.toNSNumber
import no.nordicsemi.nrf.matter.model.DeviceId
import platform.Foundation.NSNumber
import platform.Matter.MTRUnsignedIntegerValueType

/**
 * Namespace for identifiers of the manufacturer-specific cluster used in this example.
 * Mirrors the `ManufacturerSpecificCluster` enum in `LocalMatterCustomClusterController.swift`.
 */
internal object ManufacturerSpecificCluster {
    const val ID: UInt = 0xFFF1FC01u

    object Attribute {
        const val NAME: UInt = 0xfff10000u
        const val LED: UInt = 0xfff10001u
        const val BUTTON: UInt = 0xfff10002u
    }

    object Command {
        const val SET_LED: UInt = 0xFFF10000u
    }
}

/**
 * Mirrors `iosApp/iosApp/kotlin/local/LocalMatterCustomClusterController.swift`.
 */
@OptIn(ExperimentalForeignApi::class)
internal class IosMatterManufacturerSpecificController : MatterManufacturerSpecificController {

    suspend fun getData(deviceId: DeviceId, endpoint: Int): ManufacturerSpecificData {
        val reader = AttributeReader(deviceId.nsNumber())
        val endpointId = NSNumber(int = endpoint)

        val name = reader.readAttribute(
            endpoint = endpointId,
            cluster = ManufacturerSpecificCluster.ID.toNSNumber(),
            attribute = ManufacturerSpecificCluster.Attribute.NAME.toNSNumber(),
            parser = StringAttributeParser,
        )
        val led = reader.readAttribute(
            endpoint = endpointId,
            cluster = ManufacturerSpecificCluster.ID.toNSNumber(),
            attribute = ManufacturerSpecificCluster.Attribute.LED.toNSNumber(),
            parser = BoolAttributeParser,
        )
        val button = reader.readAttribute(
            endpoint = endpointId,
            cluster = ManufacturerSpecificCluster.ID.toNSNumber(),
            attribute = ManufacturerSpecificCluster.Attribute.BUTTON.toNSNumber(),
            parser = BoolAttributeParser,
        )

        return ManufacturerSpecificData(name = name, led = led, button = button)
    }

    override suspend fun setLed(deviceId: DeviceId, isOn: Boolean, endpoint: Int) {
        CommandExecutor(deviceId.nsNumber()).executeCommand(
            endpoint = NSNumber(int = endpoint),
            cluster = ManufacturerSpecificCluster.ID.toNSNumber(),
            command = ManufacturerSpecificCluster.Command.SET_LED.toNSNumber(),
            type = MTRUnsignedIntegerValueType,
            value = NSNumber(int = if (isOn) 1 else 0),
        )
    }

    override fun observeButtonChanges(deviceId: DeviceId, endpoint: Int): Flow<Boolean> {
        val flow = MutableSharedFlow<Boolean>(replay = 1, extraBufferCapacity = 8)
        AttributeSubscriber(deviceId.nsNumber()).subscribe(
            endpoint = NSNumber(int = endpoint),
            cluster = ManufacturerSpecificCluster.ID.toNSNumber(),
            attribute = ManufacturerSpecificCluster.Attribute.BUTTON.toNSNumber(),
            parser = BoolAttributeParser,
            onUpdate = { flow.tryEmit(it) },
        )
        return flow
    }
}
