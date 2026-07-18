package no.nordicsemi.nrf.matter.controller

import kotlinx.cinterop.ExperimentalForeignApi
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.matter.AttributeReader
import no.nordicsemi.nrf.matter.matter.AttributeWriter
import no.nordicsemi.nrf.matter.matter.RawAttributeParser
import no.nordicsemi.nrf.matter.matter.StandardClusterIds
import no.nordicsemi.nrf.matter.matter.TlvField
import no.nordicsemi.nrf.matter.matter.arrayElements
import no.nordicsemi.nrf.matter.matter.dataValue
import no.nordicsemi.nrf.matter.matter.decodeStructArray
import no.nordicsemi.nrf.matter.matter.decodeValueArray
import no.nordicsemi.nrf.matter.matter.encodeStructArrayValue
import no.nordicsemi.nrf.matter.matter.nsNumber
import no.nordicsemi.nrf.matter.matter.rawValue
import no.nordicsemi.nrf.matter.matter.structureValue
import no.nordicsemi.nrf.matter.matter.toTlvField
import no.nordicsemi.nrf.matter.model.DeviceId
import platform.Foundation.NSNumber
import platform.Matter.MTRArrayValueType
import platform.Matter.MTRUnsignedIntegerValueType

/**
 * Mirrors `iosApp/iosApp/kotlin/local/LocalMatterBinder.swift`, using raw `MTRBaseDevice`
 * read/write calls instead of the typed `MTRBaseCluster*` wrapper classes (see
 * [StandardClusterIds]).
 *
 * ASSUMPTION REQUIRING HARDWARE VERIFICATION: the Access Control / Binding struct field context
 * tags below (privilege=1, authMode=2, subjects=3, targets=4 for `AccessControlEntryStruct`;
 * cluster=0, endpoint=1, deviceType=2 for `AccessControlTargetStruct`; node=1, endpoint=3,
 * cluster=4 for the Binding cluster's `TargetStruct`) come from the Matter spec's general field
 * ordering convention, not something this project can verify without a real device — see the
 * note on [no.nordicsemi.nrf.matter.controller.IosMatterDecommissioner] for the equivalent
 * concern with `FabricIndex`.
 */
@OptIn(ExperimentalForeignApi::class)
internal class IosBindingController : BindingController {

    private companion object {
        const val ACL_PRIVILEGE_OPERATE = 3
        const val ACL_AUTH_MODE_CASE = 2

        const val ACL_FIELD_PRIVILEGE = 1
        const val ACL_FIELD_AUTH_MODE = 2
        const val ACL_FIELD_SUBJECTS = 3
        const val ACL_FIELD_TARGETS = 4

        const val ACL_TARGET_FIELD_CLUSTER = 0
        const val ACL_TARGET_FIELD_ENDPOINT = 1
        const val ACL_TARGET_FIELD_DEVICE_TYPE = 2

        const val BINDING_FIELD_NODE = 1
        const val BINDING_FIELD_ENDPOINT = 3
        const val BINDING_FIELD_CLUSTER = 4
    }

    override suspend fun bind(sourceNodeId: DeviceId, sourceEndpoint: Int, targetNodeId: DeviceId, targetEndpoint: Int, clusterId: Long) {
        val source = sourceNodeId.nsNumber()
        val target = targetNodeId.nsNumber()
        val cluster = NSNumber(long = clusterId)

        grantAccessToSource(targetDeviceId = target, sourceNodeId = source, cluster = cluster)
        bindSourceToTarget(sourceDeviceId = source, sourceEndpoint = sourceEndpoint, targetNodeId = target, targetEndpoint = targetEndpoint, cluster = cluster)
    }

    private suspend fun grantAccessToSource(targetDeviceId: NSNumber, sourceNodeId: NSNumber, cluster: NSNumber) {
        val reader = AttributeReader(targetDeviceId)
        val writer = AttributeWriter(targetDeviceId)
        val endpoint = NSNumber(int = 0)
        val aclAttribute = NSNumber(int = StandardClusterIds.AccessControl.ATTR_ACL)
        val aclCluster = NSNumber(int = StandardClusterIds.AccessControl.CLUSTER)

        val rawAcl = reader.readAttribute(endpoint, aclCluster, aclAttribute, RawAttributeParser)
        val existingEntries = decodeStructArray(rawAcl)

        val entryExists = existingEntries.any { entry ->
            val privilege = (entry[ACL_FIELD_PRIVILEGE].rawValue() as? NSNumber)?.intValue
            val subjects = decodeValueArray(entry[ACL_FIELD_SUBJECTS].rawValue())
            privilege == ACL_PRIVILEGE_OPERATE && subjects.any { (it as? NSNumber)?.longLongValue == sourceNodeId.longLongValue }
        }
        if (entryExists) return

        val newEntryFields = listOf(
            TlvField(ACL_FIELD_PRIVILEGE, MTRUnsignedIntegerValueType, NSNumber(int = ACL_PRIVILEGE_OPERATE)),
            TlvField(ACL_FIELD_AUTH_MODE, MTRUnsignedIntegerValueType, NSNumber(int = ACL_AUTH_MODE_CASE)),
            TlvField(
                ACL_FIELD_SUBJECTS,
                MTRArrayValueType,
                arrayElements(listOf(dataValue(MTRUnsignedIntegerValueType, sourceNodeId))),
            ),
            TlvField(
                ACL_FIELD_TARGETS,
                MTRArrayValueType,
                arrayElements(listOf(accessControlTargetStruct(cluster))),
            ),
        )

        val allEntries = existingEntries.map { entry -> entry.map { (tag, field) -> field.toTlvField(tag) } } + listOf(newEntryFields)
        writer.writeAttribute(endpoint, aclCluster, aclAttribute, encodeStructArrayValue(allEntries))
    }

    private fun accessControlTargetStruct(cluster: NSNumber): Map<String, Any> = structureValue(
        listOf(
            TlvField(ACL_TARGET_FIELD_CLUSTER, MTRUnsignedIntegerValueType, cluster),
            TlvField(ACL_TARGET_FIELD_ENDPOINT, "", null),
            TlvField(ACL_TARGET_FIELD_DEVICE_TYPE, "", null),
        ),
    )

    private suspend fun bindSourceToTarget(sourceDeviceId: NSNumber, sourceEndpoint: Int, targetNodeId: NSNumber, targetEndpoint: Int, cluster: NSNumber) {
        val reader = AttributeReader(sourceDeviceId)
        val writer = AttributeWriter(sourceDeviceId)
        val endpoint = NSNumber(int = sourceEndpoint)
        val bindingAttribute = NSNumber(int = StandardClusterIds.Binding.ATTR_BINDING)
        val bindingCluster = NSNumber(int = StandardClusterIds.Binding.CLUSTER)

        val rawBindings = reader.readAttribute(endpoint, bindingCluster, bindingAttribute, RawAttributeParser)
        val existingBindings = decodeStructArray(rawBindings)

        val newBindingFields = listOf(
            TlvField(BINDING_FIELD_NODE, MTRUnsignedIntegerValueType, targetNodeId),
            TlvField(BINDING_FIELD_ENDPOINT, MTRUnsignedIntegerValueType, NSNumber(int = targetEndpoint)),
            TlvField(BINDING_FIELD_CLUSTER, MTRUnsignedIntegerValueType, cluster),
        )

        val allBindings = existingBindings.map { entry -> entry.map { (tag, field) -> field.toTlvField(tag) } } + listOf(newBindingFields)
        writer.writeAttribute(endpoint, bindingCluster, bindingAttribute, encodeStructArrayValue(allBindings))
    }
}
