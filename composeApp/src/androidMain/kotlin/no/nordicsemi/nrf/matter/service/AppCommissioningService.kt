package no.nordicsemi.nrf.matter.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.android.gms.home.matter.commissioning.CommissioningCompleteMetadata
import com.google.android.gms.home.matter.commissioning.CommissioningRequestMetadata
import com.google.android.gms.home.matter.commissioning.CommissioningService
import com.google.android.gms.home.matter.commissioning.CommissioningService.CommissioningError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.api.androidMatterPlatform
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

class AppCommissioningService : Service(), CommissioningService.Callback {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    // The service runs in the app's process, so the library was initialized in
    // Application.onCreate long before Google Home binds to it.
    private val dependencies by lazy { NordicMatters.requireDependencies() }
    private val devicesRepository by lazy { dependencies.devicesRepository }
    private val devicesStateRepository by lazy { dependencies.devicesStateRepository }
    private val chipClient by lazy { androidMatterPlatform.chipClient }

    private lateinit var commissioningServiceDelegate: CommissioningService

    override fun onCreate() {
        super.onCreate()
        // May be invoked without MainActivity being called to initialize APP_NAME.
        // So do it here as well.
        commissioningServiceDelegate = CommissioningService.Builder(this).setCallback(this).build()
    }

    override fun onBind(intent: Intent?): IBinder {
        return commissioningServiceDelegate.asBinder()
    }

    override fun onCommissioningRequested(metadata: CommissioningRequestMetadata) {
        Log.d(
            "CommissionService",
            "*** onCommissioningRequested ***:\n" +
                    "\tdeviceDescriptor: " +
                    "deviceType [${metadata.deviceDescriptor.deviceType}] " +
                    "vendorId [${metadata.deviceDescriptor.vendorId}] " +
                    "productId [${metadata.deviceDescriptor.productId}]\n" +
                    "\tnetworkLocation: " +
                    "IP address [${metadata.networkLocation.ipAddress}] " +
                    "IP address hostAddress [${metadata.networkLocation.ipAddress.hostAddress}] " +
                    "port [${metadata.networkLocation.port}]\n" +
                    "\tpassCode [${metadata.passcode}]"
        )
        
        // Perform commissioning on custom fabric for the sample app.
        serviceScope.launch {
            val deviceId = devicesRepository.getNextDeviceId()
            try {
                chipClient.awaitEstablishPaseConnection(
                    deviceId,
                    metadata.networkLocation.ipAddress.hostAddress!!,
                    metadata.networkLocation.port,
                    metadata.passcode
                )

                chipClient.awaitCommissionDevice(deviceId)

                devicesStateRepository.addDeviceState(
                    deviceId,
                    isOnline = true,
                    isOn = false
                )

                commissioningServiceDelegate
                    .sendCommissioningComplete(
                        CommissioningCompleteMetadata.builder().setToken(deviceId.stringValue)
                            .build()
                    )
                    .addOnSuccessListener {
                        Log.d(
                            "CommissionService",
                            "Device commissioned succeeded!"
                        )
                    }
                    .addOnFailureListener { e2 ->
                        Log.e(
                            "CommissionService",
                            "Device commissioned failed!",
                            e2,
                        )
                    }

            } catch (e: Exception) {
                // No way to determine whether this was ATTESTATION_FAILED or DEVICE_UNREACHABLE.
                commissioningServiceDelegate
                    .sendCommissioningError(CommissioningError.OTHER)
                    .addOnFailureListener { e2 ->
                        Log.e(
                            "CommissionService",
                            "Device commissioned failed!",
                            e2,
                        )
                    }
                return@launch
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

}