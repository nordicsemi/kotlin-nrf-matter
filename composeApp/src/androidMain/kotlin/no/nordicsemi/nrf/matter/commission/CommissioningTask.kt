package no.nordicsemi.nrf.matter.commission

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.channels.consumeEach
import no.nordicsemi.nrf.matter.domain.OperationResult
import no.nordicsemi.nrf.matter.home.CommissioningViewModelAndroid
import no.nordicsemi.nrf.matter.model.CommissioningInput
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import org.koin.compose.viewmodel.koinViewModel

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
 * Android commissioning entry point. Drives Matter commissioning entirely through the CHIP SDK
 * (BLE discovery + network provisioning + fabric commissioning), with no dependency on the Google
 * Home / Play Services commissioning API.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun CommissioningTask(
    input: CommissioningInput,
    onSuccess: (Device) -> Unit,
    onError: (CommissioningException) -> Unit,
) {
    val commissioningModelAndroid: CommissioningViewModelAndroid = koinViewModel()

    // BLE discovery/connection during commissioning requires runtime permissions. On Android 12+
    // that is BLUETOOTH_SCAN + BLUETOOTH_CONNECT; below that, fine location covers BLE scanning.
    val blePermissions = remember {
        buildList {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                add(Manifest.permission.BLUETOOTH_SCAN)
                add(Manifest.permission.BLUETOOTH_CONNECT)
            }
            add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    val permissionsState = rememberMultiplePermissionsState(blePermissions)

    LaunchedEffect(Unit) {
        commissioningModelAndroid.deviceEvent.consumeEach {
            when (it) {
                is OperationResult.Error -> onError(
                    it.t.toCommissioningException(
                        commissioningModelAndroid.nextNodeId.value ?: DeviceId.Zero
                    )
                )

                is OperationResult.Success -> onSuccess(it.data)
            }
        }
    }

    LaunchedEffect(permissionsState.allPermissionsGranted) {
        if (permissionsState.allPermissionsGranted) {
            commissioningModelAndroid.commission(input)
        } else {
            permissionsState.launchMultiplePermissionRequest()
        }
    }
}

/**
 * Maps a throwable raised during commissioning to a [CommissioningException]. Already-mapped
 * exceptions are returned unchanged (preserving their originating [Stage]).
 */
fun Throwable.toCommissioningException(deviceId: DeviceId): CommissioningException {
    val chipErrorCode = chipErrorCodeOrNull()
    return when (this) {
        is CommissioningException -> this
        else -> CommissioningException(
            deviceId,
            Stage.COMMISSIONING,
            chipErrorCode,
            message ?: "Unknown error"
        )
    }
}

private fun Throwable.chipErrorCodeOrNull(): Int? {
    // Avoid compile-time coupling to CHIP exception classes that live in Android-only jars.
    val getter = runCatching { javaClass.getMethod("getErrorCode") }.getOrNull() ?: return null
    return (runCatching { getter.invoke(this) }.getOrNull() as? Number)?.toInt()
}

