package no.nordicsemi.nrf.matter.chip

import android.content.Context
import chip.devicecontroller.ChipDeviceController
import chip.devicecontroller.ControllerParams
import chip.devicecontroller.DiscoveredDevice
import chip.devicecontroller.GetConnectedDeviceCallbackJni
import chip.devicecontroller.InvokeCallback
import chip.devicecontroller.NetworkCredentials
import chip.devicecontroller.OpenCommissioningCallback
import chip.devicecontroller.PaseVerifierParams
import chip.devicecontroller.ReportCallback
import chip.devicecontroller.UnpairDeviceCallback
import chip.devicecontroller.WriteAttributesCallback
import chip.devicecontroller.model.AttributeState
import chip.devicecontroller.model.AttributeWriteRequest
import chip.devicecontroller.model.ChipAttributePath
import chip.devicecontroller.model.ChipEventPath
import chip.devicecontroller.model.InvokeElement
import chip.devicecontroller.model.NodeState
import chip.platform.AndroidBleManager
import chip.platform.AndroidChipPlatform
import chip.platform.ChipMdnsCallbackImpl
import chip.platform.DiagnosticDataProviderImpl
import chip.platform.NsdManagerServiceBrowser
import chip.platform.NsdManagerServiceResolver
import chip.platform.PreferencesConfigurationManager
import chip.platform.PreferencesKeyValueStoreManager
import io.github.aakira.napier.Napier
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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
class ChipClient(
    private val context: Context,
) {

    /* 0xFFF4 is a test vendor ID, replace with your assigned company ID */
    private val VENDOR_ID = 0xFFF4

    private val DEFAULT_TIMEOUT = 1000

    // Lazily instantiate [ChipDeviceController] and hold a reference to it.
    val chipDeviceController: ChipDeviceController by lazy {
        ChipDeviceController.loadJni()
        AndroidChipPlatform(
            AndroidBleManager(),
            PreferencesKeyValueStoreManager(context),
            PreferencesConfigurationManager(context),
            NsdManagerServiceResolver(context),
            NsdManagerServiceBrowser(context),
            ChipMdnsCallbackImpl(),
            DiagnosticDataProviderImpl(context)
        )
        ChipDeviceController(
            ControllerParams.newBuilder().setUdpListenPort(0).setControllerVendorId(VENDOR_ID)
                .build()
        )
    }

    /**
     * Wrapper around [ChipDeviceController.getConnectedDevicePointer] to return the value directly.
     */
    suspend fun getConnectedDevicePointer(nodeId: Long): Long {
        return suspendCoroutine { continuation ->
            chipDeviceController.getConnectedDevicePointer(
                nodeId,
                object : GetConnectedDeviceCallbackJni.GetConnectedDeviceCallback {
                    override fun onDeviceConnected(devicePointer: Long) {
                        Napier.d { "AAA, Got connected device pointer" }
                        continuation.resume(devicePointer)
                    }

                    override fun onConnectionFailure(nodeId: Long, error: Exception) {
                        val errorMessage = "Unable to get connected device with nodeId $nodeId."
                        Napier.e(error) { errorMessage }
                        continuation.resumeWithException(IllegalStateException(errorMessage))
                    }
                })
        }
    }

    /**
     * Removes the app's fabric from the device.
     *
     * @param nodeId node identifier
     */
    suspend fun awaitUnpairDevice(nodeId: Long) {
        return suspendCancellableCoroutine { continuation ->
            Napier.d { "AAA, Calling chipDeviceController.unpair" }
            val callback: UnpairDeviceCallback =
                object : UnpairDeviceCallback {
                    override fun onError(status: Int, nodeId: Long) {
                        if (continuation.isActive) {
                            continuation.resumeWithException(
                                IllegalStateException(
                                    "Failed unpairing device [$nodeId] with status [$status]"
                                )
                            )
                        }
                    }

                    override fun onSuccess(nodeId: Long) {
                        if (continuation.isActive) {
                            Napier.d { "AAA, awaitUnpairDevice.onSuccess: deviceId [$nodeId]" }
                            continuation.resume(Unit)
                        }
                    }
                }
            chipDeviceController.unpairDeviceCallback(nodeId, callback)
            continuation.invokeOnCancellation {
                Napier.d { "AAA, Unpair coroutine cancelled" }
            }
        }
    }

    fun computePaseVerifier(
        devicePtr: Long,
        pinCode: Long,
        iterations: Long,
        salt: ByteArray
    ): PaseVerifierParams {
        Napier.d {
            "AAA, computePaseVerifier: devicePtr [${devicePtr}] pinCode [${pinCode}] iterations [${iterations}] salt [${salt}]"
        }
        return chipDeviceController.computePaseVerifier(devicePtr, pinCode, iterations, salt)
    }

    suspend fun awaitEstablishPaseConnection(
        deviceId: Long,
        ipAddress: String,
        port: Int,
        setupPinCode: Long
    ) {
        return suspendCoroutine { continuation ->
            chipDeviceController.setCompletionListener(
                object : BaseCompletionListener() {
                    override fun onConnectDeviceComplete() {
                        super.onConnectDeviceComplete()
                        continuation.resume(Unit)
                    }

                    // Note that an error in processing is not necessarily communicated via onError().
                    // onCommissioningComplete with a "code != 0" also denotes an error in processing.
                    override fun onPairingComplete(code: Int) {
                        super.onPairingComplete(code)
                        if (code != 0) {
                            continuation.resumeWithException(
                                IllegalStateException("Pairing failed with error code [${code}]")
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
                        errorCode: Int
                    ) {
                        super.onCommissioningStatusUpdate(nodeId, stage, errorCode)
                        continuation.resume(Unit)
                    }
                })

            // Temporary workaround to remove interface indexes from ipAddress
            // due to https://github.com/project-chip/connectedhomeip/pull/19394/files
            // TODO: Fix it.
//            chipDeviceController.establishPaseConnection(
//                deviceId, stripLinkLocalInIpAddress(ipAddress), port, setupPinCode)
            chipDeviceController.establishPaseConnection(deviceId, ipAddress, port, setupPinCode)
        }
    }

    suspend fun awaitCommissionDevice(deviceId: Long, networkCredentials: NetworkCredentials?) {
        return suspendCoroutine { continuation ->
            chipDeviceController.setCompletionListener(
                object : BaseCompletionListener() {
                    // Note that an error in processing is not necessarily communicated via onError().
                    // onCommissioningComplete with an "errorCode != 0" also denotes an error in processing.
                    override fun onCommissioningComplete(nodeId: Long, errorCode: Int) {
                        super.onCommissioningComplete(nodeId, errorCode)
                        if (errorCode != 0) {
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
            chipDeviceController.commissionDevice(deviceId, networkCredentials)
        }
    }

    suspend fun awaitOpenPairingWindowWithPIN(
        connectedDevicePointer: Long,
        duration: Int,
        iteration: Long,
        discriminator: Int,
        setupPinCode: Long
    ) {
        return suspendCoroutine { continuation ->
            Napier.d { "AAA, Calling chipDeviceController.openPairingWindowWithPIN" }
            val callback: OpenCommissioningCallback =
                object : OpenCommissioningCallback {
                    override fun onError(status: Int, deviceId: Long) {
                        Napier.e { "AAA, awaitOpenPairingWindowWithPIN.onError: status [${status}] device [${deviceId}]" }
                        continuation.resumeWithException(
                            java.lang.IllegalStateException(
                                "Failed opening the pairing window with status [${status}]"
                            )
                        )
                    }

                    override fun onSuccess(
                        deviceId: Long,
                        manualPairingCode: String?,
                        qrCode: String?
                    ) {
                        Napier.d { "AAA, awaitOpenPairingWindowWithPIN.onSuccess: deviceId [${deviceId}]" }
                        continuation.resume(Unit)
                    }
                }
            chipDeviceController.openPairingWindowWithPINCallback(
                connectedDevicePointer, duration, iteration, discriminator, setupPinCode, callback
            )
        }
    }

    /**
     * Wrapper around [ChipDeviceController.getConnectedDevicePointer] to return the value directly.
     */
    suspend fun awaitGetConnectedDevicePointer(nodeId: Long): Long {
        return suspendCoroutine { continuation ->
            chipDeviceController.getConnectedDevicePointer(
                nodeId,
                object : GetConnectedDeviceCallbackJni.GetConnectedDeviceCallback {
                    override fun onDeviceConnected(devicePointer: Long) {
                        Napier.d { "AAA, Got connected device pointer" }
                        continuation.resume(devicePointer)
                    }

                    override fun onConnectionFailure(nodeId: Long, error: Exception) {
                        val errorMessage = "Unable to get connected device with nodeId $nodeId"
                        Napier.e(error) { errorMessage }
                        continuation.resumeWithException(IllegalStateException(errorMessage))
                    }
                })
        }
    }

    // ---------------------------------------------------------------------------
    // We use our own mDNS discovery code, but interesting to note that
    // ChipDeviceController also offers that feature.

    fun getCommissionableNodes() {
        chipDeviceController.discoverCommissionableNodes()
    }

    fun getDiscoveredDevice(index: Int): DiscoveredDevice? {
        Napier.d { "AAA, getDiscoveredDevice(${index})" }
        return chipDeviceController.getDiscoveredDevice(index)
    }

    // ---------------------------------------------------------------------------
    // Access clusters via numeric ids. Useful to access manufacturer specific clusters.

    suspend fun writeAttribute(
        devicePtr: Long,
        attributePath: ChipAttributePath,
        tlv: ByteArray,
        timedRequestTimeoutMs: Int = DEFAULT_TIMEOUT,
        imTimeoutMs: Int = DEFAULT_TIMEOUT
    ) {
        return writeAttributes(
            devicePtr, mapOf(attributePath to tlv), timedRequestTimeoutMs, imTimeoutMs
        )
    }

    /** Wrapper around [ChipDeviceController.write] */
    suspend fun writeAttributes(
        devicePtr: Long,
        attributes: Map<ChipAttributePath, ByteArray>,
        timedRequestTimeoutMs: Int = DEFAULT_TIMEOUT,
        imTimeoutMs: Int = DEFAULT_TIMEOUT
    ) {
        return suspendCoroutine { continuation ->
            val requests: List<AttributeWriteRequest> =
                attributes.toList().map {
                    AttributeWriteRequest.newInstance(
                        it.first.endpointId, it.first.clusterId, it.first.attributeId, it.second
                    )
                }
            val callback: WriteAttributesCallback =
                object : WriteAttributesCallback {
                    override fun onError(
                        attributePath: ChipAttributePath?,
                        e: java.lang.Exception?
                    ) {
                        continuation.resumeWithException(
                            IllegalStateException(
                                "writeAttributes failed",
                                e
                            )
                        )
                    }

                    override fun onResponse(attributePath: ChipAttributePath?) {
                        if (attributePath!! ==
                            ChipAttributePath.newInstance(
                                requests.last().endpointId,
                                requests.last().clusterId,
                                requests.last().attributeId
                            )
                        ) {
                            continuation.resume(Unit)
                        }
                    }
                }

            chipDeviceController.write(
                callback,
                devicePtr,
                requests,
                timedRequestTimeoutMs,
                imTimeoutMs
            )
        }
    }

    suspend fun readAttribute(devicePtr: Long, attributePath: ChipAttributePath): AttributeState? {
        return readAttributes(devicePtr, listOf(attributePath))[attributePath]
    }

    /** Wrapper around [ChipDeviceController.readAttributePath] */
    suspend fun readAttributes(
        devicePtr: Long,
        attributePaths: List<ChipAttributePath>
    ): Map<ChipAttributePath, AttributeState> {
        return suspendCoroutine { continuation ->
            val callback: ReportCallback =
                object : ReportCallback {
                    override fun onError(
                        attributePath: ChipAttributePath?,
                        eventPath: ChipEventPath?,
                        e: Exception?
                    ) {
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
                            var endpoint: Int = path.endpointId.id.toInt()
                            states[path] =
                                nodeState!!
                                    .getEndpointState(endpoint)!!
                                    .getClusterState(path.clusterId.id)!!
                                    .getAttributeState(path.attributeId.id)!!
                        }
                        continuation.resume(states)
                    }

                    override fun onDone() {
                        super.onDone()
                    }
                }
            chipDeviceController.readAttributePath(callback, devicePtr, attributePaths)
        }
    }

    /** Wrapper around [ChipDeviceController.subscribeToAttributePath] */
    suspend fun subscribeToAttribute(
        devicePtr: Long,
        attributePath: ChipAttributePath,
        minInterval: Int,
        maxInterval: Int,
        callback: ReportCallback
    ) {
        return suspendCoroutine { continuation ->
            chipDeviceController.subscribeToAttributePath(
                { continuation.resume(Unit) },
                callback,
                devicePtr,
                listOf(attributePath),
                minInterval,
                maxInterval
            )
        }
    }

    /** Wrapper around [ChipDeviceController.invoke] */
    suspend fun invoke(
        devicePtr: Long,
        invokeElement: InvokeElement,
        timedRequestTimeoutMs: Int = DEFAULT_TIMEOUT,
        imTimeoutMs: Int = DEFAULT_TIMEOUT
    ): Long {
        return suspendCoroutine { continuation ->
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
}
