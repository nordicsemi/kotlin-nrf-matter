package no.nordicsemi.nrf.matter.binding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.domain.BindingState
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceBinding
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.repository.BindingRepository

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
internal class BindDevicesUseCase(
    private val deviceController: BindingController,
    private val bindingLogsProvider: BindingLogsProvider,
    private val bindingRepository: BindingRepository,
) {
    operator fun invoke(
        switchNodeId: DeviceId,
        lightNodeId: DeviceId,
    ): Flow<BindingState> = flow {
        emit(UiState.Loading())
        try {
            deviceController.bind(
                sourceNodeId = switchNodeId,
                sourceEndpoint = 1,
                targetNodeId = lightNodeId,
                targetEndpoint = 1,
                clusterId = ON_OFF_CLUSTER_ID,
            )
            val bindingDevice = DeviceBinding(
                id = "${switchNodeId.longValue}_${lightNodeId.longValue}",
                sourceNodeId = switchNodeId,
                targetNodeId = lightNodeId,
                sourceEndpoint = 1,
                targetEndpoint = 1,
                clusterId = ON_OFF_CLUSTER_ID
            )
            bindingRepository.save(bindingDevice)
            emit(UiState.Success(bindingDevice))
        } catch (e: Exception) {
            NordicLogger.error("Binding failed: ${e.message}", e)
            emit(UiState.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    val bindingLogs: Flow<String>
        get() = bindingLogsProvider.bindingLogs
}