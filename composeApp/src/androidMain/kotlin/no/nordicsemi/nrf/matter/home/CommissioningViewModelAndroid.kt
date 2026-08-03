package no.nordicsemi.nrf.matter.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.chip.ChipClient
import no.nordicsemi.nrf.matter.chip.ClustersHelper
import no.nordicsemi.nrf.matter.chip.MatterBasicInfoProvider
import no.nordicsemi.nrf.matter.commission.CommissioningException
import no.nordicsemi.nrf.matter.model.CommissioningInput
import no.nordicsemi.nrf.matter.commission.Stage
import no.nordicsemi.nrf.matter.commission.toCommissioningException
import no.nordicsemi.nrf.matter.domain.OperationResult
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceType
import no.nordicsemi.nrf.matter.repository.DevicesRepository
import no.nordicsemi.nrf.matter.repository.DevicesStateRepository
import kotlin.time.Clock

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

class CommissioningViewModelAndroid(
    private val chipClient: ChipClient,
    private val basicInfoProvider: MatterBasicInfoProvider,
    private val clustersHelper: ClustersHelper,
    private val devicesRepository: DevicesRepository,
    private val devicesStateRepository: DevicesStateRepository,
) : ViewModel() {

    val nextNodeId = MutableStateFlow<DeviceId?>(null)
    val deviceEvent = Channel<OperationResult<Device>>()

    init {
        viewModelScope.launch {
            nextNodeId.value = devicesRepository.incrementAndReturnLastDeviceId()
        }
    }

    /**
     * Commissions a device end-to-end directly through the CHIP SDK — BLE discovery, PASE, network
     * provisioning, and fabric commissioning — with no dependency on the Google Home / Play
     * Services flow. Reuses the node ID reserved in [init].
     */
    fun commission(input: CommissioningInput) {
        viewModelScope.launch {
            // Wait for the reserved node id from init.
            val deviceId = nextNodeId.first { it != null }!!
            try {
                catchAndThrow(Stage.COMMISSIONING) {
                    chipClient.awaitCommissionDeviceWithCode(
                        deviceId = deviceId,
                        setupCode = input.setupCode,
                        networkConfig = input.network,
                    )
                }

                devicesStateRepository.addDeviceState(deviceId, isOnline = true, isOn = false)

                val basicInfo = catchAndThrow(Stage.READ_BASIC_INFORMATION) {
                    basicInfoProvider.fetchBasicInfo(deviceId)
                }

                val deviceMatterInfoList = catchAndThrow(Stage.READ_DESCRIPTOR_CLUSTER) {
                    clustersHelper.fetchDeviceMatterInfo(deviceId)
                }

                val deviceType = mutableStateListOf<DeviceType>()
                deviceMatterInfoList.forEach {
                    // Ignore the first endpoint because this is the root node.
                    if (it.endpoint != 0) {
                        // Get the device type from the rest of the endpoint.
                        it.types.forEach { type ->
                            val type = convertToAppDeviceType(type)
                            deviceType.add(type)
                        }
                    }
                }
                val device = Device(
                    vendorName = basicInfo.vendorName,
                    productName = basicInfo.productName,
                    dateCommissioned = Clock.System.now()
                        .toEpochMilliseconds(), // Date when the device was commissioned.
                    vendorId = basicInfo.vendorId.toString(),
                    productId = basicInfo.productId.toString(),
                    deviceType = deviceType.firstOrNull() ?: DeviceType.UNSUPPORTED,
                    deviceId = deviceId,
                    name = basicInfo.productName,
                    uniqueId = basicInfo.uniqueId.toString(),
                    softwareVersion = basicInfo.softwareVersion,
                    serialNumer = basicInfo.serialNumber,
                    specificationVersion = basicInfo.specificationVersion,
                    deviceMatterInfo = deviceMatterInfoList,
                )

                deviceEvent.send(OperationResult.Success(device))
            } catch (t: Throwable) {
                NordicLogger.error("Commissioning failed", t, tag = TAG)
                deviceEvent.send(OperationResult.Error(t.toCommissioningException(deviceId)))
            }
        }
    }

    private suspend fun <T> catchAndThrow(stage: Stage, block: suspend () -> T): T {
        try {
            return block()
        } catch (t: Throwable) {
            throw CommissioningException(
                nextNodeId.value,
                stage,
                t.chipErrorCodeOrNull(),
                t.message ?: ""
            )
        }
    }

    private fun Throwable.chipErrorCodeOrNull(): Int? {
        // Avoid compile-time coupling to CHIP exception classes that live in Android-only jars.
        val getter = runCatching { javaClass.getMethod("getErrorCode") }.getOrNull() ?: return null
        return (runCatching { getter.invoke(this) }.getOrNull() as? Number)?.toInt()
    }

    private fun convertToAppDeviceType(matterDeviceType: Long): DeviceType {
        return when (matterDeviceType) {
            256L -> DeviceType.LIGHT_ON_OFF // 0x0100 On/Off Light
            257L -> DeviceType.DIMMABLE_LIGHT // 0x0101 Dimmable Light
            259L -> DeviceType.LIGHT_SWITCH// 0x0103 On/Off Light Switch
            260L -> DeviceType.LIGHT_SWITCH // 0x0104 On/Off Outlet

            266L -> DeviceType.OUTLET // 0x010A (On/Off Plug-in Unit)
            268L -> DeviceType.COLOR_TEMPERATURE_LIGHT // 0x010C Color Temperature Light
            269L -> DeviceType.EXTENDED_COLOR_LIGHT // 0x010D Extended Color Light
            10L -> DeviceType.DOOR_LOCK // 0x000A door lock // todo need to review the hex value
//            11L ->   Door Lock Controller // (0x000B)
            0xFFF10001 -> DeviceType.MANUFACTURER_SPECIFIC_DEVICE
            else -> DeviceType.UNSUPPORTED
        }
    }

    companion object {
        private const val TAG = "Commissioning"
    }
}
