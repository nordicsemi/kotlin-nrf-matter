package no.nordicsemi.nrf.matter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.api.Fabric
import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.commission.DecommissionState
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceState
import no.nordicsemi.nrf.matter.model.DeviceUiModel
import no.nordicsemi.nrf.matter.model.DevicesListUiModel
import no.nordicsemi.nrf.matter.ui.DeviceViewModelCache
import no.nordicsemi.nrf.matter.ui.device.DeviceViewModel

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

class HomeViewModel : ViewModel() {

    private val fabric: Fabric = NordicMatters.defaultFabric
    private val matterControllerCache = DeviceViewModelCache()

    private val _decommissionState = MutableStateFlow<DecommissionState>(DecommissionState.Idle)
    val decommissionState = _decommissionState.asStateFlow()

    private val devicesListUiModelFlow: Flow<DevicesListUiModel> =
        combine(
            fabric.devices,
            fabric.deviceStates,
        ) { devices, states ->
            DevicesListUiModel(
                devices = processDevices(devices, states),

                )
        }

    val devices: StateFlow<List<DeviceViewModel>> =
        devicesListUiModelFlow.map { uiModel ->
            matterControllerCache.retainOnly(uiModel.devices.map { it.device.deviceId }.toSet())

            uiModel.devices.map { device ->
                (matterControllerCache[device.device.deviceId] ?: matterControllerCache.create(
                    device
                )).also {
                    NordicLogger.debug("Device $it", "HomeViewModel")
                }
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val devicesUiModelFlow: StateFlow<DevicesListUiModel> =
        devicesListUiModelFlow.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            DevicesListUiModel(emptyList())
        )

    private fun processDevices(
        devices: List<Device>,
        devicesStates: List<DeviceState>
    ): List<DeviceUiModel> {
        val list = mutableListOf<DeviceUiModel>()
        devices.forEach { device ->
            val state = devicesStates.find { it.deviceId == device.deviceId }
            if (state == null) {
                list.add(DeviceUiModel(device, isOnline = false, isOn = false))
            } else {
                list.add(DeviceUiModel(device, state.online, state.on))
            }
        }
        return list
    }

    /**
     * Removes the device. First we remove the fabric from the device, and then we remove the device from the app's devices repository.
     * Note that unlinking the device may take a while if the device is offline.
     * If removing the fabric from the device fails (e.g. device is offline),
     * then a dialog is shown so the user has the option to force remove the device without unlinking
     * the fabric at the device.
     */
    fun decommissionDevice(deviceId: DeviceId) {
        viewModelScope.launch {
            fabric.decommissionDevice(deviceId).collect {
                updateDecommissionState(it)
            }
        }
    }

    fun updateDecommissionState(state: DecommissionState) {
        _decommissionState.update { state }
    }

    /**
     * Force removes the device from the app's devices repository without unlinking the fabric at the device.
     */
    fun forceRemove(deviceId: DeviceId) {
        viewModelScope.launch {
            fabric.forceRemoveDevice(deviceId).collect {
                updateDecommissionState(it)
            }
        }
    }
}
