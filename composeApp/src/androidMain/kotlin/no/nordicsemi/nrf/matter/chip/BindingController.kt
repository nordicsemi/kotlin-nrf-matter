package no.nordicsemi.nrf.matter.chip

import chip.devicecontroller.ChipClusters
import chip.devicecontroller.ChipStructs
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId
import java.util.Optional
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class BindingControllerImpl(
    private val chipClient: ChipClient,
) : BindingController {

    private var lightSwitchFabricIndex: Int? = null

    /**
     * Creates a binding between a switch and a light cluster, ensuring the switch can control the light.
     *
     * This function performs two key steps:
     * 1. Grants the switch "operate" access to the target cluster on the light (ACL).
     * 2. Creates a binding entry from the switch to the light in the binding table.
     *
     * The operation is idempotent: if the ACL or binding already exists, it will not create duplicates.
     *
     * @param sourceNodeId Node ID of the switch device.
     * @param sourceEndpoint Endpoint on the switch where the binding is configured.
     * @param targetNodeId Node ID of the target light device.
     * @param targetEndpoint Endpoint on the light device.
     * @param clusterId ID of the cluster to bind (e.g., On/Off).
     */
    override suspend fun bind(
        sourceNodeId: DeviceId,
        sourceEndpoint: Int,
        targetNodeId: DeviceId,
        targetEndpoint: Int,
        clusterId: Long
    ) {
        bind(sourceNodeId.longValue, sourceEndpoint, targetNodeId.longValue, targetEndpoint, clusterId)
    }

    private suspend fun bind(
        switchNodeId: Long,
        switchEndpoint: Int,
        lightNodeId: Long,
        lightEndpoint: Int,
        clusterId: Long,
    ) {
        val switchPtr = chipClient.getConnectedDevicePointer(switchNodeId)
        val lightPtr = chipClient.getConnectedDevicePointer(lightNodeId)

        // ACL (LIGHT)
        grantOperateAccess(
            devicePtr = lightPtr,
            switchNodeId = switchNodeId,
            clusterId = clusterId
        )

        // Binding (SWITCH)
        createSwitchToLightBinding(
            devicePtr = switchPtr,
            switchEndpoint = switchEndpoint,
            lightNodeId = lightNodeId,
            lightEndpoint = lightEndpoint,
            clusterId = clusterId
        )
    }

    /**
     * Grants "operate" access for a specific cluster to a switch device by modifying the ACL.
     *
     * This function:
     * - Reads the current ACL from the device
     * - Checks if an entry already exists for the given [switchNodeId] and [clusterId]
     * - Reuses the existing fabric index (if available)
     * - Creates and appends a new ACL entry if no duplicate is found
     * - Writes the updated ACL back to the cluster
     *
     * If a matching ACL entry already exists, no changes are made.
     *
     * @param devicePtr Pointer to the target device.
     * @param switchNodeId Node ID of the switch to grant access to.
     * @param clusterId ID of the cluster for which "operate" access is granted.
     */
    private suspend fun grantOperateAccess(
        devicePtr: Long,
        switchNodeId: Long,
        clusterId: Long,
    ) {
        val cluster = ChipClusters.AccessControlCluster(devicePtr, ROOT_ENDPOINT)

        // Read existing ACL
        val existingAcl = cluster.awaitReadAcl()
        NordicLogger.debug("Light has already existing ACL of size: ${existingAcl.size}", tag = TAG)
            .takeIf { existingAcl.isNotEmpty() }

        // Check duplicates
        val alreadyExists = existingAcl.any { entry ->
            entry?.subjects?.contains(switchNodeId) == true &&
                    entry.targets?.any {
                        it.cluster == clusterId
                    } == true
        }

        if (alreadyExists) {
            NordicLogger.debug("ACL already exists, skipping", tag = TAG)
            return
        }
        // Get Fabric Index
        val fabricIndex = existingAcl
            .firstOrNull { it?.fabricIndex != null }
            ?.fabricIndex

        // Save non-null fabric index locally since we are using the same fabric index for both devices.
        fabricIndex?.let {
            NordicLogger.debug("Fabric Index: $it", tag = TAG)
            lightSwitchFabricIndex = it
        }

        // Create new ACL entry
        val newEntry = ChipStructs.AccessControlClusterAccessControlEntryStruct(
            /* privilege */ 3, // Operate
            /* authMode */ 2,  // CASE
            /* subjects */
            arrayListOf(switchNodeId),
            /* targets */
            arrayListOf(
                ChipStructs.AccessControlClusterAccessControlTargetStruct(
                    clusterId, // cluster (OnOff)
                    null,    // endpoint
                    null     // deviceType
                )
            ),
            /* fabricIndex */
            fabricIndex
        )

        // Append
        existingAcl.add(newEntry)

        // Write full list
        cluster.awaitWriteAcl(existingAcl)

        NordicLogger.debug("ACL updated successfully (cluster API)", tag = TAG)
    }

    /**
     * Suspends until the ACL (Access Control List) attribute is read from the cluster.
     *
     * @return An [ArrayList] of [ChipStructs.AccessControlClusterAccessControlEntryStruct] entries,
     * or an empty list if no values are returned.
     * @throws Exception if the read operation fails.
     */
    private suspend fun ChipClusters.AccessControlCluster.awaitReadAcl():
            ArrayList<ChipStructs.AccessControlClusterAccessControlEntryStruct?> {

        return suspendCancellableCoroutine { continuation ->

            readAclAttribute(object : ChipClusters.AccessControlCluster.AclAttributeCallback {

                override fun onSuccess(
                    valueList: List<ChipStructs.AccessControlClusterAccessControlEntryStruct?>?
                ) {
                    NordicLogger.debug("Read ACL (Access Control List) success", tag = TAG)
                    val result = ArrayList(
                        valueList ?: emptyList()
                    )

                    if (continuation.isActive) {
                        continuation.resume(result)
                    }
                }

                override fun onError(ex: Exception) {
                    NordicLogger.error(
                        "Read ACL (Access Control List) failed with exception: $ex",
                        tag = TAG
                    )
                    if (continuation.isActive) {
                        continuation.resumeWithException(ex)
                    }
                }
            })
            continuation.invokeOnCancellation {
                NordicLogger.debug("Read ACL (Access Control List) cancelled", tag = TAG)
            }
        }
    }

    /**
     * Suspends until the ACL (Access Control List) attribute is written to the cluster.
     *
     * @param acl An [ArrayList] of [ChipStructs.AccessControlClusterAccessControlEntryStruct] entries.
     * @throws Exception if the write operation fails.
     */
    private suspend fun ChipClusters.AccessControlCluster.awaitWriteAcl(
        acl: ArrayList<ChipStructs.AccessControlClusterAccessControlEntryStruct?>
    ) {
        return suspendCancellableCoroutine { continuation ->

            writeAclAttribute(
                object : ChipClusters.DefaultClusterCallback {
                    override fun onSuccess() {
                        NordicLogger.debug("Write ACL (Access Control List) success", tag = TAG)
                        if (continuation.isActive) {
                            continuation.resume(Unit)
                        }
                    }

                    override fun onError(ex: Exception) {
                        NordicLogger.error(
                            "Write ACL (Access Control List) failed with exception: $ex",
                            tag = TAG
                        )
                        if (continuation.isActive) {
                            continuation.resumeWithException(ex)
                        }
                    }
                },
                acl
            )

            continuation.invokeOnCancellation {
                NordicLogger.debug("Write ACL (Access Control List) cancelled", tag = TAG)
            }
        }
    }

    /**
     * Suspends until the Binding attribute is read from the cluster.
     *
     * @return An [ArrayList] of [ChipStructs.BindingClusterTargetStruct] entries,
     * or an empty list if no values are returned.
     * @throws Exception if the read operation fails.
     */
    private suspend fun ChipClusters.BindingCluster.awaitReadBinding():
            ArrayList<ChipStructs.BindingClusterTargetStruct?> {

        return suspendCancellableCoroutine { continuation ->

            readBindingAttribute(object : ChipClusters.BindingCluster.BindingAttributeCallback {

                override fun onSuccess(
                    valueList: List<ChipStructs.BindingClusterTargetStruct?>?
                ) {
                    NordicLogger.debug("Read Binding success", tag = TAG)
                    val result = ArrayList(
                        valueList ?: emptyList()
                    )

                    if (continuation.isActive) {
                        continuation.resume(result)
                    }
                }

                override fun onError(ex: Exception) {
                    NordicLogger.error("Read Binding failed with exception: $ex", tag = TAG)
                    if (continuation.isActive) {
                        continuation.resumeWithException(ex)
                    }
                }
            })

            continuation.invokeOnCancellation {
                NordicLogger.debug("Read Binding cancelled", tag = TAG)
            }
        }
    }

    /**
     * Suspends until the Binding attribute is written to the cluster.
     *
     * @param bindings An [ArrayList] of [ChipStructs.BindingClusterTargetStruct] entries.
     * @throws Exception if the write operation fails
     */
    private suspend fun ChipClusters.BindingCluster.awaitWriteBinding(
        bindings: ArrayList<ChipStructs.BindingClusterTargetStruct?>
    ) {
        return suspendCancellableCoroutine { continuation ->

            writeBindingAttribute(
                object : ChipClusters.DefaultClusterCallback {
                    override fun onSuccess() {
                        NordicLogger.debug("Write Binding success", tag = TAG)
                        if (continuation.isActive) {
                            continuation.resume(Unit)
                        }
                    }

                    override fun onError(ex: Exception) {
                        NordicLogger.error("Write Binding failed with exception: $ex", tag = TAG)
                        if (continuation.isActive) {
                            continuation.resumeWithException(ex)
                        }
                    }
                },
                bindings
            )

            continuation.invokeOnCancellation {
                NordicLogger.debug("Write Binding cancelled", tag = TAG)
            }
        }
    }

    /**
     * Binds a light switch to a light for a specific cluster by updating the binding table.
     *
     * This function:
     * - Reads the current binding table from the switch
     * - Checks if a binding already exists for the given light node, endpoint, and cluster
     * - Reuses the existing fabric index (if available)
     * - Creates and appends a new binding entry if no duplicate is found
     * - Writes the updated binding table back to the cluster
     *
     * If a matching binding already exists, no changes are made.
     *
     * @param devicePtr Pointer to the switch device.
     * @param switchEndpoint Endpoint on the switch where the binding is configured.
     * @param lightNodeId Node ID of the target light device.
     * @param lightEndpoint Endpoint on the light device (default is 1).
     * @param clusterId ID of the cluster to bind (e.g., On/Off).
     */
    private suspend fun createSwitchToLightBinding(
        devicePtr: Long,
        switchEndpoint: Int,
        lightNodeId: Long,
        lightEndpoint: Int,
        clusterId: Long,
    ) {
        val cluster = ChipClusters.BindingCluster(devicePtr, switchEndpoint)

        // Read existing bindings
        val existingBindings = cluster.awaitReadBinding()

        // Check duplicates
        val alreadyExists = existingBindings.any {
            it?.node?.orElse(null) == lightNodeId &&
                    it.cluster?.orElse(null) == clusterId &&
                    it.endpoint?.orElse(null) == lightEndpoint
        }

        if (alreadyExists) {
            NordicLogger.debug("Binding already exists, skipping", tag = "BindingLightSwitch")
            return
        }
        // Get fabric Index
        val fabricIndex = existingBindings
            .firstOrNull { it?.fabricIndex != null }
            ?.fabricIndex
        // Save non-null fabric index locally since we are using the same fabric index for both devices.
        fabricIndex?.let {
            lightSwitchFabricIndex = it
        }

        // Create new entry (Binding Table)
        val newEntry = ChipStructs.BindingClusterTargetStruct(
            Optional.of(lightNodeId),
            Optional.empty(), // Taking empty since for now we are using single light and switch binding.
            Optional.of(lightEndpoint),
            Optional.of(clusterId), // ON/OFF cluster
            lightSwitchFabricIndex,
        )

        existingBindings.add(newEntry)

        // Write full list
        cluster.awaitWriteBinding(existingBindings)

        NordicLogger.debug("Binding written successfully", tag = TAG)

    }

    companion object {
        private val TAG: String
            get() = "BindingLightSwitch"

        private const val ROOT_ENDPOINT: Int = 0
    }
}

