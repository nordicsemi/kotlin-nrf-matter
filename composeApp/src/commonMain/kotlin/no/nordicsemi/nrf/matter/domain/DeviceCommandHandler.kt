package no.nordicsemi.nrf.matter.domain

import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import no.nordicsemi.nrf.matter.device.BindingUiState
import no.nordicsemi.nrf.matter.device.UiState
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceBinding
import no.nordicsemi.nrf.matter.model.DeviceController
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceType
import no.nordicsemi.nrf.matter.repository.BindingRepository
import no.nordicsemi.nrf.matter.repository.DevicesRepository
import no.nordicsemi.nrf.matter.repository.DevicesStateRepository

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
class DeviceCommandHandler(
    private val devicesRepository: DevicesRepository,
    private val devicesStateRepository: DevicesStateRepository,
    private val deviceController: DeviceController,
    private val bindingRepository: BindingRepository,
) {

    suspend fun execute(
        deviceId: DeviceId,
        command: Boolean
    ) {
        val device = devicesRepository.getDeviceOrNull(deviceId) ?: return

        when (device.deviceType) {
            DeviceType.UNKNOWN -> {
                // TODO: Handle unknown devices.
            }

            DeviceType.MANUFACTURER_SPECIFIC_DEVICE -> handleLed(device, command)
            DeviceType.LIGHT_ON_OFF,
            DeviceType.DIMMABLE_LIGHT,
            DeviceType.LIGHT_SWITCH,
            DeviceType.COLOR_TEMPERATURE_LIGHT,
            DeviceType.EXTENDED_COLOR_LIGHT -> handlePower(device, deviceId, command)

            DeviceType.LIGHT_SWITCH, DeviceType.OUTLET -> {
                // Do nothing, since the role of switch is different from other device types.
            }

            DeviceType.DOOR_LOCK -> handleLock(device, deviceId, command)
        }
    }

    private suspend fun handleLed(
        device: Device,
        isOn: Boolean
    ) {
        val deviceId = device.deviceId
        val endpoint = resolveEndpoint(device, clusterId = ON_OFF_CLUSTER_ID)

        try {
            devicesStateRepository.updateDeviceState(
                deviceId = deviceId,
                isOnline = true,
                isOn = isOn
            )

            deviceController.setLed(
                deviceId = deviceId,
                isOn = isOn,
                endpoint = endpoint
            )

        } catch (e: Exception) {

            devicesStateRepository.updateDeviceState(
                deviceId = deviceId,
                isOnline = false,
                isOn = !isOn
            )

            throw e
        }
    }

    private suspend fun handlePower(
        device: Device,
        deviceId: DeviceId,
        isOn: Boolean
    ) {
        val endpoint = resolveEndpoint(device, clusterId = ON_OFF_CLUSTER_ID)

        try {
            devicesStateRepository.updateDeviceState(
                deviceId = deviceId,
                isOnline = true,
                isOn = isOn
            )

            deviceController.setDeviceOnOff(
                deviceId = deviceId,
                isDeviceOnline = true,
                isOn = isOn,
                endpoint = endpoint
            )

        } catch (e: Exception) {

            devicesStateRepository.updateDeviceState(
                deviceId = deviceId,
                isOnline = false,
                isOn = !isOn
            )

            throw e
        }
    }

    private suspend fun handleLock(
        device: Device,
        deviceId: DeviceId,
        isLocked: Boolean
    ) {
        val endpoint =
            resolveEndpoint(
                device,
                clusterId = LOCK_UNLOCK_CLUSTER_ID
            ) // todo: use the proper cluster id

        try {
            devicesStateRepository.updateDeviceState(
                deviceId = deviceId,
                isOnline = true,
                isOn = isLocked
            )

            deviceController.lockUnlockDoor(
                deviceId = deviceId,
                isLocked = isLocked,
                endpoint = endpoint,
            )

        } catch (e: Exception) {

            devicesStateRepository.updateDeviceState(
                deviceId = deviceId,
                isOnline = false,
                isOn = !isLocked
            )

            throw e
        }
    }

    private fun resolveEndpoint(device: Device, clusterId: Long): Int {
        return device.deviceMatterInfo
            .firstOrNull { it.serverClusters.contains(clusterId) }
            ?.endpoint ?: 0 // TODO: change to exception and handle from UI.
    }

    fun bind(
        switchNodeId: DeviceId,
        lightNodeId: DeviceId,
    ): Flow<BindingUiState> = flow {
        emit(UiState.Loading)

        try {
            deviceController.bind(
                sourceNodeId = switchNodeId,
                sourceEndpoint = 1, // TODO: Add a function call that looks the endpoint of the switch where binding is configured.
                targetNodeId = lightNodeId,
                targetEndpoint = 1, // TODO: Add a function call that looks the endpoint of the light where cluster id is configured.
                clusterId = ON_OFF_CLUSTER_ID, // TODO: Change it to provide the cluster id based on the type of binding.
            )

            val bindingDevice = DeviceBinding(
                id = "${switchNodeId}_${lightNodeId}_$ON_OFF_CLUSTER_ID",
                sourceNodeId = switchNodeId,
                targetNodeId = lightNodeId,
                sourceEndpoint = 1,
                targetEndpoint = 1,
                clusterId = ON_OFF_CLUSTER_ID
            )

            bindingRepository.save(bindingDevice)

            emit(UiState.Success(bindingDevice))

        } catch (e: Exception) {
            Napier.e(e) { "Binding failed: ${e.message}" }
            emit(UiState.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    fun subscribeToButtonChanges(deviceId: DeviceId): Flow<Boolean> {
        return deviceController.subscribeToButtonChanges(deviceId, 1)
    }

    fun subscribeToRandomNumber(deviceId: DeviceId): Flow<UInt> {
        return deviceController.subscribeToRandomNumber(deviceId, 1)
    }

    suspend fun generateRandomNumber(deviceId: DeviceId): Int {
        return deviceController.generateRandomNumber(deviceId)
    }

    companion object {
        private val TAG: String
            get() = "DeviceCommandHandler"
        private const val ON_OFF_CLUSTER_ID: Long = 0x0006L
        private const val LOCK_UNLOCK_CLUSTER_ID: Long = 0x0101.toLong()
        private const val MANUFACTURER_SPECIFIC_CLUSTER_ID: Long = 0xFFF1FC01
    }
}
