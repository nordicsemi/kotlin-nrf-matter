package no.nordicsemi.nrf.matter.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.Devices
import no.nordicsemi.nrf.matter.model.toDeviceId

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

internal class DevicesRepository(
    private val dataSource: DevicesDataSource
) {

    val devicesFlow: Flow<Devices> = dataSource.devicesFlow

    suspend fun incrementAndReturnLastDeviceId(): DeviceId {
        var newId = 0L
        dataSource.update { devices ->
            newId = devices.lastDeviceId.longValue + 1
            devices.copy(lastDeviceId = newId.toDeviceId())
        }
        return newId.toDeviceId()
    }

    suspend fun getNextDeviceId(): DeviceId {
        return dataSource.devicesFlow.first().lastDeviceId
    }

    suspend fun addDevice(device: Device) {
        dataSource.update { devices ->
            devices.copy(devicesList = devices.devicesList + device)
        }
    }

    suspend fun removeDevice(deviceId: DeviceId) {
        var removed = false

        dataSource.update { devices ->
            val filtered = devices.devicesList.filter {
                if (it.deviceId == deviceId) {
                    removed = true
                    false
                } else true
            }
            devices.copy(devicesList = filtered)
        }

        if (!removed) {
            throw IllegalStateException("Device not found: $deviceId")
        }
    }

    suspend fun getAllDevices(): Devices =
        devicesFlow.first()

}
