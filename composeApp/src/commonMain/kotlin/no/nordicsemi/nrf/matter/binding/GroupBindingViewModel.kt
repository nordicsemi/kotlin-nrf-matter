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
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceType
import no.nordicsemi.nrf.matter.model.GroupBinding
import no.nordicsemi.nrf.matter.repository.DevicesRepository
import no.nordicsemi.nrf.matter.repository.GroupBindingRepository
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

data class GroupBindingUiState(
    val groupBindingState: UiState<GroupBinding> = UiState.Idle(),
    val sourceDevices: List<Device> = emptyList(),
    val activeGroupBindings: List<GroupBinding> = emptyList(),
    val selectedSourceDeviceId: DeviceId? = null,
    val selectedTargetDeviceId: DeviceId? = null,
    val eligibleTargetDevices: List<Device> = emptyList(),
    val groupBindingSupported: Boolean = false,
    val availableGroups: List<GroupInfo> = emptyList(),
    val selectedGroupId: Int? = null,
    val newGroupName: String? = null,
)

class GroupBindingViewModel(
    private val groupBindingRepository: GroupBindingRepository,
    private val devicesRepository: DevicesRepository,
    private val bindGroupDevicesUseCase: BindGroupDevicesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupBindingUiState())
    val uiState: StateFlow<GroupBindingUiState> = _uiState.asStateFlow()

    private val _bindingLogs = MutableStateFlow<PersistentList<String>>(persistentListOf())
    val bindingLogs = _bindingLogs.asStateFlow()

    init {
        loadSourceDevices()
        getActiveGroupBindings()
        updateGroupSupport()
    }

    fun updateGroupBindingState(state: UiState<GroupBinding>) =
        _uiState.update {
            it.copy(groupBindingState = state)
        }

    fun loadSourceDevices() = viewModelScope.launch {
        val bindingSourceDevices = devicesRepository.getAllDevices().devicesList.filter {
            it.deviceType == DeviceType.LIGHT_SWITCH ||
                    it.deviceType == DeviceType.OUTLET
        }
        _uiState.update {
            it.copy(sourceDevices = bindingSourceDevices)
        }
    }

    private fun updateGroupSupport() {
        _uiState.update {
            it.copy(groupBindingSupported = bindGroupDevicesUseCase.isSupported)
        }
    }

    fun onSourceSelected(sourceDeviceId: DeviceId) {
        _uiState.update {
            it.copy(selectedSourceDeviceId = sourceDeviceId)
        }

        updateEligibleTargetDevices(sourceDeviceId)
    }

    fun onTargetSelected(targetDeviceId: DeviceId) {
        _uiState.update {
            it.copy(selectedTargetDeviceId = targetDeviceId)
        }
    }

    fun onGroupSelected(groupId: Int?) {
        _uiState.update {
            it.copy(
                selectedGroupId = groupId,
                newGroupName = null
            )
        }
    }

    fun onGroupNameSet(groupName: String?) {
        _uiState.update {
            it.copy(
                newGroupName = groupName,
                selectedGroupId = null
            )
        }
    }

    fun initiateGroupBinding(
        sourceDeviceId: DeviceId,
        targetDeviceId: DeviceId,
        groupId: Int? = null,
        groupName: String? = null,
    ) {
        if (!bindGroupDevicesUseCase.isSupported) {
            updateGroupBindingState(UiState.Error("Group binding is not available on this platform."))
            return
        }

        val collectLogsJob = bindGroupDevicesUseCase.bindingLogs
            .onStart { _bindingLogs.update { it.cleared() } }
            .onEach { log ->
                _bindingLogs.update { it.adding(log) }
            }.launchIn(viewModelScope)

        bindGroupDevicesUseCase.invoke(
            switchNodeId = sourceDeviceId,
            lightNodeId = targetDeviceId,
            groupId = groupId,
            groupName = groupName,
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

    private fun resetFormAndState() {
        _uiState.update {
            it.copy(
                groupBindingState = UiState.Idle(),
                selectedSourceDeviceId = null,
                selectedTargetDeviceId = null,
                selectedGroupId = null,
                newGroupName = null,
            )
        }
    }

    fun updateEligibleTargetDevices(sourceDeviceId: DeviceId) = viewModelScope.launch {
        // Group binding doesn't necessarily have the same restrictions as unicast binding in terms of "already bound" 
        // because we might want to add multiple lights to the same group.
        // However, for simplicity and consistency with the current implementation:
        val lightDevicesInRepository =
            devicesRepository.getAllDevices().devicesList.filter {
                it.deviceType == DeviceType.LIGHT_ON_OFF ||
                        it.deviceType == DeviceType.DIMMABLE_LIGHT
            }
        
        _uiState.update {
            it.copy(eligibleTargetDevices = lightDevicesInRepository)
        }
    }

    fun getActiveGroupBindings() = viewModelScope.launch {
        groupBindingRepository.getAllBinding()
            .collect { bindings ->
                val groups = bindings
                    .distinctBy { it.groupId }
                    .map { GroupInfo(it.groupId, it.groupName) }
                _uiState.update { state ->
                    state.copy(
                        activeGroupBindings = bindings,
                        availableGroups = groups
                    )
                }
            }
    }

    fun updateActiveGroupBinding(binding: GroupBinding) = viewModelScope.launch {
        val activeBindings = _uiState.value.activeGroupBindings.toMutableList()
        val index = activeBindings.indexOfFirst { it.id == binding.id }
        if (index != -1) {
            activeBindings[index] = binding
        } else {
            activeBindings.add(binding)
        }

        _uiState.update {
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
