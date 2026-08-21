package no.nordicsemi.nrf.matter.chip

import android.content.Context
import chip.devicecontroller.ChipClusters
import chip.devicecontroller.ChipDeviceController
import chip.devicecontroller.ChipStructs
import chip.devicecontroller.CommissionParameters
import chip.devicecontroller.ControllerParams
import chip.devicecontroller.GetConnectedDeviceCallbackJni
import chip.devicecontroller.InvokeCallback
import chip.devicecontroller.ReportCallback
import chip.devicecontroller.SubscriptionEstablishedCallback
import chip.devicecontroller.WriteAttributesCallback
import chip.devicecontroller.model.AttributeState
import chip.devicecontroller.model.AttributeWriteRequest
import chip.devicecontroller.model.ChipAttributePath
import chip.devicecontroller.model.ChipEventPath
import chip.devicecontroller.model.InvokeElement
import chip.devicecontroller.model.NodeState
import chip.devicecontroller.model.Status
import chip.platform.AndroidBleManager
import chip.platform.AndroidChipLogging
import chip.platform.AndroidChipPlatform
import chip.platform.AndroidNfcCommissioningManager
import chip.platform.ChipMdnsCallbackImpl
import chip.platform.DiagnosticDataProviderImpl
import chip.platform.NsdManagerServiceBrowser
import chip.platform.NsdManagerServiceResolver
import chip.platform.PreferencesConfigurationManager
import chip.platform.PreferencesKeyValueStoreManager
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId
import java.util.Optional
import java.util.concurrent.atomic.AtomicReference
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

/* 0xFFF4 is a test vendor ID, replace with your assigned company ID */
private const val VENDOR_ID = 0xFFF4

private const val DEFAULT_TIMEOUT = 1000

private const val DEFAULT_IM_TIMEOUT = 30_000
private const val DEFAULT_SUBSCRIPTION_MIN_INTERVAL_S = 0
private const val DEFAULT_SUBSCRIPTION_MAX_INTERVAL_S = 10
private const val DEFAULT_SUBSCRIPTION_TIMEOUT_MS = 10_000

/**
 * Manages the lifecycle of the Matter (CHIP) native device controller and provides
 * coroutine-based wrappers around its callback-driven APIs.
 *
 * Supported operations include commissioning, PASE/CASE session establishment,
 * attribute reads and writes, command invocation, attribute subscriptions,
 * and fabric management for decommissioning.
 *
 * @property context The Android [Context] used to initialize the underlying CHIP platform
 *   integrations (BLE, NFC, mDNS, and persistent storage).
 */
class ChipClient(
    private val context: Context,
) {
    /**
     * A stream of native CHIP SDK log lines, each prefixed with its originating module name.
     *
     * Buffers up to 200 entries and drops the oldest entry on overflow.
     */
    val chipLogFlow = MutableSharedFlow<String>(
        extraBufferCapacity = 200,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /**
     * The lazily-initialized [ChipDeviceController] backing all operations in this class.
     *
     * On first access, this property:
     * 1. Loads the native CHIP JNI library.
     * 2. Wires native log output into [chipLogFlow].
     * 3. Configures the Android CHIP platform (BLE, NFC, mDNS, and preference-backed storage).
     * 4. Constructs the controller with [VENDOR_ID] as the vendor ID.
     */
    val chipDeviceController: ChipDeviceController by lazy {
        ChipDeviceController.loadJni()

        AndroidChipLogging.setLogCallback { module, _, message ->

            chipLogFlow.tryEmit("[$module] $message")
        }

        AndroidChipPlatform(
            AndroidBleManager(),
            AndroidNfcCommissioningManager(),
            PreferencesKeyValueStoreManager(context),
            PreferencesConfigurationManager(context),
            NsdManagerServiceResolver(context),
            NsdManagerServiceBrowser(context),
            ChipMdnsCallbackImpl(),
            DiagnosticDataProviderImpl(context)
        )
        ChipDeviceController(
            ControllerParams.newBuilder()
                .setUdpListenPort(0)
                .setControllerVendorId(VENDOR_ID)
                .build()
        )
    }

    /**
     * Resolves a native device pointer for an already-commissioned node by establishing
     * a CASE session if one is not currently active.
     *
     * @param nodeId The Matter node ID of the target device.
     * @return A native pointer to the connected device. The caller is responsible for
     *   releasing it via [ChipDeviceController.releaseConnectedDevicePointer].
     * @throws IllegalStateException If the connection attempt fails.
     */
    suspend fun getConnectedDevicePointer(nodeId: Long): Long {
        return suspendCancellableCoroutine { continuation ->
            chipDeviceController.getConnectedDevicePointer(
                nodeId,
                object : GetConnectedDeviceCallbackJni.GetConnectedDeviceCallback {
                    override fun onDeviceConnected(devicePointer: Long) {
                        continuation.resume(devicePointer)
                    }

                    override fun onConnectionFailure(nodeId: Long, error: Exception) {
                        val errorMessage = "Unable to get connected device with nodeId $nodeId."
                        NordicLogger.error(errorMessage, error, tag = TAG)
                        continuation.resumeWithException(IllegalStateException(errorMessage))
                    }
                })
        }
    }

    /**
     * Removes all fabrics from a commissioned device, returning it to an uncommissioned state.
     *
     * This method reads all fabrics present on the device, removes any foreign fabrics first
     * (e.g., those added by a third-party ecosystem), and finally removes this application's
     * own fabric.
     *
     * @param deviceId The Matter node ID of the device to decommission.
     */
    suspend fun decommissionDevice(deviceId: Long) {
        NordicLogger.info("Decommission device: $deviceId", tag = TAG)

        var connectedDevicePtr: Long? = null

        try {
            connectedDevicePtr = getConnectedDevicePointer(deviceId)
            // Read ALL fabrics (fabric-filtered = false)
            val fabrics = readFabrics(connectedDevicePtr, fabricFiltered = false)
            val ownFabrics = readFabrics(connectedDevicePtr, fabricFiltered = true)

            if (fabrics.isEmpty()) {
                NordicLogger.info("No fabrics — already decommissioned", tag = TAG)
                return
            }

            // Filter out our own fabric from the list of fabrics to remove.
            val foreignFabrics = fabrics.filterNot { fabric ->
                ownFabrics.any { it.fabricIndex == fabric.fabricIndex }
            }

            // Since we commissioned the device using the Google Home app,
            // it will have a foreign fabric that we need to remove first.
            // Then we can remove our own fabric last.
            for (fabric in foreignFabrics) {
                runCatching {
                    removeFabric(connectedDevicePtr, fabric.fabricIndex)
                }.onSuccess {
                    NordicLogger.info(
                        "Foreign fabric ${fabric.fabricIndex} removed",
                        tag = TAG
                    )
                }.onFailure {
                    NordicLogger.error(
                        "Error removing foreign fabric ${fabric.fabricIndex}: $it",
                        it as? Exception,
                        tag = TAG
                    )
                }
            }

            // Remove own fabric
            ownFabrics.firstOrNull()?.let { fabric ->
                NordicLogger.info("Removing own fabric index=${fabric.fabricIndex}... ", TAG)
                runCatching {
                    removeFabric(connectedDevicePtr, fabric.fabricIndex)
                }.onFailure {
                    NordicLogger.error(
                        "Failed removing own fabric ${fabric.fabricIndex}",
                        it as? Exception,
                        tag = TAG
                    )
                }
            }
            NordicLogger.info("Device $deviceId fully decommissioned.", tag = TAG)
        } finally {
            connectedDevicePtr?.let {
                chipDeviceController.releaseConnectedDevicePointer(it)
            }
            NordicLogger.info("Released connected device pointer for device $deviceId", tag = TAG)
        }
    }

    /**
     * Reads the `Fabrics` attribute from the Operational Credentials cluster on endpoint 0.
     *
     * @param connectedDevicePtr A native pointer to the connected device.
     * @param fabricFiltered If `true`, restricts results to the fabric of the caller's active
     *   session. If `false`, returns all fabrics known to the device.
     * @return The list of fabric descriptors reported by the device.
     * @throws Exception If the underlying attribute read fails.
     */
    private suspend fun readFabrics(
        connectedDevicePtr: Long,
        fabricFiltered: Boolean
    ): List<ChipStructs.OperationalCredentialsClusterFabricDescriptorStruct> =
        suspendCancellableCoroutine { continuation ->
            val cluster =
                ChipClusters.OperationalCredentialsCluster(connectedDevicePtr, 0)

            val callback =
                object : ChipClusters.OperationalCredentialsCluster.FabricsAttributeCallback {
                    override fun onSuccess(
                        values: List<ChipStructs.OperationalCredentialsClusterFabricDescriptorStruct>
                    ) {
                        continuation.resume(values)
                    }

                    override fun onError(error: Exception) {
                        continuation.resumeWithException(error)
                    }
                }

            cluster.readFabricsAttributeWithFabricFilter(callback, fabricFiltered)
        }

    /**
     * Sends the `RemoveFabric` command to the Operational Credentials cluster on endpoint 0.
     *
     * @param connectedDevicePtr A native pointer to the connected device.
     * @param fabricIndex The index of the fabric to remove.
     * @throws Exception If the command invocation fails.
     */
    private suspend fun removeFabric(
        connectedDevicePtr: Long,
        fabricIndex: Int
    ) = suspendCancellableCoroutine { continuation ->
        ChipClusters.OperationalCredentialsCluster(connectedDevicePtr, 0)
            .removeFabric(
                object : ChipClusters.OperationalCredentialsCluster.NOCResponseCallback {
                    override fun onSuccess(
                        statusCode: Int?,
                        fabricIndex: Optional<Int?>?,
                        debugText: Optional<String?>?
                    ) {
                        continuation.resume(Unit)
                    }

                    override fun onError(error: Exception) {
                        continuation.resumeWithException(error)
                    }
                },
                fabricIndex
            )
    }

    /**
     * Establishes a PASE (Password-Authenticated Session Establishment) connection with a
     * device over IP as the first step of the Matter commissioning flow.
     *
     * Suspends until the controller reports one of the following events: device connected,
     * commissioning info read, a commissioning status update, or pairing complete.
     *
     * @param deviceId The node ID to assign to the device being paired.
     * @param ipAddress The IP address of the target device.
     * @param port The port of the target device.
     * @param setupPinCode The setup PIN code from the device's onboarding payload.
     * @throws IllegalStateException If pairing completes with a non-zero error code.
     * @throws Throwable If the native SDK reports an error during the connection attempt.
     */
    suspend fun awaitEstablishPaseConnection(
        deviceId: DeviceId,
        ipAddress: String,
        port: Int,
        setupPinCode: Long
    ) {
        return suspendCancellableCoroutine { continuation ->
            chipDeviceController.setCompletionListener(
                object : BaseCompletionListener() {
                    override fun onConnectDeviceComplete() {
                        super.onConnectDeviceComplete()
                        continuation.resume(Unit)
                    }

                    // Note that an error in processing is not necessarily communicated via onError().
                    // onCommissioningComplete with a "code != 0" also denotes an error in processing.
                    override fun onPairingComplete(errorCode: Long) {
                        super.onPairingComplete(errorCode)
                        if (errorCode != 0L) {
                            continuation.resumeWithException(
                                IllegalStateException("Pairing failed with error code [${errorCode}]")
                            )
                        } else {
                            continuation.resume(Unit)
                        }
                    }

                    override fun onError(error: Throwable) {
                        super.onError(error)
                        continuation.resumeWithException(error)
                    }

                    override fun onReadCommissioningInfo(
                        vendorId: Int,
                        productId: Int,
                        wifiEndpointId: Int,
                        threadEndpointId: Int
                    ) {
                        super.onReadCommissioningInfo(
                            vendorId,
                            productId,
                            wifiEndpointId,
                            threadEndpointId
                        )
                        continuation.resume(Unit)
                    }

                    override fun onCommissioningStatusUpdate(
                        nodeId: Long,
                        stage: String?,
                        errorCode: Long
                    ) {
                        super.onCommissioningStatusUpdate(nodeId, stage, errorCode)
                        continuation.resume(Unit)
                    }
                })
            chipDeviceController.establishPaseConnection(
                deviceId.longValue,
                ipAddress,
                port,
                setupPinCode
            )
        }
    }

    /**
     * Commissions a device that already has an established PASE connection.
     *
     * Completes the Matter commissioning flow without supplying network credentials,
     * suitable for devices that do not require Wi-Fi or Thread provisioning.
     *
     * @param deviceId The node ID of the device to commission, matching the ID used with
     *   [awaitEstablishPaseConnection].
     * @throws IllegalStateException If commissioning completes with a non-zero error code.
     * @throws Throwable If the native SDK reports an error during commissioning.
     */
    suspend fun awaitCommissionDevice(deviceId: DeviceId) {
        return suspendCancellableCoroutine { continuation ->
            chipDeviceController.setCompletionListener(
                object : BaseCompletionListener() {
                    override fun onCommissioningComplete(nodeId: Long, errorCode: Long) {
                        super.onCommissioningComplete(nodeId, errorCode)
                        if (errorCode != 0L) {
                            continuation.resumeWithException(
                                IllegalStateException("Commissioning failed with error code [${errorCode}]")
                            )
                        } else {
                            continuation.resume(Unit)
                        }
                    }

                    override fun onError(error: Throwable) {
                        super.onError(error)
                        continuation.resumeWithException(error)
                    }
                })

            val commissionParameters = CommissionParameters.Builder()
                .setNetworkCredentials(null)
                .build()

            chipDeviceController.commissionDevice(deviceId.longValue, commissionParameters)
        }
    }

    suspend fun readAttribute(devicePtr: Long, endpoint: Int, clusterId: Long, attributeId: Long): Any? {
        val path = ChipAttributePath.newInstance(endpoint, clusterId, attributeId)
        return readAttribute(devicePtr, path)?.value
    }

    suspend fun writeAttribute(
        devicePtr: Long,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long,
        value: Any?,
        timedRequestTimeoutMs: Int = 0,
        imTimeoutMs: Int = DEFAULT_IM_TIMEOUT,
    ) {
        val request = AttributeWriteRequest.newInstance(
            endpoint,
            clusterId,
            attributeId,
            encodeAttributeValue(value),
        )
        return suspendCancellableCoroutine { continuation ->
            val callback = object : WriteAttributesCallback {
                override fun onError(attributePath: ChipAttributePath?, e: Exception) {
                    if (!continuation.isActive) return
                    NordicLogger.error(
                        "Error on writeAttribute callback for path: $attributePath",
                        e,
                        tag = TAG
                    )
                    continuation.resumeWithException(
                        IllegalStateException("writeAttribute failed", e)
                    )
                }

                override fun onResponse(attributePath: ChipAttributePath?, status: Status?) {
                    if (!continuation.isActive) return
                    val code = status?.status
                    if (code != null && code != Status.Code.Success) {
                        continuation.resumeWithException(
                            IllegalStateException("writeAttribute failed with status $code")
                        )
                    } else {
                        continuation.resume(Unit)
                    }
                }
            }

            chipDeviceController.write(
                callback,
                devicePtr,
                listOf(request),
                timedRequestTimeoutMs,
                imTimeoutMs,
            )
        }
    }

    suspend fun invokeCommand(
        devicePtr: Long,
        endpoint: Int,
        clusterId: Long,
        commandId: Long,
        value: Any?,
        timedRequestTimeoutMs: Int = 0,
        imTimeoutMs: Int = DEFAULT_IM_TIMEOUT,
    ): Any? {
        val invokeElement = InvokeElement.newInstance(
            endpoint,
            clusterId,
            commandId,
            encodeCommandFields(value),
            null,
        )
        return suspendCancellableCoroutine { continuation ->
            val callback = object : InvokeCallback {
                override fun onError(e: Exception) {
                    if (!continuation.isActive) return
                    NordicLogger.error(
                        "Error on invoke callback for command $commandId of cluster $clusterId",
                        e,
                        tag = TAG
                    )
                    continuation.resumeWithException(IllegalStateException("invoke failed", e))
                }

                override fun onResponse(invokeElement: InvokeElement?, successCode: Long) {
                    if (!continuation.isActive) return
                    continuation.resume(decodeCommandResponse(invokeElement?.tlvByteArray))
                }
            }

            chipDeviceController.invoke(
                callback,
                devicePtr,
                invokeElement,
                timedRequestTimeoutMs,
                imTimeoutMs,
            )
        }
    }

    fun observeAttribute(
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long,
        minIntervalS: Int = DEFAULT_SUBSCRIPTION_MIN_INTERVAL_S,
        maxIntervalS: Int = DEFAULT_SUBSCRIPTION_MAX_INTERVAL_S,
        timeoutMs: Int = DEFAULT_SUBSCRIPTION_TIMEOUT_MS,
    ): Flow<Any?> = callbackFlow {
        val devicePtr = getConnectedDevicePointer(deviceId.longValue)

        val subscriptionId = AtomicReference<Long?>(null)
        val reportCallback = object : ReportCallback {
            override fun onError(
                attributePath: ChipAttributePath?,
                eventPath: ChipEventPath?,
                e: Exception
            ) {
                NordicLogger.error(
                    "Error receiving report for path: $attributePath",
                    e,
                    tag = TAG
                )
                close(e)
            }

            override fun onReport(nodeState: NodeState?) {
                val attributeState = nodeState?.getEndpointState(endpoint)
                    ?.getClusterState(clusterId)
                    ?.getAttributeState(attributeId)
                    ?: return
                trySend(attributeState.value)
            }
        }

        subscribeAttribute(
            reportCallback = reportCallback,
            devicePtr = devicePtr,
            attributePaths = listOf(
                ChipAttributePath.newInstance(endpoint, clusterId, attributeId)
            ),
            minIntervalS = minIntervalS,
            maxIntervalS = maxIntervalS,
            timeoutMs = timeoutMs,
            onSubscriptionEstablished = { subscriptionId.set(it) },
        )

        awaitClose {
            subscriptionId.get()?.let {
                chipDeviceController.shutdownSubscriptions(
                    chipDeviceController.fabricIndex,
                    deviceId.longValue,
                    it,
                )
            }
            NordicLogger.debug("Stopped observing attribute $attributeId", tag = TAG)
        }
    }

    /**
     * Reads a single attribute from a device.
     *
     * Convenience wrapper over [readAttributes] for single-attribute reads.
     *
     * @param devicePtr A native pointer to the connected device.
     * @param attributePath The path of the attribute to read.
     * @return The attribute's [AttributeState], or `null` if the device did not report it.
     * @throws IllegalStateException If the underlying read fails.
     */
    suspend fun readAttribute(devicePtr: Long, attributePath: ChipAttributePath): AttributeState? {
        return readAttributes(devicePtr, listOf(attributePath))[attributePath]
    }

    /**
     * Reads one or more attributes from a device in a single interaction.
     *
     * The read is subject to a 30-second timeout.
     *
     * @param devicePtr A native pointer to the connected device.
     * @param attributePaths The paths of the attributes to read.
     * @return A map from each requested [ChipAttributePath] to its resolved [AttributeState].
     * @throws IllegalStateException If the underlying read fails.
     */
    suspend fun readAttributes(
        devicePtr: Long,
        attributePaths: List<ChipAttributePath>
    ): Map<ChipAttributePath, AttributeState> {
        return suspendCancellableCoroutine { continuation ->
            val callback: ReportCallback =
                object : ReportCallback {
                    override fun onError(
                        attributePath: ChipAttributePath?,
                        eventPath: ChipEventPath?,
                        e: Exception
                    ) {
                        NordicLogger.error("Error on readAttributes Callback!", e, tag = TAG)
                        continuation.resumeWithException(
                            IllegalStateException(
                                "readAttributes failed",
                                e
                            )
                        )
                    }

                    override fun onReport(nodeState: NodeState?) {
                        val states: HashMap<ChipAttributePath, AttributeState> = HashMap()
                        for (path in attributePaths) {
                            val endpoint: Int = path.endpointId.id.toInt()
                            nodeState?.getEndpointState(endpoint)
                                ?.getClusterState(path.clusterId.id)
                                ?.getAttributeState(path.attributeId.id)?.let {
                                    states[path] = it
                                }
                        }
                        continuation.resume(states)
                    }

                }

            chipDeviceController.readAttributePath(callback, devicePtr, attributePaths, 30_000)

            continuation.invokeOnCancellation {
                NordicLogger.debug("Read attribute coroutine cancelled", tag = TAG)
            }
        }
    }

    /**
     * Invokes a cluster command on a device and returns the response status code.
     *
     * @param devicePtr A native pointer to the connected device.
     * @param invokeElement The command to invoke, including its endpoint, cluster, command ID,
     *   and TLV-encoded fields.
     * @param timedRequestTimeoutMs The timeout in milliseconds for the timed-invoke window.
     *   Defaults to [DEFAULT_TIMEOUT].
     * @param imTimeoutMs The timeout in milliseconds for the Interaction Model exchange.
     *   Defaults to [DEFAULT_TIMEOUT].
     * @return The success status code returned by the device.
     * @throws IllegalStateException If the invocation fails.
     */
    suspend fun invoke(
        devicePtr: Long,
        invokeElement: InvokeElement,
        timedRequestTimeoutMs: Int = DEFAULT_TIMEOUT,
        imTimeoutMs: Int = DEFAULT_TIMEOUT
    ): Long {
        return suspendCancellableCoroutine { continuation ->
            val invokeCallback: InvokeCallback =
                object : InvokeCallback {
                    override fun onError(e: java.lang.Exception?) {
                        continuation.resumeWithException(IllegalStateException("invoke failed", e))
                    }

                    override fun onResponse(invokeElement: InvokeElement?, successCode: Long) {
                        continuation.resume(successCode)
                    }
                }
            chipDeviceController.invoke(
                invokeCallback, devicePtr, invokeElement, timedRequestTimeoutMs, imTimeoutMs
            )
        }
    }

    /**
     * Subscribes to one or more attributes and delivers ongoing reports to [reportCallback].
     *
     * @param reportCallback The callback invoked for each attribute report and on error.
     * @param devicePtr A native pointer to the connected device.
     * @param attributePaths The paths of the attributes to subscribe to.
     * @param minIntervalS The minimum reporting interval in seconds.
     * @param maxIntervalS The maximum reporting interval in seconds.
     * @param timeoutMs The timeout in milliseconds for establishing the subscription.
     * @param onSubscriptionEstablished Invoked with the subscription ID once the device confirms
     *   the subscription. The ID can be passed to [ChipDeviceController.shutdownSubscriptions]
     *   to tear down this subscription alone.
     */
    fun subscribeAttribute(
        reportCallback: ReportCallback,
        devicePtr: Long,
        attributePaths: List<ChipAttributePath>,
        minIntervalS: Int,
        maxIntervalS: Int,
        timeoutMs: Int,
        onSubscriptionEstablished: (Long) -> Unit = {},
    ) {
        chipDeviceController.subscribeToPath(
            { subscriptionId ->
                NordicLogger.debug(
                    "Subscription established: $subscriptionId",
                    tag = "SubscribeAttribute"
                )
                onSubscriptionEstablished(subscriptionId)
            },
            null,
            reportCallback,
            devicePtr,
            attributePaths,
            emptyList(),
            minIntervalS,
            maxIntervalS,
            true,
            false,
            timeoutMs,
        )
    }

    companion object {
        private val TAG: String
            get() = ChipClient::class.java.simpleName
    }

}
