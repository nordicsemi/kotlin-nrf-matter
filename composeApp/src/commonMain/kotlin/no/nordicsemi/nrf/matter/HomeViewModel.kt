package no.nordicsemi.nrf.matter

import androidx.lifecycle.ViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceController
import no.nordicsemi.nrf.matter.model.DeviceType
import no.nordicsemi.nrf.matter.model.DeviceUiModel
import no.nordicsemi.nrf.matter.model.Devices
import no.nordicsemi.nrf.matter.model.DevicesListUiModel
import no.nordicsemi.nrf.matter.model.DevicesState
import no.nordicsemi.nrf.matter.model.UserPreferences
import no.nordicsemi.nrf.matter.repository.DevicesRepository
import no.nordicsemi.nrf.matter.repository.DevicesStateRepository
import no.nordicsemi.nrf.matter.repository.UserPreferencesRepository

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

class HomeViewModel(
    private val devicesRepository: DevicesRepository,
    private val devicesStateRepository: DevicesStateRepository,
    userPreferencesRepository: UserPreferencesRepository,
    private val deviceController: DeviceController,
) : ViewModel() {
    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )
    private val devicesListUiModelFlow: Flow<DevicesListUiModel> =
        combine(
            devicesRepository.devicesFlow,
            devicesStateRepository.devicesStateFlow,
            userPreferencesRepository.userPreferencesFlow
        ) { devices, states, prefs ->
            DevicesListUiModel(
                devices = processDevices(devices, states, prefs),
                showOfflineDevices = !prefs.hideOfflineDevices
            )
        }

    val devicesUiModelFlow: StateFlow<DevicesListUiModel> =
        devicesListUiModelFlow.stateIn(
            scope,
            SharingStarted.Eagerly,
            DevicesListUiModel(emptyList(), showOfflineDevices = true)
        )

    private fun updateDeviceStateRepository(deviceId: Long, isOnline: Boolean, isOn: Boolean) {
        scope.launch {
            devicesStateRepository.updateDeviceState(deviceId, isOnline = isOnline, isOn = isOn)
        }
    }

    private fun processDevices(
        devices: Devices,
        devicesStates: DevicesState,
        userPreferences: UserPreferences
    ): List<DeviceUiModel> {
        val list = mutableListOf<DeviceUiModel>()
        devices.devicesList.forEach { device ->
            val state = devicesStates.devicesStateList.find { it.deviceId == device.deviceId }
            if (userPreferences.hideOfflineDevices && state?.online != true) return@forEach
            if (state == null) {
                list.add(DeviceUiModel(device, isOnline = false, isOn = false))
            } else {
                list.add(DeviceUiModel(device, state.online, state.on))
            }
        }
        return list
    }

    fun addCommissionedDevice(
        device: Device,
        isOnline: Boolean,
        isOn: Boolean,
    ) {
        scope.launch {
            devicesRepository.addDevice(device)
            devicesStateRepository.addDeviceState(
                device.deviceId,
                isOnline = isOnline,
                isOn = isOn
            )
        }
    }

    fun updateDeviceType(
        deviceId: Long,
        deviceType: DeviceType
    ) {
        scope.launch {
            devicesRepository.updateDeviceType(deviceId, deviceType)
        }
    }

    fun commissioningFailed(resultCode: Int) {
        // TODO: Handle commissioning failure with proper UI states.
        if (resultCode == 0) {
            // User simply wilfully exited from commissioning.
            return
        }
    }

    fun updateDeviceState(deviceId: Long, isOnline: Boolean, isOn: Boolean) {
        scope.launch {
            try {
                updateDeviceStateRepository(
                    deviceId = deviceId,
                    isOn = isOn,
                    isOnline = isOnline
                )
                deviceController.setDeviceOnOff(
                    deviceId = deviceId,
                    isDeviceOnline = isOnline,
                    isOn = isOn
                )
            } catch (e: Exception) {
                Napier.e(e) { "Error updating device state: ${e.message}" }
                updateDeviceStateRepository(
                    deviceId = deviceId,
                    isOnline = false,
                    isOn = !isOn
                )
            }
        }
    }
}

