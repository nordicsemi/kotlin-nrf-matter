package no.nordicsemi.nrf.matter.chip

import chip.devicecontroller.ChipClusters
import chip.devicecontroller.ChipStructs
import chip.devicecontroller.ReportCallback
import chip.devicecontroller.model.AttributeState
import chip.devicecontroller.model.ChipAttributePath
import chip.devicecontroller.model.ChipEventPath
import chip.devicecontroller.model.NodeState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.domain.ManufacturerSpecificData
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceMatterInfo
import no.nordicsemi.nrf.matter.model.LockDeviceState
import java.util.Optional
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/*
 * Copyright (c) 2025, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

class ClustersHelper(private val chipClient: ChipClient) {

    /** Fetches MatterDeviceInfo for each endpoint supported by the device. */
    suspend fun fetchDeviceMatterInfo(deviceId: DeviceId): List<DeviceMatterInfo> {
        val matterDeviceInfoList = arrayListOf<DeviceMatterInfo>()
        val connectedDevicePtr =
            try {
                chipClient.getConnectedDevicePointer(deviceId.longValue)
            } catch (e: IllegalStateException) {
                NordicLogger.error("Can't get connectedDevicePointer.", e, tag = TAG)
                return emptyList()
            }
        fetchDeviceMatterInfo(deviceId.longValue, connectedDevicePtr, 0, matterDeviceInfoList)
        return matterDeviceInfoList
    }

    /** Fetches MatterDeviceInfo for a specific endpoint. */
    private suspend fun fetchDeviceMatterInfo(
        nodeId: Long,
        connectedDevicePtr: Long,
        endpointInt: Int,
        matterDeviceInfoList: ArrayList<DeviceMatterInfo>
    ) {

        val partsListAttribute =
            readDescriptorClusterPartsListAttribute(connectedDevicePtr, endpointInt)

        // DeviceListAttribute
        val deviceListAttribute =
            readDescriptorClusterDeviceListAttribute(connectedDevicePtr, endpointInt)
        val types = arrayListOf<Long>()
        deviceListAttribute.forEach { types.add(it.deviceType) }

        // ServerListAttribute
        val serverListAttribute =
            readDescriptorClusterServerListAttribute(connectedDevicePtr, endpointInt)
        val serverClusters = arrayListOf<Long>()
        serverListAttribute.forEach { serverClusters.add(it) }

        // ClientListAttribute
        val clientListAttribute =
            readDescriptorClusterClientListAttribute(connectedDevicePtr, endpointInt)
        val clientClusters = arrayListOf<Long>()
        clientListAttribute.forEach { clientClusters.add(it) }

        // manufacturer specific
        val manufacturerSpecificData = if (serverListAttribute.contains(0xFFF1FC01)) {
            getManufacturerSpecificData(endpointInt.toLong(), connectedDevicePtr)
        } else {
            NordicLogger.info("No manufacturer specific cluster", tag = TAG)
            null
        }

        val deviceMatterInfo = DeviceMatterInfo(
            endpointInt,
            types,
            serverClusters,
            clientClusters,
            manufacturerSpecificData
        )
        matterDeviceInfoList.add(deviceMatterInfo)

        // Recursive call for the parts supported by the endpoint.
        // For each part (endpoint)
        partsListAttribute?.forEach { part ->
            val childEndpoint = part as? Int ?: return@forEach

            val childServerList = try {
                readDescriptorClusterServerListAttribute(connectedDevicePtr, childEndpoint)
            } catch (_: Throwable) {
                NordicLogger.error(
                    "Endpoint $childEndpoint has no Descriptor cluster, skipping...",
                    tag = TAG
                )
                return@forEach
            }

            if (childServerList.isNotEmpty()) {
                fetchDeviceMatterInfo(
                    nodeId,
                    connectedDevicePtr,
                    childEndpoint,
                    matterDeviceInfoList
                )
            }
        }
    }

    // -----------------------------------------------------------------------------------------------
    // DescriptorCluster functions

    /**
     * PartsListAttribute. These are the endpoints supported.
     */
    suspend fun readDescriptorClusterPartsListAttribute(
        devicePtr: Long,
        endpoint: Int
    ): List<Any>? {
        return suspendCancellableCoroutine { continuation ->
            getDescriptorClusterForDevice(devicePtr, endpoint)
                .readPartsListAttribute(
                    object : ChipClusters.DescriptorCluster.PartsListAttributeCallback {
                        override fun onSuccess(values: MutableList<Int>?) {
                            continuation.resume(values)
                        }

                        override fun onError(ex: Exception) {
                            continuation.resumeWithException(ex)
                        }
                    })
        }
    }

    suspend fun getManufacturerSpecificData(
        endpoint: Long,
        connectedDevicePtr: Long
    ): ManufacturerSpecificData? {
        return try {
            val ep = endpoint.toInt()

            val namePath = ChipAttributePath.newInstance(ep, 0xFFF1FC01, 0xFFF10000)
            val ledPath = ChipAttributePath.newInstance(ep, 0xFFF1FC01, 0xFFF10001)
            val buttonPath = ChipAttributePath.newInstance(ep, 0xFFF1FC01, 0xFFF10002)
            NordicLogger.debug(
                "namePath: $namePath, ledPath: $ledPath, buttonPath: $buttonPath",
                tag = "ManufacturerSpecificData"
            )
            val results = chipClient.readAttributes(
                connectedDevicePtr,
                listOf<ChipAttributePath>(namePath, ledPath, buttonPath)
            )

            // Look up by matching IDs instead of by path object reference
            fun Map<ChipAttributePath, AttributeState>.findValue(
                endpointId: Int,
                clusterId: Long,
                attributeId: Long
            ): AttributeState? = entries.firstOrNull { (path, _) ->
                path.endpointId.id.toInt() == endpointId &&
                        path.clusterId.id == clusterId &&
                        path.attributeId.id == attributeId
            }?.value

            val name =
                results.findValue(ep, 0xFFF1FC01L, 0xFFF10000L)?.value as? String ?: return null
            val led = results.findValue(ep, 0xFFF1FC01L, 0xFFF10001L)?.value as? Boolean ?: false
            val button = results.findValue(ep, 0xFFF1FC01L, 0xFFF10002L)?.value as? Boolean ?: false

            NordicLogger.debug(
                "name=$name led=$led button=$button",
                tag = "ManufacturerSpecificData"
            )

            ManufacturerSpecificData(name, led, button)
        } catch (t: Throwable) {
            NordicLogger.error(
                "Manufacturer Specific Data acquisition failed: ${t.message}",
                tag = "ManufacturerSpecificData"
            )
            null
        }
    }

    suspend fun generateRandomNumber(deviceId: DeviceId): Long? {
        return try {
            val deviceId = deviceId
            val connectedDevicePtr = chipClient.getConnectedDevicePointer(deviceId.longValue)
            chipClient.generateRandomNumber(
                connectedDevicePtr,
                ChipAttributePath.newInstance(
                    0,
                    0x28,
                    0x00,
                )

            )
            val namePath = ChipAttributePath.newInstance(0, 0x0028, 0x00017)
            val nameAttr = chipClient.readAttribute(connectedDevicePtr, namePath)
            nameAttr?.value as? Long
        } catch (t: Throwable) {
            NordicLogger.error("Random number generation failed: ${t.message}", t, tag = TAG)
            t.printStackTrace()
            null
        }
    }

    // Subscribe to button changes.
    fun subscribeToButtonChanges(
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long,
    ): Flow<Boolean> = chipClient.subscribeToAttribute(
        deviceId,
        endpoint,
        clusterId,
        attributeId
    )

    /**
     * DeviceListAttribute
     *
     * ```
     * For example, on endpoint 0:
     *   device: [long type: 22, int revision: 1] -> maps to Root node (0x0016) (utility device type)
     * on endpoint 1:
     *   device: [long type: 256, int revision: 1] -> maps to On/Off Light (0x0100)
     * ```
     */
    suspend fun readDescriptorClusterDeviceListAttribute(
        devicePtr: Long,
        endpoint: Int
    ): List<ChipStructs.DescriptorClusterDeviceTypeStruct> {
        return suspendCancellableCoroutine { continuation ->
            getDescriptorClusterForDevice(devicePtr, endpoint)
                .readDeviceTypeListAttribute(
                    object : ChipClusters.DescriptorCluster.DeviceTypeListAttributeCallback {
                        override fun onSuccess(
                            values: List<ChipStructs.DescriptorClusterDeviceTypeStruct>
                        ) {
                            continuation.resume(values)
                        }

                        override fun onError(ex: Exception) {
                            continuation.resumeWithException(ex)
                        }
                    })
        }
    }

    /**
     * ServerListAttribute
     */
    suspend fun readDescriptorClusterServerListAttribute(
        devicePtr: Long,
        endpoint: Int
    ): List<Long> {
        return suspendCancellableCoroutine { continuation ->
            getDescriptorClusterForDevice(devicePtr, endpoint)
                .readServerListAttribute(
                    object : ChipClusters.DescriptorCluster.ServerListAttributeCallback {
                        override fun onSuccess(values: MutableList<Long>) {
                            continuation.resume(values)
                        }

                        override fun onError(ex: Exception) {
                            continuation.resumeWithException(ex)
                        }
                    })
        }
    }

    /** ClientListAttribute */
    suspend fun readDescriptorClusterClientListAttribute(
        devicePtr: Long,
        endpoint: Int
    ): List<Long> {
        return suspendCancellableCoroutine { continuation ->
            getDescriptorClusterForDevice(devicePtr, endpoint)
                .readClientListAttribute(
                    object : ChipClusters.DescriptorCluster.ClientListAttributeCallback {
                        override fun onSuccess(values: MutableList<Long>) {
                            continuation.resume(values)
                        }

                        override fun onError(ex: Exception) {
                            continuation.resumeWithException(ex)
                        }
                    })
        }
    }

    private fun getDescriptorClusterForDevice(
        devicePtr: Long,
        endpoint: Int
    ): ChipClusters.DescriptorCluster {
        return ChipClusters.DescriptorCluster(devicePtr, endpoint)
    }

    /**
     * Writes NodeLabel attribute. See spec section "11.1.6.3. Attributes" of the "Basic Information
     * Cluster".
     *
     * @param deviceId device identifier
     * @param nodeLabel device name/node label
     */
    suspend fun writeBasicClusterNodeLabelAttribute(deviceId: DeviceId, nodeLabel: String) {
        val connectedDevicePtr =
            try {
                chipClient.getConnectedDevicePointer(deviceId.longValue)
            } catch (e: IllegalStateException) {
                NordicLogger.error("Can't get connectedDevicePointer.", e, tag = TAG)
                return
            }

        return suspendCancellableCoroutine { continuation ->
            val callback =
                object : ChipClusters.DefaultClusterCallback {
                    override fun onSuccess() {
                        continuation.resume(Unit)
                    }

                    override fun onError(ex: Exception) {
                        continuation.resumeWithException(ex)
                    }
                }

            ChipClusters.BasicInformationCluster(connectedDevicePtr, 0)
                .writeNodeLabelAttribute(callback, nodeLabel)
        }
    }

    suspend fun setOnOffDeviceStateOnOffCluster(deviceId: Long, isOn: Boolean, endpoint: Int) {
        val connectedDevicePtr =
            try {
                chipClient.getConnectedDevicePointer(deviceId)
            } catch (e: IllegalStateException) {
                NordicLogger.error("Can't get connectedDevicePointer.", e, tag = TAG)
                return
            }
        if (isOn) {
            // ON
            return suspendCancellableCoroutine { continuation ->
                getOnOffClusterForDevice(connectedDevicePtr, endpoint)
                    .on(
                        object : ChipClusters.DefaultClusterCallback {
                            override fun onSuccess() {
                                continuation.resume(Unit)
                            }

                            override fun onError(ex: Exception) {
                                continuation.resumeWithException(ex)
                            }
                        })
            }
        } else {
            // OFF
            return suspendCancellableCoroutine { continuation ->
                getOnOffClusterForDevice(connectedDevicePtr, endpoint)
                    .off(
                        object : ChipClusters.DefaultClusterCallback {
                            override fun onSuccess() {
                                continuation.resume(Unit)
                            }

                            override fun onError(ex: Exception) {
                                continuation.resumeWithException(ex)
                            }
                        })
            }
        }
    }

    suspend fun lockUnlockDoor(
        deviceId: Long,
        isLocked: Boolean,
        endpoint: Int,
        pinCode: String? = null
    ) {
        val connectedDevicePtr =
            try {
                chipClient.getConnectedDevicePointer(deviceId)
            } catch (_: IllegalStateException) {
                return
            }

        // If pin code is not provided then pull empty value.
        val pinOptional = pinCode?.let {
            Optional.of(it.toByteArray(Charsets.UTF_8))
        } ?: Optional.empty()

        return suspendCancellableCoroutine { continuation ->
            val cluster = getLockUnlockClusterForDevice(connectedDevicePtr, endpoint)
            val callback = object : ChipClusters.DefaultClusterCallback {
                override fun onSuccess() {
                    if (continuation.isActive) continuation.resume(Unit)
                }

                override fun onError(ex: Exception?) {
                    if (continuation.isActive) {
                        continuation.resumeWithException(
                            ex ?: RuntimeException("Unknown Matter Error")
                        )
                    }
                }
            }

            if (isLocked) {
                cluster.lockDoor(callback, pinOptional, 10000)
            } else {
                cluster.unlockDoor(callback, pinOptional, 10000)
            }
        }
    }

    private fun getOnOffClusterForDevice(
        devicePtr: Long,
        endpoint: Int
    ): ChipClusters.OnOffCluster {
        return ChipClusters.OnOffCluster(devicePtr, endpoint)
    }

    private fun getLockUnlockClusterForDevice(
        devicePtr: Long,
        endpoint: Int
    ): ChipClusters.DoorLockCluster {
        return ChipClusters.DoorLockCluster(devicePtr, endpoint)
    }

    /**
     * Writes the current brightness level to the Level Control cluster.
     * The command used is "Move to Level with On/Off", which sets the brightness level and turns on
     * or off the device based on the brightness level (if brightnessLevel > 0, the device will be turned on;
     * if brightnessLevel == 0, the device will be turned off).
     */
    suspend fun setBrightnessLevel(
        deviceId: DeviceId,
        brightnessLevel: Int,
        endpoint: Int
    ) {
        val connectedDevicePtr =
            try {
                chipClient.getConnectedDevicePointer(deviceId.longValue)
            } catch (e: IllegalStateException) {
                NordicLogger.error("Can't get connectedDevicePointer.", e, tag = TAG)
                return
            }
        return suspendCancellableCoroutine { continuation ->
            getLevelControlClusterForDevice(connectedDevicePtr, endpoint)
                .moveToLevelWithOnOff(
                    object : ChipClusters.DefaultClusterCallback {
                        override fun onSuccess() {
                            continuation.resume(Unit)
                        }

                        override fun onError(ex: Exception) {
                            continuation.resumeWithException(ex)
                        }
                    },
                    brightnessLevel,
                    0, // transitionTime (0 = instantaneous)
                    0, // optionsMask
                    0  // optionsOverride
                )
        }
    }

    private fun getLevelControlClusterForDevice(
        devicePtr: Long,
        endpoint: Int
    ): ChipClusters.LevelControlCluster {
        return ChipClusters.LevelControlCluster(devicePtr, endpoint)
    }

    fun observeLightState(deviceId: DeviceId, endpoint: Int): Flow<Boolean> =
        callbackFlow {

            val reportCallback = object : ReportCallback {

                override fun onError(
                    attributePath: ChipAttributePath?,
                    eventPath: ChipEventPath?,
                    e: java.lang.Exception
                ) {
                    NordicLogger.error(
                        "Error receiving report from DK for path: $attributePath",
                        e, tag = TAG
                    )
                }

                override fun onReport(nodeState: NodeState) {
                    val endpointState = nodeState.getEndpointState(endpoint) ?: return

                    // On/Off State (Cluster 6, Attribute 0)
                    val onOffCluster = endpointState.getClusterState(6)
                    val onOffAttr = onOffCluster?.getAttributeState(0)
                    val isOn = onOffAttr?.value as? Boolean

                    if (isOn != null) {
                        NordicLogger.info(
                            "Received On/Off report: isLedOn=$isOn", tag = TAG
                        )
                        trySend(isOn)
                    }
                }
            }

            try {
                val devicePtr = chipClient.getConnectedDevicePointer(deviceId.longValue)
                val attributePaths = listOf(
                    ChipAttributePath.newInstance(endpoint, 6, 0), // OnOff
                )

                chipClient.subscribeAttribute(
                    reportCallback = reportCallback,
                    devicePtr = devicePtr,
                    attributePaths = attributePaths,
                    minIntervalS = 0,    // Report changes instantly
                    maxIntervalS = 10,   // Heartbeat check every 10 seconds
                    timeoutMs = 10000    // 10 second network timeout for establishing the session
                )
            } catch (e: Exception) {
                NordicLogger.error(
                    "Failed to setup wrapper subscription", e, tag = TAG
                )

                close(e)
            }

            awaitClose {
                // Handle stream cleanup
            }
        }

    fun observeBrightnessState(deviceId: DeviceId, endpoint: Int): Flow<Float> =
        callbackFlow {
            val reportCallback = object : ReportCallback {

                override fun onError(
                    attributePath: ChipAttributePath?,
                    eventPath: ChipEventPath?,
                    e: java.lang.Exception
                ) {
                    NordicLogger.error(
                        "Error receiving report from DK for path: $attributePath", e, tag = TAG
                    )
                }

                override fun onReport(nodeState: NodeState) {
                    val endpointState = nodeState.getEndpointState(endpoint) ?: return

                    // Brightness Level (Cluster 8, Attribute 0)
                    val levelCluster = endpointState.getClusterState(8)
                    val levelAttr = levelCluster?.getAttributeState(0)
                    val rawValue = levelAttr?.value as? Number

                    if (rawValue != null) {
                        val rawLevel = rawValue.toLong()
                        val percent = ((rawLevel.toFloat() - 1f) / 253f).coerceIn(0f, 1f)
                        NordicLogger.info(
                            "Received Brightness report: brightnessPercentage=$percent", tag = TAG
                        )
                        trySend(percent)
                    }
                }
            }

            try {
                val devicePtr = chipClient.getConnectedDevicePointer(deviceId.longValue)
                val attributePaths = listOf(
                    ChipAttributePath.newInstance(endpoint, 8, 0)  // CurrentLevel
                )

                chipClient.subscribeAttribute(
                    reportCallback = reportCallback,
                    devicePtr = devicePtr,
                    attributePaths = attributePaths,
                    minIntervalS = 0,    // Report changes instantly
                    maxIntervalS = 10,   // Heartbeat check every 10 seconds
                    timeoutMs = 10000    // 10 second network timeout for establishing the session
                )
            } catch (e: Exception) {
                NordicLogger.error(
                    "Failed to setup wrapper subscription", e, tag = TAG
                )

                close(e)
            }

            awaitClose {
                // Handle stream cleanup
            }
        }

    fun observeLockState(deviceId: DeviceId, endpoint: Int, doorLockClusterId: Long) =
        callbackFlow {
            val reportCallback = object : ReportCallback {
                override fun onError(
                    attributePath: ChipAttributePath?,
                    eventPath: ChipEventPath?,
                    e: Exception
                ) {
                    NordicLogger.error("Subscription error on lock: $attributePath", e, tag = TAG)
                }

                override fun onReport(nodeState: NodeState) {
                    val endpointState = nodeState.getEndpointState(endpoint) ?: return

                    // Door Lock Cluster (257L), LockState Attribute (0)
                    val lockCluster = endpointState.getClusterState(doorLockClusterId)
                    val lockAttr = lockCluster?.getAttributeState(0)

                    val rawValue = lockAttr?.value as? Number

                    if (rawValue != null) {
                        val lockStateEnum = rawValue.toInt()

                        // 1 = Locked, 2 = Unlocked
                        val isLocked = lockStateEnum == 1

                        NordicLogger.info(
                            "Received LockState report: isLocked=$isLocked", tag = TAG
                        )

                        trySend(LockDeviceState.create(lockStateEnum))
                    }
                }
            }

            try {
                val devicePtr = chipClient.getConnectedDevicePointer(deviceId.longValue)
                val targetPaths = listOf(
                    ChipAttributePath.newInstance(endpoint, doorLockClusterId, 0) // Track LockState
                )

                chipClient.subscribeAttribute(
                    reportCallback = reportCallback,
                    devicePtr = devicePtr,
                    attributePaths = targetPaths,
                    minIntervalS = 0,
                    maxIntervalS = 10,
                    timeoutMs = 10000
                )
            } catch (e: Exception) {
                close(e)
            }

            awaitClose { /* Cleanup */ }
        }

    companion object {
        private val TAG: String
            get() = ClustersHelper::class.java.simpleName
    }

}