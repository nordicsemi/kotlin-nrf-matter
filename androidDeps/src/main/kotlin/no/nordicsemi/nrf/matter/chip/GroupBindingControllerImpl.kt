package no.nordicsemi.nrf.matter.chip

import chip.devicecontroller.ChipClusters
import chip.devicecontroller.ChipStructs
import kotlinx.coroutines.suspendCancellableCoroutine
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
        groupId: Int?,
        groupName: String?,
    ): GroupBinding {
        val sourcePtr = chipClient.getConnectedDevicePointer(sourceNodeId.longValue)
        val targetPtr = chipClient.getConnectedDevicePointer(targetNodeId.longValue)

        try {
            val fabrics = readFabrics(sourcePtr)
            val fabricIndex = fabrics.firstOrNull()?.fabricIndex 
                ?: error("Could not determine fabric index for node $sourceNodeId")

            val finalGroupId = groupId ?: nextGroupId()
            val finalGroupName = groupName ?: "Group $finalGroupId"
            val keySetId = nextKeySetId()
            val keyMaterial = ByteArray(16).also { secureRandom.nextBytes(it) }

            NordicLogger.info("Starting group binding: GroupId=$finalGroupId, KeySetId=$keySetId, Fabric=$fabricIndex", tag = TAG)

            // Group key set on BOTH nodes.
            keySetWrite(sourcePtr, keySetId, keyMaterial)
            keySetWrite(targetPtr, keySetId, keyMaterial)

            // Map the group ID to the key set for this fabric on BOTH nodes.
            writeGroupKeyMap(sourcePtr, finalGroupId, keySetId, fabricIndex)
            writeGroupKeyMap(targetPtr, finalGroupId, keySetId, fabricIndex)

            // Add the target endpoint to the group.
            addGroup(targetPtr, targetEndpoint, finalGroupId, finalGroupName)

            // Update ACL on the target node to allow the group to operate.
            appendGroupAcl(targetPtr, finalGroupId, fabricIndex)

            // Write the group binding on the source node.
            writeGroupBinding(sourcePtr, sourceEndpoint, finalGroupId, clusterId, fabricIndex)

            NordicLogger.info("Group binding completed successfully", tag = TAG)

            return GroupBinding(
                id = "${sourceNodeId.longValue}_${targetNodeId.longValue}_group_$finalGroupId",
                sourceNodeId = sourceNodeId,
                sourceEndpoint = sourceEndpoint,
                targetNodeId = targetNodeId,
                targetEndpoint = targetEndpoint,
                clusterId = clusterId,
                groupId = finalGroupId,
                groupName = finalGroupName,
                keySetId = keySetId,
                fabricIndex = fabricIndex,
            )
        } catch (e: Exception) {
            NordicLogger.error("Group binding failed: ${e.message}", e, tag = TAG)
            throw e
        } finally {
            chipClient.chipDeviceController.releaseConnectedDevicePointer(sourcePtr)
            chipClient.chipDeviceController.releaseConnectedDevicePointer(targetPtr)
        }
    }

    private fun nextGroupId(): Int {
        val usedKeySets = chipClient.chipDeviceController.keySetIds.toSet()
        for (candidate in 1..0xFEFF) {
            if (candidate !in usedKeySets) {
                return candidate
            }
        }
        error("No group ids available")
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

    /** Step 1 — install the group key set (single epoch key, TrustFirst policy). */
    private suspend fun keySetWrite(devicePtr: Long, keySetId: Int, epochKey: ByteArray) =
        awaitDefault { cb ->
            val keySet = ChipStructs.GroupKeyManagementClusterGroupKeySetStruct(
                keySetId,
                GROUP_KEY_SECURITY_POLICY_TRUST_FIRST,
                epochKey,
                EPOCH_START_TIME_SAFE, // Using 1L as a safe non-zero starting time
                null, null,
                null, null,
            )
            ChipClusters.GroupKeyManagementCluster(devicePtr, ROOT_ENDPOINT)
                .keySetWrite(cb, keySet)
        }

    /** Step 2 — map the group id to the key set for this fabric. */
    private suspend fun writeGroupKeyMap(devicePtr: Long, groupId: Int, keySetId: Int, fabricIndex: Int) =
        awaitDefault { cb ->
            val map = ChipStructs.GroupKeyManagementClusterGroupKeyMapStruct(
                groupId, keySetId, fabricIndex,
            )
            ChipClusters.GroupKeyManagementCluster(devicePtr, ROOT_ENDPOINT)
                .writeGroupKeyMapAttribute(cb, arrayListOf(map))
        }

    /** Step 3 — add the endpoint to the group via the Groups cluster. */
    private suspend fun addGroup(devicePtr: Long, endpoint: Int, groupId: Int, groupName: String) =
        suspendCancellableCoroutine { cont ->
            ChipClusters.GroupsCluster(devicePtr, endpoint).addGroup(
                object : ChipClusters.GroupsCluster.AddGroupResponseCallback {
                    override fun onSuccess(status: Int?, groupID: Int?) {
                        if (cont.isActive) cont.resume(Unit)
                    }

                    override fun onError(error: Exception?) {
                        if (cont.isActive) cont.resumeWithException(error ?: RuntimeException("addGroup failed"))
                    }
                },
                groupId, groupName,
            )
        }

    /**
     * Step 4 — append a group ACL entry (read-modify-write so the admin entry is preserved).
     * authMode = Group(3), subjects = [groupId], privilege = Operate(3).
     */
    private suspend fun appendGroupAcl(devicePtr: Long, groupId: Int, fabricIndex: Int) {
        val aclCluster = ChipClusters.AccessControlCluster(devicePtr, ROOT_ENDPOINT)
        val existing = readAcl(aclCluster)
        
        val alreadyPresent = existing.any { entry ->
            entry.authMode == AUTH_MODE_GROUP && entry.subjects?.contains(groupId.toLong()) == true
        }
        if (alreadyPresent) return

        val groupEntry = ChipStructs.AccessControlClusterAccessControlEntryStruct(
            PRIVILEGE_OPERATE,
            AUTH_MODE_GROUP,
            arrayListOf(groupId.toLong()),
            null, // targets (all)
            fabricIndex,
        )
        val updated = ArrayList(existing).apply { add(groupEntry) }
        awaitDefault { cb -> aclCluster.writeAclAttribute(cb, updated) }
    }

    /** Step 5 — write the group binding on the switch's Binding cluster. */
    private suspend fun writeGroupBinding(
        sourcePtr: Long,
        sourceEndpoint: Int,
        groupId: Int,
        clusterId: Long,
        fabricIndex: Int
    ) {
        val bindingCluster = ChipClusters.BindingCluster(sourcePtr, sourceEndpoint)
        val existing = readBindings(bindingCluster)

        val newEntry = ChipStructs.BindingClusterTargetStruct(
            Optional.empty(),
            Optional.of(groupId),
            Optional.empty(),
            Optional.of(clusterId),
            fabricIndex,
        )

        val alreadyExists = existing.any {
            it?.group?.orElse(null) == groupId &&
            it.cluster?.orElse(null) == clusterId
        }

        if (alreadyExists) return

        val updated = ArrayList(existing).apply { add(newEntry) }
        awaitDefault { cb -> bindingCluster.writeBindingAttribute(cb, updated) }
    }

    private suspend fun readFabrics(devicePtr: Long): List<ChipStructs.OperationalCredentialsClusterFabricDescriptorStruct> =
        suspendCancellableCoroutine { cont ->
            ChipClusters.OperationalCredentialsCluster(devicePtr, ROOT_ENDPOINT)
                .readFabricsAttribute(object : ChipClusters.OperationalCredentialsCluster.FabricsAttributeCallback {
                    override fun onSuccess(values: List<ChipStructs.OperationalCredentialsClusterFabricDescriptorStruct>) {
                        if (cont.isActive) cont.resume(values)
                    }
                    override fun onError(error: Exception) {
                        if (cont.isActive) cont.resumeWithException(error)
                    }
                })
        }

    private suspend fun readAcl(aclCluster: ChipClusters.AccessControlCluster): List<ChipStructs.AccessControlClusterAccessControlEntryStruct> =
        suspendCancellableCoroutine { cont ->
            aclCluster.readAclAttribute(object : ChipClusters.AccessControlCluster.AclAttributeCallback {
                override fun onSuccess(value: MutableList<ChipStructs.AccessControlClusterAccessControlEntryStruct>?) {
                    if (cont.isActive) cont.resume(value ?: emptyList())
                }
                override fun onError(error: Exception?) {
                    if (cont.isActive) cont.resumeWithException(error ?: RuntimeException("readAcl failed"))
                }
            })
        }

    private suspend fun readBindings(bindingCluster: ChipClusters.BindingCluster): List<ChipStructs.BindingClusterTargetStruct?> =
        suspendCancellableCoroutine { cont ->
            bindingCluster.readBindingAttribute(object : ChipClusters.BindingCluster.BindingAttributeCallback {
                override fun onSuccess(valueList: List<ChipStructs.BindingClusterTargetStruct?>?) {
                    if (cont.isActive) cont.resume(valueList ?: emptyList())
                }
                override fun onError(ex: Exception) {
                    if (cont.isActive) cont.resumeWithException(ex)
                }
            })
        }

    private suspend fun awaitDefault(block: (ChipClusters.DefaultClusterCallback) -> Unit) =
        suspendCancellableCoroutine { cont ->
            val cb = object : ChipClusters.DefaultClusterCallback {
                override fun onSuccess() {
                    if (cont.isActive) cont.resume(Unit)
                }
                override fun onError(error: Exception?) {
                    if (cont.isActive) cont.resumeWithException(error ?: RuntimeException("cluster write failed"))
                }
            }
            block(cb)
        }

    companion object {
        private const val TAG = "GroupBinding"
        private const val ROOT_ENDPOINT = 0

        private const val GROUP_KEY_SECURITY_POLICY_TRUST_FIRST = 0
        private const val EPOCH_START_TIME_SAFE = 1L
        
        private const val AUTH_MODE_GROUP = 3
        private const val PRIVILEGE_OPERATE = 3
    }
}
