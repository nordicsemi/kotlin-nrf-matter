package no.nordicsemi.nrf.matter.commission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import multiplatform.network.cmptoast.ToastDuration
import multiplatform.network.cmptoast.ToastGravity
import multiplatform.network.cmptoast.showToast
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.ui.AlertDialogView
import no.nordicsemi.nrf.matter.ui.Loader

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
 * Renders the feedback for an ongoing or finished decommissioning: the progress loader, the
 * error dialog offering a force remove, and the success toast.
 *
 * This must be hosted above the device list, so that it stays in composition even when the last
 * device disappears from the list as a result of the decommissioning. Otherwise, the terminal
 * states would never be observed and the toast would only show up once the list becomes
 * non-empty again.
 */
@Composable
internal fun DecommissionStateHandler(
    state: DecommissionState,
    onForceRemove: (DeviceId) -> Unit,
    onStateHandled: () -> Unit,
) {
    when (state) {
        DecommissionState.Idle -> {
            // DO NOTHING
        }

        is DecommissionState.InProgress -> {
            // Show loader while the device is being removed.
            Loader {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Removing device...",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        "It might take a few seconds, please wait!",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        is DecommissionState.Error -> {
            // Show error dialog with an option to force remove.
            AlertDialogView(
                onDismiss = onStateHandled,
                onConfirm = { onForceRemove(state.deviceId) },
                title = "Error Removing Device",
                message = "An error occurred while removing the device. Force remove?"
            )
        }

        is DecommissionState.Success -> {
            LaunchedEffect(state) {
                showToast(
                    message = "Device decommissioned successfully!",
                    duration = ToastDuration.Long,
                    gravity = ToastGravity.Center
                )
                onStateHandled()
            }
        }
    }
}