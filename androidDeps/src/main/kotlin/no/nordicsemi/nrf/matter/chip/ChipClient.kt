package no.nordicsemi.nrf.matter.chip

import android.content.Context
import chip.devicecontroller.ChipClusters
import chip.devicecontroller.ChipDeviceController
import chip.devicecontroller.ChipStructs
import chip.devicecontroller.CommissionParameters
import chip.devicecontroller.ControllerParams
import chip.devicecontroller.GetConnectedDeviceCallbackJni
import chip.devicecontroller.InvokeCallback
import chip.devicecontroller.NetworkCredentials
import chip.devicecontroller.ReportCallback
import chip.devicecontroller.SubscriptionEstablishedCallback
import chip.devicecontroller.model.AttributeState
import chip.devicecontroller.model.ChipAttributePath
import chip.devicecontroller.model.ChipEventPath
import chip.devicecontroller.model.InvokeElement
import chip.devicecontroller.model.NodeState
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import matter.tlv.AnonymousTag
import matter.tlv.ContextSpecificTag
import matter.tlv.TlvWriter
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.NetworkConfig
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

/* 0xFFF4 is a test vendor ID, replace with your assigned company ID */
private const val VENDOR_ID = 0xFFF4

private const val DEFAULT_TIMEOUT = 1000

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
            // Pass the Android [Context] so the SDK's BLE manager can scan for and connect to
            // devices itself during BLE-based commissioning (pairDeviceWithCode). The no-arg
            // constructor has no adapter/context and cannot drive discovery.
            AndroidBleManager(context),
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

    private fun toNetworkCredentials(networkConfig: NetworkConfig): NetworkCredentials? = when (networkConfig) {
        is NetworkConfig.Thread -> NetworkCredentials.forThread(
            NetworkCredentials.ThreadCredentials(networkConfig.datasetHex.hexToByteArray())
        )

        is NetworkConfig.WiFi -> NetworkCredentials.forWiFi(
            NetworkCredentials.WiFiCredentials(networkConfig.ssid, networkConfig.password)
        )

        NetworkConfig.OnNetwork -> null
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

    /**
     * Commissions a device end-to-end from its onboarding payload, without any dependency on the
     * Google Home / Play Services commissioning flow.
     *
     * This drives the full Matter commissioning sequence directly on the native controller:
     * the SDK parses [setupCode], discovers the device over BLE (via the [AndroidBleManager]
     * configured with a [Context]) and/or on-network mDNS, performs PASE, provisions the supplied
     * [networkCredentials] (Thread or Wi-Fi), and finalizes commissioning onto this app's fabric.
     *
     * @param deviceId The node ID to assign to the device being commissioned.
     * @param setupCode The onboarding payload — a QR code string (e.g. `MT:...`) or an 11/21-digit
     *   manual pairing code.
     * @param networkConfig The selected network provisioning mode for commissioning.
     * @throws IllegalStateException If commissioning completes with a non-zero error code.
     * @throws Throwable If the native SDK reports an error during commissioning.
     */
    suspend fun awaitCommissionDeviceWithCode(
        deviceId: DeviceId,
        setupCode: String,
        networkConfig: NetworkConfig,
    ) {
        val networkCredentials = toNetworkCredentials(networkConfig)

        return suspendCancellableCoroutine { continuation ->
            chipDeviceController.setCompletionListener(
                object : BaseCompletionListener() {
                    override fun onCommissioningComplete(nodeId: Long, errorCode: Long) {
                        super.onCommissioningComplete(nodeId, errorCode)
                        if (!continuation.isActive) return
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
                        if (continuation.isActive) continuation.resumeWithException(error)
                    }
                })

            continuation.invokeOnCancellation {
                runCatching { chipDeviceController.stopDevicePairing(deviceId.longValue) }
            }

            val commissionParameters = CommissionParameters.Builder()
                .setNetworkCredentials(networkCredentials)
                .build()

            chipDeviceController.pairDeviceWithCode(
                deviceId.longValue,
                setupCode,
                /* discoverOnce = */ true,
                /* useOnlyOnNetworkDiscovery = */ false,
                commissionParameters,
            )
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
     * Invokes a cluster command with a fixed TLV payload encoding an unsigned byte value of
     * `2` in context-specific tag 0.
     *
     * Resolves a CASE session for [deviceId] before invoking. Errors from the invocation
     * are logged and not propagated to the caller.
     *
     * @param deviceId The node ID of the target device.
     * @param isOn `true` to send the On command, `false` to send the Off command.
     * @param endpoint The endpoint hosting the target cluster.
     * @param clusterId The cluster ID of the command to invoke.
     * @param commandId The command ID to invoke.
     */
    suspend fun setLet(
        deviceId: DeviceId,
        isOn: Boolean,
        endpoint: Int,
        clusterId: Long,
        commandId: Long,
    ) {
        val ptr = getConnectedDevicePointer(deviceId.longValue)
        return suspendCancellableCoroutine { continuation ->

            val onCommand = if (isOn) { 1 } else { 0 }.toUByte()

            val tlvWriter = TlvWriter()
            tlvWriter.startStructure(AnonymousTag)
            tlvWriter.put(ContextSpecificTag(0), onCommand)
            tlvWriter.endStructure()
            val invokeElement =
                InvokeElement.newInstance(
                    endpoint,
                    clusterId,
                    commandId,
                    tlvWriter.getEncoded(),
                    null
                )

            val customInvokeCallback = object : InvokeCallback {

                override fun onError(e: Exception) {
                    NordicLogger.error("Error on invoke Callback!", e, tag = TAG)
                    continuation.resume(Unit)
                }

                override fun onResponse(
                    invokeElement: InvokeElement?,
                    successCode: Long
                ) {
                    NordicLogger.info(
                        "Command Response Success!",
                        tag = "SetLet"
                    )
                    continuation.resume(Unit)
                }

            }

            chipDeviceController.invoke(
                customInvokeCallback,
                ptr,
                invokeElement,
                15_000,
                30_000,
            )
        }
    }

    /**
     * Invokes a cluster command with a fixed TLV payload encoding a boolean `true` value
     * in context-specific tag 0.
     *
     * @param devicePtr A native pointer to the connected device.
     * @param path The attribute path whose endpoint, cluster, and attribute IDs are used as
     *   the endpoint, cluster ID, and command ID for the invocation.
     * @throws Exception If the invocation fails.
     */
    suspend fun generateRandomNumber(
        devicePtr: Long,
        path: ChipAttributePath
    ) {
        return suspendCancellableCoroutine { continuation ->
            val fields = TlvWriter().apply {
                startStructure(AnonymousTag)
                put(
                    ContextSpecificTag(0),
                    true
                )
                endStructure()
            }.getEncoded()


            val customInvokeCallback = object : InvokeCallback {

                override fun onError(e: Exception) {
                    NordicLogger.error("Error on invoke Callback!", e, tag = TAG)
                    continuation.resumeWithException(e)
                }

                override fun onResponse(
                    invokeElement: InvokeElement?,
                    successCode: Long
                ) {
                    NordicLogger.info(
                        "Command Response Success!",
                        tag = "GenerateRandomNumber"
                    )
                    continuation.resume(Unit)
                }

            }

            val invokeElement = InvokeElement.newInstance(
                path.endpointId,
                path.clusterId,
                path.attributeId,
                fields,
                null
            )

            chipDeviceController.invoke(
                customInvokeCallback,
                devicePtr,
                invokeElement,
                15_000,
                30_000,
            )
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
     */
    fun subscribeAttribute(
        reportCallback: ReportCallback,
        devicePtr: Long,
        attributePaths: List<ChipAttributePath>,
        minIntervalS: Int,
        maxIntervalS: Int,
        timeoutMs: Int,
    ) {
        chipDeviceController.subscribeToAttributePath(
            object : SubscriptionEstablishedCallback {
                override fun onSubscriptionEstablished(subscriptionId: Long) {
                    NordicLogger.debug(
                        "Subscription established: $subscriptionId",
                        tag = "SubscribeAttribute"
                    )
                }
            },
            reportCallback,
            devicePtr,
            attributePaths,
            minIntervalS,
            maxIntervalS,
            timeoutMs,
        )
    }

    companion object {
        private val TAG: String
            get() = ChipClient::class.java.simpleName
    }

}
