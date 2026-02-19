package no.nordicsemi.nrf.matter.home

import android.content.Context
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.home.matter.commissioning.CommissioningResult
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.HomeViewModel
import no.nordicsemi.nrf.matter.chip.ChipClient
import no.nordicsemi.nrf.matter.chip.ClustersHelper
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceType

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

class HomeViewModelAndroid(
    private val baseViewModel: HomeViewModel,
    context: Context,
) : ViewModel() {
    val devicesUiModelLiveData = baseViewModel.devicesUiModelFlow

    private var gpsCommissioningResult: CommissioningResult? = null
    val chipsClient: ChipClient = ChipClient(context)
    val clustersHelper: ClustersHelper = ClustersHelper(chipsClient)

    fun gpsCommissioningDeviceSucceeded(activityResult: ActivityResult) {
        gpsCommissioningResult =
            CommissioningResult.fromIntentSenderResult(
                activityResult.resultCode,
                activityResult.data
            )
        // TODO: Now we need to capture the device name.
        onCommissionedDeviceNameCaptured("Device-Test")
    }

    fun commissionDeviceFailed(resultCode: Int) {
        baseViewModel.commissioningFailed(resultCode)
    }

    fun onCommissionedDeviceNameCaptured(deviceName: String) {
        viewModelScope.launch {
            val deviceId = gpsCommissioningResult?.token?.toLong()!!
            val vendorName =
                try {
                    clustersHelper.readBasicClusterVendorNameAttribute(deviceId)
                } catch (ex: Exception) {
                    Napier.e(ex) { "AAA, Failed to read VendorName attribute with exception: $ex" }
                    ""
                }

            val productName =
                try {
                    clustersHelper.readBasicClusterProductNameAttribute(deviceId)
                } catch (ex: Exception) {
                    Napier.e(ex) { "AAA, Failed to read ProductName attribute with exception: $ex" }
                    ""
                }

            try {
                val deviceType = convertToAppDeviceType(
                    gpsCommissioningResult?.commissionedDeviceDescriptor?.deviceType?.toLong()!!
                )
                val device = Device(
                    vendorName = vendorName,
                    productName = productName,
                    dateCommissioned = gpsCommissioningResult?.token?.toLong(),
                    vendorId = gpsCommissioningResult?.commissionedDeviceDescriptor?.vendorId.toString(),
                    productId = gpsCommissioningResult?.commissionedDeviceDescriptor?.productId.toString(),
                    deviceType = deviceType,
                    deviceId = deviceId,
                    name = gpsCommissioningResult?.deviceName,
                )
                baseViewModel.addCommissionedDevice(device, isOnline = true, isOn = false)
            } catch (e: Exception) {
                val msg = "Adding device [${deviceId}] [${deviceName}] to app's repository failed."
                Napier.e(e) { "BBB, onCommissionedDeviceNameCaptured: $msg, $e" }
            }

            val deviceMatterInfoList = clustersHelper.fetchDeviceMatterInfo(deviceId)
            var gotDeviceType = false
            deviceMatterInfoList.forEach { deviceMatterInfo ->
                Napier.d("AAA, Processing endpoint [${deviceMatterInfo.endpoint}]")
                // Endpoint 0 is the Root Node, so we disregard it.
                if (deviceMatterInfo.endpoint != 0) {
                    if (gotDeviceType) {
                        // TODO: Handle this properly once we have specific examples to learn from.
                        return@forEach
                    }
                    if (deviceMatterInfo.types.size > 1) {
                        // TODO: Handle this properly once we have specific examples to learn from.
                        baseViewModel.updateDeviceType(
                            deviceId,
                            convertToAppDeviceType(deviceMatterInfo.types.first()),
                        )
                        gotDeviceType = true
                    }
                }
            }

            // update device name
            try {
                clustersHelper.writeBasicClusterNodeLabelAttribute(deviceId, deviceName)
            } catch (ex: Exception) {
                Napier.e(ex) { "AAA,  Failed to write NodeLabel $deviceName with exception: $ex" }
            }
        }
    }

    private fun convertToAppDeviceType(matterDeviceType: Long): DeviceType {
        return when (matterDeviceType) {
            256L -> DeviceType.LIGHT_ON_OFF // 0x0100 On/Off Light
            257L -> DeviceType.DIMMABLE_LIGHT // 0x0101 Dimmable Light
            259L -> DeviceType.LIGHT_SWITCH// 0x0103 On/Off Light Switch
            266L -> DeviceType.OUTLET // 0x010A (On/Off Plug-in Unit)
            268L -> DeviceType.COLOR_TEMPERATURE_LIGHT // 0x010C Color Temperature Light
            269L -> DeviceType.EXTENDED_COLOR_LIGHT // 0x010D Extended Color Light
            else -> DeviceType.UNKNOWN
        }
    }

}

