package no.nordicsemi.nrf.matter.chip

import chip.devicecontroller.ChipClusters
import chip.devicecontroller.ChipStructs
import chip.devicecontroller.model.AttributeState
import chip.devicecontroller.model.ChipAttributePath
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.model.ManufacturerSpecificData
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceMatterInfo
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

    /**
     * Retrieves Matter endpoint metadata for a commissioned device.
     *
     * Returns descriptor-cluster data and manufacturer-specific data, when available,
     * as a list of [DeviceMatterInfo] entries.
     *
     * @param deviceId The identifier of the commissioned device to query.
     * @return A list of [DeviceMatterInfo] for the device, or an empty list if the device
     *   cannot be connected.
     */
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

    /**
     * Collects endpoint metadata for a single endpoint and its descendant endpoints.
     *
     * @param nodeId The Matter node ID of the device being queried.
     * @param connectedDevicePtr A native pointer to the connected device.
     * @param endpointInt The endpoint to query on this invocation.
     * @param matterDeviceInfoList The mutable list that receives collected results.
     */
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

    /**
     * Reads the `PartsList` attribute of the Descriptor cluster on the given endpoint.
     *
     * @param devicePtr A native pointer to the connected device.
     * @param endpoint The endpoint hosting the Descriptor cluster to query.
     * @return The part values reported by the device, or `null` if none are reported.
     * @throws Exception If the underlying read request fails.
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

    /**
     * Reads Nordic manufacturer-specific attributes for an endpoint.
     *
     * @param endpoint The endpoint hosting the manufacturer-specific cluster.
     * @param connectedDevicePtr A native pointer to the connected device.
     * @return A [ManufacturerSpecificData] instance when required attributes are available,
     *   otherwise `null`.
     */
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

    /**
     * Reads the `DeviceTypeList` attribute of the Descriptor cluster on the given endpoint.
     *
     * @param devicePtr A native pointer to the connected device.
     * @param endpoint The endpoint hosting the Descriptor cluster to query.
     * @return The device type structs reported by the device for this endpoint.
     * @throws Exception If the underlying read request fails.
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
     * Reads the `ServerList` attribute of the Descriptor cluster on the given endpoint.
     *
     * @param devicePtr A native pointer to the connected device.
     * @param endpoint The endpoint hosting the Descriptor cluster to query.
     * @return The cluster IDs of the server clusters implemented on this endpoint.
     * @throws Exception If the underlying read request fails.
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

    /**
     * Reads the `ClientList` attribute of the Descriptor cluster on the given endpoint.
     *
     * @param devicePtr A native pointer to the connected device.
     * @param endpoint The endpoint hosting the Descriptor cluster to query.
     * @return The cluster IDs of the client clusters implemented on this endpoint.
     * @throws Exception If the underlying read request fails.
     */
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

    /**
     * Creates a [ChipClusters.DescriptorCluster] binding for the given device and endpoint.
     *
     * @param devicePtr A native pointer to the connected device.
     * @param endpoint The endpoint the returned cluster binding will target.
     * @return A new [ChipClusters.DescriptorCluster] bound to [devicePtr] and [endpoint].
     */
    private fun getDescriptorClusterForDevice(
        devicePtr: Long,
        endpoint: Int
    ): ChipClusters.DescriptorCluster {
        return ChipClusters.DescriptorCluster(devicePtr, endpoint)
    }

    companion object {
        private val TAG: String
            get() = ClustersHelper::class.java.simpleName
    }

}