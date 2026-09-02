package no.nordicsemi.nrf.matter.commission

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.model.DeviceId
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
internal class DecommissionUseCases(
    private val deviceController: MatterDecommissioner,
    private val devicesStateRepository: DevicesStateRepository,
    private val devicesRepository: DevicesRepository,
    private val bindingRepository: BindingRepository,
) {
    private fun decommissionFlow(
        deviceId: DeviceId,
        action: suspend () -> Unit
    ): Flow<DecommissionState> = flow {
        emit(DecommissionState.InProgress)

        try {
            action()
            emit(DecommissionState.Success(deviceId))
        } catch (e: Exception) {
            emit(
                DecommissionState.Error(
                    deviceId = deviceId,
                    message = e.message
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    fun decommissionDevice(deviceId: DeviceId): Flow<DecommissionState> =
        decommissionFlow(deviceId) {
            deviceController.decommission(deviceId)
            devicesStateRepository.removeDevice(deviceId)
            devicesRepository.removeDevice(deviceId)
            // Let's also remove all bindings for this device, as it is decommissioned and won't be able to communicate with other devices.
            bindingRepository.delete(deviceId)
        }


    fun forceRemoveDevice(deviceId: DeviceId): Flow<DecommissionState> =
        decommissionFlow(deviceId) {
            devicesStateRepository.removeDevice(deviceId)
            devicesRepository.removeDevice(deviceId)
            bindingRepository.delete(deviceId)

        }
}