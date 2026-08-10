package no.nordicsemi.nrf.matter.chip

import chip.devicecontroller.ChipClusters
import chip.devicecontroller.ChipStructs
import chip.devicecontroller.ClusterIDMapping
import chip.devicecontroller.GroupKeySecurityPolicy
import kotlinx.coroutines.suspendCancellableCoroutine
import matter.tlv.AnonymousTag
import matter.tlv.ContextSpecificTag
import matter.tlv.TlvWriter
import no.nordicsemi.nrf.matter.controller.GroupBindingController
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.GroupBinding
import java.security.SecureRandom
import java.util.Optional
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GroupBindingControllerImpl(
    private val chipClient: ChipClient,
) : GroupBindingController {

    private val secureRandom = SecureRandom()

    override suspend fun bind(
        sourceNodeId: DeviceId,
        sourceEndpoint: Int,
        targetNodeId: DeviceId,
        targetEndpoint: Int,
        clusterId: Long,
    ): GroupBinding {
        val sourcePtr = chipClient.getConnectedDevicePointer(sourceNodeId.longValue)
        val targetPtr = chipClient.getConnectedDevicePointer(targetNodeId.longValue)

        try {
            val existingBindings = readBindings(sourcePtr, sourceEndpoint)
            val groupId = nextGroupId()
            val keySetId = nextKeySetId()
            val groupName = "Group $groupId"
            val keyMaterial = ByteArray(16).also { secureRandom.nextBytes(it) }
            val epochStartTime = System.currentTimeMillis() / 1000L

            writeGroupKeySet(
                targetNodeId = sourceNodeId.longValue,
                groupKeySetId = keySetId,
                keyMaterial = keyMaterial,
                epochStartTime = epochStartTime,
            )
            writeGroupKeySet(
                targetNodeId = targetNodeId.longValue,
                groupKeySetId = keySetId,
                keyMaterial = keyMaterial,
                epochStartTime = epochStartTime,
            )
            addTargetToGroup(
                targetNodeId = targetNodeId.longValue,
                targetEndpoint = targetEndpoint,
                groupId = groupId,
                groupName = groupName,
            )

            val fabricIndex = existingBindings
                .firstOrNull { it?.fabricIndex != null }
                ?.fabricIndex

            writeGroupBinding(
                sourcePtr = sourcePtr,
                sourceEndpoint = sourceEndpoint,
                targetNodeId = targetNodeId.longValue,
                targetEndpoint = targetEndpoint,
                clusterId = clusterId,
                groupId = groupId,
                fabricIndex = fabricIndex,
            )

            return GroupBinding(
                id = "${sourceNodeId.longValue}_${targetNodeId.longValue}_group_$groupId",
                sourceNodeId = sourceNodeId,
                sourceEndpoint = sourceEndpoint,
                targetNodeId = targetNodeId,
                targetEndpoint = targetEndpoint,
                clusterId = clusterId,
                groupId = groupId,
                groupName = groupName,
                keySetId = keySetId,
                fabricIndex = fabricIndex,
            )
        } finally {
            chipClient.chipDeviceController.releaseConnectedDevicePointer(sourcePtr)
            chipClient.chipDeviceController.releaseConnectedDevicePointer(targetPtr)
        }
    }

    private fun nextGroupId(): Int {
        val available = chipClient.chipDeviceController.availableGroupIds
        return available.firstOrNull { it in 1..0xFFFF } ?: error("No group ids available")
    }

    private fun nextKeySetId(): Int {
        val usedIds = chipClient.chipDeviceController.keySetIds.toSet()
        for (candidate in 1..0xFFFF) {
            if (candidate !in usedIds) {
                return candidate
            }
        }
        error("No key set ids available")
    }

    private suspend fun writeGroupKeySet(
        targetNodeId: Long,
        groupKeySetId: Int,
        keyMaterial: ByteArray,
        epochStartTime: Long,
    ) {
        val payload = TlvWriter().apply {
            startStructure(AnonymousTag)
            startStructure(
                ContextSpecificTag(
                    ClusterIDMapping.GroupKeyManagement.KeySetWriteCommandField.GroupKeySet.getID()
                )
            )
            put(ContextSpecificTag(0), groupKeySetId)
            put(ContextSpecificTag(1), GroupKeySecurityPolicy.TrustFirst.getID())
            put(ContextSpecificTag(2), keyMaterial)
            put(ContextSpecificTag(3), epochStartTime)
            put(ContextSpecificTag(4), ByteArray(keyMaterial.size))
            put(ContextSpecificTag(5), 0L)
            put(ContextSpecificTag(6), ByteArray(keyMaterial.size))
            put(ContextSpecificTag(7), 0L)
            endStructure()
            endStructure()
        }.getEncoded()

        invokeClusterCommand(
            targetNodeId = targetNodeId,
            endpoint = ROOT_ENDPOINT,
            clusterId = GROUP_KEY_MANAGEMENT_CLUSTER_ID,
            commandId = ClusterIDMapping.GroupKeyManagement.Command.KeySetWrite.id,
            payload = payload,
        )
    }

    private suspend fun addTargetToGroup(
        targetNodeId: Long,
        targetEndpoint: Int,
        groupId: Int,
        groupName: String,
    ) {
        val payload = TlvWriter().apply {
            startStructure(AnonymousTag)
            put(
                ContextSpecificTag(ClusterIDMapping.Groups.AddGroupCommandField.GroupID.id),
                groupId
            )
            put(
                ContextSpecificTag(ClusterIDMapping.Groups.AddGroupCommandField.GroupName.id),
                groupName
            )
            endStructure()
        }.getEncoded()

        invokeClusterCommand(
            targetNodeId = targetNodeId,
            endpoint = targetEndpoint,
            clusterId = GROUPS_CLUSTER_ID,
            commandId = ClusterIDMapping.Groups.Command.AddGroup.id,
            payload = payload,
        )
    }

    private suspend fun writeGroupBinding(
        sourcePtr: Long,
        sourceEndpoint: Int,
        targetNodeId: Long,
        targetEndpoint: Int,
        clusterId: Long,
        groupId: Int,
        fabricIndex: Int?,
    ) {
        val cluster = ChipClusters.BindingCluster(sourcePtr, sourceEndpoint)
        val existingBindings = readBindings(sourcePtr, sourceEndpoint)

        val newEntry = ChipStructs.BindingClusterTargetStruct(
            Optional.empty(),
            Optional.of(groupId),
            Optional.empty(),
            Optional.of(clusterId),
            fabricIndex,
        )

        val alreadyExists = existingBindings.any {
            it?.group?.orElse(null) == groupId &&
                    it.cluster?.orElse(null) == clusterId &&
                    it.endpoint?.orElse(null) == null
        }

        if (alreadyExists) {
            NordicLogger.debug("Group binding already exists, skipping", tag = TAG)
            return
        }

        existingBindings.add(newEntry)
        cluster.awaitWriteBinding(existingBindings)
    }

    private suspend fun invokeClusterCommand(
        targetNodeId: Long,
        endpoint: Int,
        clusterId: Long,
        commandId: Long,
        payload: ByteArray,
    ) {
        val connectedDevicePtr = chipClient.getConnectedDevicePointer(targetNodeId)
        try {
            val invokeElement = chip.devicecontroller.model.InvokeElement.newInstance(
                endpoint,
                clusterId,
                commandId,
                payload,
                null
            )
            chipClient.invoke(connectedDevicePtr, invokeElement)
        } finally {
            chipClient.chipDeviceController.releaseConnectedDevicePointer(connectedDevicePtr)
        }
    }

    private suspend fun readBindings(
        devicePtr: Long,
        endpoint: Int,
    ): ArrayList<ChipStructs.BindingClusterTargetStruct?> {
        val cluster = ChipClusters.BindingCluster(devicePtr, endpoint)
        return suspendCancellableCoroutine { continuation ->
            cluster.readBindingAttribute(object : ChipClusters.BindingCluster.BindingAttributeCallback {
                override fun onSuccess(valueList: List<ChipStructs.BindingClusterTargetStruct?>?) {
                    continuation.resume(ArrayList(valueList ?: emptyList()))
                }

                override fun onError(ex: Exception) {
                    continuation.resumeWithException(ex)
                }
            })
        }
    }

    private suspend fun ChipClusters.BindingCluster.awaitWriteBinding(
        bindings: ArrayList<ChipStructs.BindingClusterTargetStruct?>
    ) {
        return suspendCancellableCoroutine { continuation ->
            writeBindingAttribute(
                object : ChipClusters.DefaultClusterCallback {
                    override fun onSuccess() {
                        if (continuation.isActive) {
                            continuation.resume(Unit)
                        }
                    }

                    override fun onError(ex: Exception) {
                        if (continuation.isActive) {
                            continuation.resumeWithException(ex)
                        }
                    }
                },
                bindings
            )
        }
    }

    companion object {
        private const val GROUPS_CLUSTER_ID: Long = 0x0004L
        private const val GROUP_KEY_MANAGEMENT_CLUSTER_ID: Long = 0x003FL
        private const val ROOT_ENDPOINT: Int = 0
        private const val TAG = "GroupBinding"
    }
}
