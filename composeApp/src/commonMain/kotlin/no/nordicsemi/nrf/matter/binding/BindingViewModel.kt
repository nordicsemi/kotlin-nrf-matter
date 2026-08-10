package no.nordicsemi.nrf.matter.binding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.domain.BindingState
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceBinding
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceType
import no.nordicsemi.nrf.matter.model.GroupBinding
import no.nordicsemi.nrf.matter.repository.BindingRepository
import no.nordicsemi.nrf.matter.repository.GroupBindingRepository
import no.nordicsemi.nrf.matter.repository.DevicesRepository
import no.nordicsemi.nrf.matter.ui.device.isBindingCapable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

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
data class BindingUiState(
    val bindingState: BindingState = UiState.Idle(),
    val groupBindingState: UiState<GroupBinding> = UiState.Idle(),
    val sourceDevices: List<Device> = emptyList(),
    val activeBindings: List<DeviceBinding> = emptyList(),
    val activeGroupBindings: List<GroupBinding> = emptyList(),
    val selectedSourceDeviceId: DeviceId? = null,
    val selectedTargetDeviceId: DeviceId? = null,
    val eligibleTargetDevices: List<Device> = emptyList(),
    val groupBindingSupported: Boolean = false,
)

class BindingViewModel(
    private val bindingRepository: BindingRepository,
    private val groupBindingRepository: GroupBindingRepository,
    private val devicesRepository: DevicesRepository,
    private val bindDevicesUseCase: BindDevicesUseCase,
    private val bindGroupDevicesUseCase: BindGroupDevicesUseCase? = null,
) : ViewModel() {

    private val _bindingUiState = MutableStateFlow(BindingUiState())
    val bindingUiState: StateFlow<BindingUiState> = _bindingUiState.asStateFlow()

    private val _bindingLogs = MutableStateFlow<PersistentList<String>>(persistentListOf())
    val bindingLogs = _bindingLogs.asStateFlow()

    init {
        loadSourceDevices()
        getActiveBindings()
        getActiveGroupBindings()
        updateGroupSupport()
    }

    fun updateBindingState(state: BindingState) =
        _bindingUiState.update {
            it.copy(bindingState = state)
        }

    fun updateGroupBindingState(state: UiState<GroupBinding>) =
        _bindingUiState.update {
            it.copy(groupBindingState = state)
        }

    fun loadSourceDevices() = viewModelScope.launch {
        val bindingSourceDevices = devicesRepository.getAllDevices().devicesList.filter {
            it.deviceType == DeviceType.LIGHT_SWITCH ||
                    it.deviceType == DeviceType.OUTLET
        }
        _bindingUiState.update {
            it.copy(sourceDevices = bindingSourceDevices)
        }
    }

    fun getActiveBindings() = viewModelScope.launch {
        bindingRepository.getAllBinding()
            .collect {
                _bindingUiState.update { state ->
                    state.copy(activeBindings = it)
                }
            }
    }

    private fun updateGroupSupport() {
        _bindingUiState.update {
            it.copy(groupBindingSupported = bindGroupDevicesUseCase != null)
        }
    }

    fun onSourceSelected(sourceDeviceId: DeviceId) {
        _bindingUiState.update {
            it.copy(selectedSourceDeviceId = sourceDeviceId)
        }

        updateEligibleTargetDevices(sourceDeviceId)
    }


    fun onTargetSelected(targetDeviceId: DeviceId) {
        _bindingUiState.update {
            it.copy(selectedTargetDeviceId = targetDeviceId)
        }
    }


    fun initiateBinding(sourceDeviceId: DeviceId, targetDeviceId: DeviceId) {
        val collectLogsJob = bindDevicesUseCase.bindingLogs
            .onStart { _bindingLogs.update { it.cleared() } }
            .onEach { log ->
                _bindingLogs.update { it.adding(log) }
            }.launchIn(viewModelScope)

        bindDevicesUseCase.invoke(
            switchNodeId = sourceDeviceId,
            lightNodeId = targetDeviceId
        )
            .onStart { updateBindingState(UiState.Loading()) }
            .onCompletion { collectLogsJob.cancel() }
            .delayIf(1.seconds) { it is UiState.Success } // Fake delay to display success log.
            .onEach { state ->
                if (state is UiState.Success) {
                    updateActiveBinding(state.data)
                    resetFormAndState()
                } else {
                    updateBindingState(state)
                }

            }
            .launchIn(viewModelScope)
    }

    fun initiateGroupBinding(sourceDeviceId: DeviceId, targetDeviceId: DeviceId) {
        val groupUseCase = bindGroupDevicesUseCase
        if (groupUseCase == null) {
            updateBindingState(UiState.Error("Group binding is not available on this platform."))
            return
        }

        val collectLogsJob = groupUseCase.bindingLogs
            .onStart { _bindingLogs.update { it.cleared() } }
            .onEach { log ->
                _bindingLogs.update { it.adding(log) }
            }.launchIn(viewModelScope)

        groupUseCase.invoke(
            switchNodeId = sourceDeviceId,
            lightNodeId = targetDeviceId
        )
            .onStart { updateGroupBindingState(UiState.Loading()) }
            .onCompletion { collectLogsJob.cancel() }
            .delayIf(1.seconds) { it is UiState.Success }
            .onEach { state ->
                if (state is UiState.Success) {
                    updateActiveGroupBinding(state.data)
                    resetFormAndState()
                } else {
                    updateGroupBindingState(state)
                }
            }
            .launchIn(viewModelScope)
    }

    // Resets selections and UI status back to initial state
    private fun resetFormAndState() {
        _bindingUiState.update {
            it.copy(
                bindingState = UiState.Idle(),
                groupBindingState = UiState.Idle(),
                selectedSourceDeviceId = null,
                selectedTargetDeviceId = null,
            )
        }
    }

    fun updateEligibleTargetDevices(sourceDeviceId: DeviceId) = viewModelScope.launch {
        bindingRepository.getTargetsForDevice(sourceDeviceId)
            .collect { bindings ->
                // Filter out devices that are lights and are not already bound to the selected source device.
                val lightDevicesInRepository =
                    devicesRepository.getAllDevices().devicesList.filter { it.isBindingCapable() }
                val targetIds = bindings.map { it.targetNodeId }.toSet()

                val result = lightDevicesInRepository.filterNot { it.deviceId in targetIds }

                _bindingUiState.update {
                    it.copy(eligibleTargetDevices = result)
                }
            }
    }

    fun getActiveGroupBindings() = viewModelScope.launch {
        groupBindingRepository.getAllBinding()
            .collect {
                _bindingUiState.update { state ->
                    state.copy(activeGroupBindings = it)
                }
            }
    }


    fun updateActiveBinding(binding: DeviceBinding) = viewModelScope.launch {
        val activeBindings = _bindingUiState.value.activeBindings.toMutableList()
        // Check if the binding already exists in the active bindings list. If it does, update it. If it doesn't, add it to the list.
        val index = activeBindings.indexOfFirst { it.id == binding.id }
        if (index != -1) {
            activeBindings[index] = binding
        } else {
            activeBindings.add(binding)
        }

        _bindingUiState.update {
            it.copy(activeBindings = activeBindings)
        }
    }

    fun updateActiveGroupBinding(binding: GroupBinding) = viewModelScope.launch {
        val activeBindings = _bindingUiState.value.activeGroupBindings.toMutableList()
        val index = activeBindings.indexOfFirst { it.id == binding.id }
        if (index != -1) {
            activeBindings[index] = binding
        } else {
            activeBindings.add(binding)
        }

        _bindingUiState.update {
            it.copy(activeGroupBindings = activeBindings)
        }
    }

    private fun <T> Flow<T>.delayIf(time: Duration, predicate: (T) -> Boolean): Flow<T> {
        return this.transform { value ->
            if (predicate(value)) {
                delay(time)
            }
            emit(value)
        }
    }
}
