package no.nordicsemi.nrf.matter.model

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import no.nordicsemi.nrf.matter.MatterBinder
import no.nordicsemi.nrf.matter.MatterClusterExtensionController
import no.nordicsemi.nrf.matter.MatterDecommissioner
import no.nordicsemi.nrf.matter.MatterDoorController
import no.nordicsemi.nrf.matter.MatterManufacturerCustomDataController
import no.nordicsemi.nrf.matter.MatterOnOffController
import no.nordicsemi.nrf.matter.MatterOutletController

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

class IosDeviceController(
    private val matterOnOffController: MatterOnOffController,
    private val matterDecommissioner: MatterDecommissioner,
    private val matterBinder: MatterBinder,
    private val matterDoorController: MatterDoorController,
    private val matterOutletController: MatterOutletController,
    private val matterManufacturerCustomDataController: MatterManufacturerCustomDataController,
    private val matterClusterExtensionController: MatterClusterExtensionController,
): DeviceController {

    override suspend fun setDeviceOnOff(
        deviceId: DeviceId,
        isDeviceOnline: Boolean,
        isOn: Boolean,
        endpoint: Int,
    ) {
        matterOnOffController.setDeviceOnOff(deviceId, isOn, endpoint)
    }

    override suspend fun setLed(
        deviceId: DeviceId,
        isOn: Boolean,
        endpoint: Int
    ) {
        matterManufacturerCustomDataController.setLed(deviceId, isOn, endpoint)
    }

    override suspend fun unlinkDevice(deviceId: DeviceId) {
        matterDecommissioner.decommission(deviceId)
    }

    override suspend fun lockUnlockDoor(
        deviceId: DeviceId,
        isLocked: Boolean,
        endpoint: Int
    ) {
        matterDoorController.lockUnlockDoor(deviceId, isLocked, endpoint)
    }

    override suspend fun handleOutlet(
        deviceId: DeviceId,
        isSwitchOn: Boolean,
        endpoint: Int
    ) {
        matterOutletController.handleOutlet(deviceId, isSwitchOn, endpoint)
    }

    override suspend fun bind(
        sourceNodeId: DeviceId,
        sourceEndpoint: Int,
        targetNodeId: DeviceId,
        targetEndpoint: Int,
        clusterId: Long
    ) {
        matterBinder.bind(sourceNodeId, sourceEndpoint, targetNodeId, targetEndpoint, clusterId)
    }

    override fun subscribeToButtonChanges(
        deviceId: DeviceId,
        endpoint: Int
    ): Flow<Boolean> = callbackFlow {
        matterManufacturerCustomDataController.subscribeToButtonChanges(deviceId, endpoint) {
            trySend(it)
        }

        awaitClose {
            
        }
    }

    override fun subscribeToRandomNumber(
        deviceId: DeviceId,
        endpoint: Int
    ): Flow<UInt> = callbackFlow {
        matterClusterExtensionController.subscribeToRandomNumber(deviceId, endpoint) {
            trySend(it)
        }

        awaitClose {

        }
    }

    override suspend fun generateRandomNumber(deviceId: DeviceId): Int {
        return matterClusterExtensionController.generateRandomNumber(deviceId)
    }
}
