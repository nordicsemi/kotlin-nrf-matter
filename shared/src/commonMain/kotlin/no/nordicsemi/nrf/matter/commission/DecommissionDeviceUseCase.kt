package no.nordicsemi.nrf.matter.commission

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import no.nordicsemi.nrf.matter.api.Fabric
import no.nordicsemi.nrf.matter.model.DeviceId
import kotlin.coroutines.cancellation.CancellationException

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

class DecommissionDeviceUseCase(private val fabric: Fabric) {

    /** Unlinks the fabric at the device, then forgets it. */
    fun decommissionDevice(deviceId: DeviceId): Flow<DecommissionState> =
        decommissionFlow(deviceId) { fabric.decommissionDevice(deviceId) }

    /** Forgets the device without unlinking the fabric at the device itself. */
    fun forceRemoveDevice(deviceId: DeviceId): Flow<DecommissionState> =
        decommissionFlow(deviceId) { fabric.forceRemoveDevice(deviceId) }

    private fun decommissionFlow(
        deviceId: DeviceId,
        action: suspend () -> Unit,
    ): Flow<DecommissionState> = flow {
        emit(DecommissionState.InProgress)
        try {
            action()
            emit(DecommissionState.Success(deviceId))
        } catch (c: CancellationException) {
            throw c
        } catch (e: Exception) {
            emit(DecommissionState.Error(deviceId = deviceId, message = e.message))
        }
    }
}
