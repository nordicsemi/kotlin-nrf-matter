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
 * ChipDeviceController uses a CompletionListener for callbacks. This is a "base" default
 * implementation for that CompletionListener.
 */
abstract class BaseCompletionListener : ChipDeviceController.CompletionListener {
    override fun onConnectDeviceComplete() {
        NordicLogger.info("Connect Device Complete!", tag = TAG)
    }

    override fun onStatusUpdate(status: Int) {
        NordicLogger.info("Status Updated, status [${status}]", tag = TAG)
    }

    override fun onPairingComplete(errorCode: Long) {
        NordicLogger.info("Pairing Completed!", tag = TAG)
    }

    override fun onPairingDeleted(errorCode: Long) {
        NordicLogger.info("AAA, BaseCompletionListener onPairingDeleted(): errorCode [${errorCode}]")
    }

    override fun onCommissioningComplete(nodeId: Long, errorCode: Long) {
        NordicLogger.debug(
            "Commissioning Complete! nodeId [${nodeId}], errorCode [${errorCode}]", tag = TAG
        )
    }

    override fun onNotifyChipConnectionClosed() {
        NordicLogger.debug("Notify Chip Connection Closed!", tag = TAG)
    }

    override fun onCloseBleComplete() {
        NordicLogger.debug("Close Ble Completed!", tag = TAG)
    }

    override fun onError(error: Throwable) {
        NordicLogger.error("Commission Error", error, tag = TAG)
    }

    override fun onOpCSRGenerationComplete(csr: ByteArray) {
        NordicLogger.debug("Op CSR GenerationCompleted! CSR: ${csr.toHexString()}", tag = TAG)
    }

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

    override fun onCommissioningStatusUpdate(nodeId: Long, stage: String?, errorCode: Long) {
        NordicLogger.debug(
            "Commissioning Status Updated! \tnodeId [${nodeId}]\tstage [${stage}]\terrorCode [${errorCode}]",
            tag = TAG
        )
    }

    override fun onCommissioningStageStart(nodeId: Long, stage: String?) {
        NordicLogger.debug(
            "Commissioning Stage Started! \tnodeId [${nodeId}]\tstage [${stage}]",
            tag = TAG
        )
    }

    override fun onICDRegistrationComplete(errorCode: Long, icdDeviceInfo: ICDDeviceInfo?) {
        NordicLogger.debug(
            "ICD Registration Completed! \terrorCode [${errorCode}]\ticdDeviceInfo [${icdDeviceInfo}]",
            tag = TAG
        )
    }

    override fun onICDRegistrationInfoRequired() {
        NordicLogger.debug("ICD Registration Info Required!", tag = TAG)
    }

    companion object {
        private val TAG = BaseCompletionListener::class.java.simpleName
    }
}
