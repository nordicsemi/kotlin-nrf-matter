package no.nordicsemi.nrf.matter.controller

import kotlinx.cinterop.ExperimentalForeignApi
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.matter.AttributeReader
import no.nordicsemi.nrf.matter.matter.CommandExecutor
import no.nordicsemi.nrf.matter.matter.CommandField
import no.nordicsemi.nrf.matter.matter.LocalControllerProviderImpl
import no.nordicsemi.nrf.matter.matter.RawAttributeParser
import no.nordicsemi.nrf.matter.matter.StandardClusterIds
import no.nordicsemi.nrf.matter.matter.decodeStructArray
import no.nordicsemi.nrf.matter.matter.nsNumber
import no.nordicsemi.nrf.matter.matter.rawValue
import no.nordicsemi.nrf.matter.model.DeviceId
import platform.Foundation.NSNumber
import platform.Matter.MTRUnsignedIntegerValueType

/**
 * Mirrors `iosApp/iosApp/kotlin/local/LocalMatterDecommissioner.swift`, but reads the Fabrics
 * attribute and invokes RemoveFabric via raw `MTRBaseDevice` calls (see [StandardClusterIds]).
 *
 * ASSUMPTION REQUIRING HARDWARE VERIFICATION: `FabricIndex` is read here as the Matter global
 * field at context tag 254 (0xFE), per the general "fabric-scoped struct" convention in the
 * Matter spec — this project has no way to verify that tag number without a real fabric/device
 * to decommission. If decommissioning fails or removes an unexpected fabric, this is the first
 * thing to check (e.g. against Apple's `MTROperationalCredentialsClusterFabricDescriptorStruct`
 * source or a packet capture).
 */
@OptIn(ExperimentalForeignApi::class)
internal class IosMatterDecommissioner : MatterDecommissioner {

    private companion object {
        const val FABRIC_INDEX_TAG = 254
    }

    override suspend fun decommission(deviceId: DeviceId) {
        val nodeId = deviceId.nsNumber()
        val reader = AttributeReader(nodeId)

        val rawFabrics = reader.readAttribute(
            endpoint = NSNumber(int = 0),
            cluster = NSNumber(int = StandardClusterIds.OperationalCredentials.CLUSTER),
            attribute = NSNumber(int = StandardClusterIds.OperationalCredentials.ATTR_FABRICS),
            parser = RawAttributeParser,
        )
        val fabrics = decodeStructArray(rawFabrics)
        val fabricIndex = (fabrics.firstOrNull()?.get(FABRIC_INDEX_TAG).rawValue() as? NSNumber)
            ?: error("No fabrics found on device, or fabric index missing.")

        val commandExecutor = CommandExecutor(nodeId)
        commandExecutor.executeCommand(
            endpoint = NSNumber(int = 0),
            cluster = NSNumber(int = StandardClusterIds.OperationalCredentials.CLUSTER),
            command = NSNumber(int = StandardClusterIds.OperationalCredentials.CMD_REMOVE_FABRIC),
            fields = listOf(CommandField(contextTag = 0, type = MTRUnsignedIntegerValueType, value = fabricIndex)),
        )

        val controller = LocalControllerProviderImpl.getController("IosMatterDecommissioner")
        controller.forgetDeviceWithNodeID(nodeId)
    }
}
