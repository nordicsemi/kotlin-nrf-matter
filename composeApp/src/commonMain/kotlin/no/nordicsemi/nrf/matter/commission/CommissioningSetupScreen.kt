package no.nordicsemi.nrf.matter.commission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.nordicsemi.nrf.matter.model.CommissioningInput
import no.nordicsemi.nrf.matter.model.NetworkConfig

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

private enum class NetworkChoice { THREAD, WIFI, ON_NETWORK }

/**
 * Collects the onboarding payload and network credentials needed to commission a device directly
 * (no Google Home). Supports scanning a QR code or entering the setup code manually, and choosing
 * between Thread, Wi-Fi, or on-network provisioning.
 *
 * @param onCommission Invoked with the assembled [no.nordicsemi.nrf.matter.model.CommissioningInput] when the user taps Continue.
 */
@Composable
fun CommissioningSetupScreen(
    onCommission: (CommissioningInput) -> Unit,
) {
    var showScanner by remember { mutableStateOf(false) }

    var setupCode by remember { mutableStateOf("") }
    var networkChoice by remember { mutableStateOf(NetworkChoice.THREAD) }
    var threadDatasetHex by remember { mutableStateOf("") }
    var ssid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scanner = qrScannerContent
    if (showScanner && scanner != null) {
        scanner(
            Modifier.fillMaxSize(),
            { code ->
                setupCode = code.trim()
                showScanner = false
            },
            { showScanner = false },
        )
        return
    }

    val network: NetworkConfig = when (networkChoice) {
        NetworkChoice.THREAD -> NetworkConfig.Thread(threadDatasetHex.trim())
        NetworkChoice.WIFI -> NetworkConfig.WiFi(ssid.trim(), password)
        NetworkChoice.ON_NETWORK -> NetworkConfig.OnNetwork
    }

    val canContinue = setupCode.isNotBlank() && when (networkChoice) {
        NetworkChoice.THREAD -> threadDatasetHex.isNotBlank()
        NetworkChoice.WIFI -> ssid.isNotBlank()
        NetworkChoice.ON_NETWORK -> true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 24.dp),
    ) {
        Text(
            text = "Add a Matter device",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Commission directly over BLE — no Google Home required.",
            fontSize = 14.sp,
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = setupCode,
            onValueChange = { setupCode = it },
            label = { Text("Setup code (QR payload or manual code)") },
            placeholder = { Text("MT:… or 21-digit code") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        if (scanner != null) {
            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = { showScanner = true },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Icon(Icons.Rounded.QrCodeScanner, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Scan QR code")
            }
        }

        Spacer(Modifier.height(24.dp))

        Text("Network", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(Modifier.height(4.dp))

        NetworkOption("Thread", networkChoice == NetworkChoice.THREAD) {
            networkChoice = NetworkChoice.THREAD
        }
        NetworkOption("Wi-Fi", networkChoice == NetworkChoice.WIFI) {
            networkChoice = NetworkChoice.WIFI
        }
        NetworkOption("Already on network", networkChoice == NetworkChoice.ON_NETWORK) {
            networkChoice = NetworkChoice.ON_NETWORK
        }

        Spacer(Modifier.height(12.dp))

        when (networkChoice) {
            NetworkChoice.THREAD -> OutlinedTextField(
                value = threadDatasetHex,
                onValueChange = { threadDatasetHex = it },
                label = { Text("Thread operational dataset (hex)") },
                placeholder = { Text("0e08000000000001000035060004…") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            NetworkChoice.WIFI -> Column {
                OutlinedTextField(
                    value = ssid,
                    onValueChange = { ssid = it },
                    label = { Text("Wi-Fi SSID") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Wi-Fi password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            NetworkChoice.ON_NETWORK -> Text(
                text = "The device must already be reachable on your IP network.",
                fontSize = 13.sp,
            )
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { onCommission(CommissioningInput(setupCode.trim(), network)) },
            enabled = canContinue,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp),
        ) {
            Text("Continue", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun NetworkOption(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .selectable(selected = selected, onClick = onSelect),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Spacer(Modifier.width(4.dp))
        Text(label, fontSize = 15.sp)
    }
}
