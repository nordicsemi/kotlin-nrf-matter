package no.nordicsemi.nrf.matter.controller

import kotlinx.cinterop.ExperimentalForeignApi
import no.nordicsemi.nrf.matter.controller.MatterClusterExtensionController
import no.nordicsemi.nrf.matter.matter.AttributeReader
import no.nordicsemi.nrf.matter.matter.CommandExecutor
import no.nordicsemi.nrf.matter.matter.IntAttributeParser
import no.nordicsemi.nrf.matter.matter.nsNumber
import no.nordicsemi.nrf.matter.model.DeviceId
import platform.Foundation.NSNumber
import platform.Matter.MTRBooleanValueType

/**
 * Namespace for identifiers used by the custom Basic Information cluster extension.
 * Mirrors `BasicInformationClusterExtension` in `LocalMatterClusterExtController.swift`.
 */
internal object BasicInformationClusterExtension {
    const val ID = 0x28

    object Attribute {
        const val RANDOM_NUMBER = 0x17
    }

    object Command {
        const val GENERATE_RANDOM_NUMBER = 0x00
    }
}

/**
 * Mirrors `iosApp/iosApp/kotlin/local/LocalMatterClusterExtController.swift`.
 */
@OptIn(ExperimentalForeignApi::class)
internal class IosMatterClusterExtensionController : MatterClusterExtensionController {

    override suspend fun generateRandomNumber(deviceId: DeviceId, endpoint: Int): Long? {
        val nodeId = deviceId.nsNumber()
        val endpointId = NSNumber(int = endpoint)

        return try {
            CommandExecutor(nodeId).executeCommand(
                endpoint = endpointId,
                cluster = NSNumber(int = BasicInformationClusterExtension.ID),
                command = NSNumber(int = BasicInformationClusterExtension.Command.GENERATE_RANDOM_NUMBER),
                type = MTRBooleanValueType,
                value = NSNumber(bool = true),
            )

            AttributeReader(nodeId).readAttribute(
                endpoint = endpointId,
                cluster = NSNumber(int = BasicInformationClusterExtension.ID),
                attribute = NSNumber(int = BasicInformationClusterExtension.Attribute.RANDOM_NUMBER),
                parser = IntAttributeParser,
            ).toLong()
        } catch (e: Exception) {
            null
        }
    }
}
