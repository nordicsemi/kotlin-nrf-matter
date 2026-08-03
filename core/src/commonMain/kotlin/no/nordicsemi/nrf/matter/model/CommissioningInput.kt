package no.nordicsemi.nrf.matter.model

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
 * Everything the app needs to commission a Matter device directly via the CHIP SDK, without the
 * Google Home / Play Services commissioning flow.
 *
 * @property setupCode The device onboarding payload: a QR code string (e.g. `MT:...`) or an
 *   11/21-digit manual pairing code.
 * @property network How the device should be attached to the operational network.
 */
data class CommissioningInput(
    val setupCode: String,
    val network: NetworkConfig,
)

/**
 * The operational network to provision onto the device during commissioning.
 */
sealed interface NetworkConfig {

    /**
     * Provision a Thread network using the border router's operational dataset.
     *
     * @property datasetHex The Thread active operational dataset, hex-encoded (as exported from a
     *   Thread Border Router / `ot-ctl dataset active -x`).
     */
    data class Thread(val datasetHex: String) : NetworkConfig

    /**
     * Provision a Wi-Fi network.
     */
    data class WiFi(val ssid: String, val password: String) : NetworkConfig

    /**
     * No network provisioning — the device is already reachable on the operational IP network.
     */
    data object OnNetwork : NetworkConfig
}
