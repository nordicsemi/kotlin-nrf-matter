package no.nordicsemi.nrf.matter.chip

import chip.devicecontroller.ChipDeviceController
import chip.devicecontroller.ICDDeviceInfo
import no.nordicsemi.nrf.matter.logger.NordicLogger

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
/**
 * Default logging implementation of [ChipDeviceController.CompletionListener].
 *
 * This base class provides no-op behavior beyond structured logging for all callback
 * events, allowing subclasses to override only the callbacks they need.
 */
abstract class BaseCompletionListener : ChipDeviceController.CompletionListener {
    /**
     * Called when device connection completes.
     */
    override fun onConnectDeviceComplete() {
        NordicLogger.info("Connect Device Complete!", tag = TAG)
    }

    /**
     * Called when the controller reports a status update.
     *
     * @param status The status code provided by the controller.
     */
    override fun onStatusUpdate(status: Int) {
        NordicLogger.info("Status Updated, status [${status}]", tag = TAG)
    }

    /**
     * Called when pairing completes.
     *
     * @param errorCode The pairing result code.
     */
    override fun onPairingComplete(errorCode: Long) {
        NordicLogger.info("Pairing Completed!", tag = TAG)
    }

    /**
     * Called when a pairing is deleted.
     *
     * @param errorCode The deletion result code.
     */
    override fun onPairingDeleted(errorCode: Long) {
        NordicLogger.info("Pairing deleted, errorCode [${errorCode}]", tag = TAG)
    }

    /**
     * Called when commissioning completes.
     *
     * @param nodeId The commissioned node ID.
     * @param errorCode The commissioning result code.
     */
    override fun onCommissioningComplete(nodeId: Long, errorCode: Long) {
        NordicLogger.info(
            "Commissioning Complete! nodeId [${nodeId}], errorCode [${errorCode}]", tag = TAG
        )
    }

    /**
     * Called when the CHIP connection is closed.
     */
    override fun onNotifyChipConnectionClosed() {
        NordicLogger.debug("Notify Chip Connection Closed!", tag = TAG)
    }

    /**
     * Called when BLE teardown completes.
     */
    override fun onCloseBleComplete() {
        NordicLogger.debug("Close Ble Completed!", tag = TAG)
    }

    /**
     * Called when the controller reports an error.
     *
     * @param error The reported error.
     */
    override fun onError(error: Throwable) {
        NordicLogger.error("Commission Error", error, tag = TAG)
    }

    /**
     * Called when operational CSR generation completes.
     *
     * @param csr The generated certificate signing request bytes.
     */
    override fun onOpCSRGenerationComplete(csr: ByteArray) {
        NordicLogger.debug("Op CSR GenerationCompleted!", tag = TAG)
    }

    /**
     * Called when commissioning metadata is read.
     *
     * @param vendorId The device vendor ID.
     * @param productId The device product ID.
     * @param wifiEndpointId The Wi-Fi endpoint ID.
     * @param threadEndpointId The Thread endpoint ID.
     */
    override fun onReadCommissioningInfo(
        vendorId: Int,
        productId: Int,
        wifiEndpointId: Int,
        threadEndpointId: Int
    ) {
        NordicLogger.info(
            "Read Commissioning Info \nvendorId [${vendorId}],\tproductId [${productId}],\twifiEndpointId [${wifiEndpointId}],\tthreadEndpointId [${threadEndpointId}]",
            tag = TAG
        )
    }

    /**
     * Called when the commissioning status changes.
     *
     * @param nodeId The target node ID.
     * @param stage The current commissioning stage.
     * @param errorCode The stage result code.
     */
    override fun onCommissioningStatusUpdate(nodeId: Long, stage: String?, errorCode: Long) {
        NordicLogger.debug(
            "Commissioning Status Updated! \tnodeId [${nodeId}]\tstage [${stage}]\terrorCode [${errorCode}]",
            tag = TAG
        )
    }

    /**
     * Called when a commissioning stage starts.
     *
     * @param nodeId The target node ID.
     * @param stage The stage that started.
     */
    override fun onCommissioningStageStart(nodeId: Long, stage: String?) {
        NordicLogger.debug(
            "Commissioning Stage Started! \tnodeId [${nodeId}]\tstage [${stage}]",
            tag = TAG
        )
    }

    /**
     * Called when ICD registration completes.
     *
     * @param errorCode The registration result code.
     * @param icdDeviceInfo The returned ICD device info, if available.
     */
    override fun onICDRegistrationComplete(errorCode: Long, icdDeviceInfo: ICDDeviceInfo?) {
        NordicLogger.debug(
            "ICD Registration Completed! \terrorCode [${errorCode}]",
            tag = TAG
        )
    }

    /**
     * Called when ICD registration information is required.
     */
    override fun onICDRegistrationInfoRequired() {
        NordicLogger.debug("ICD Registration Info Required!", tag = TAG)
    }

    companion object {
        private const val TAG = "MatterCommissioning"
    }
}
