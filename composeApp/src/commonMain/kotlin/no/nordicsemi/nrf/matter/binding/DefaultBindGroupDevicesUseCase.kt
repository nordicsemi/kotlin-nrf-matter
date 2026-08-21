package no.nordicsemi.nrf.matter.binding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.GroupBindingController
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.GroupBinding
import no.nordicsemi.nrf.matter.model.MatterGroup
import no.nordicsemi.nrf.matter.repository.GroupBindingRepository
import no.nordicsemi.nrf.matter.repository.MatterGroupRepository

class DefaultBindGroupDevicesUseCase(
    private val groupBindingController: GroupBindingController,
    private val bindingLogsProvider: BindingLogsProvider,
    private val groupBindingRepository: GroupBindingRepository,
    private val matterGroupRepository: MatterGroupRepository,
) : BindGroupDevicesUseCase {
    override val isSupported: Boolean = true

    override operator fun invoke(
        switchNodeId: DeviceId,
        lightNodeId: DeviceId,
        groupId: Int?,
        groupName: String?,
    ): Flow<UiState<GroupBinding>> = flow {
        emit(UiState.Loading())
        try {
            val existingBinding = groupBindingRepository.getAllBinding()
                .first()
                .firstOrNull {
                    it.sourceNodeId == switchNodeId &&
                            it.targetNodeId == lightNodeId &&
                            it.clusterId == 0x006L &&
                            (groupId == null || it.groupId == groupId)
                }

            if (existingBinding != null) {
                emit(UiState.Success(existingBinding))
                return@flow
            }

            val binding = groupBindingController.bind(
                sourceNodeId = switchNodeId,
                sourceEndpoint = 1,
                targetNodeId = lightNodeId,
                targetEndpoint = 1,
                clusterId = 0x006L,
                groupId = groupId,
                groupName = groupName,
            )
            groupBindingRepository.save(binding)
            matterGroupRepository.save(MatterGroup(binding.groupId, binding.groupName))
            emit(UiState.Success(binding))
        } catch (e: Exception) {
            NordicLogger.error("Group binding failed: ${e.message}", e)
            emit(UiState.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    override val bindingLogs: Flow<String>
        get() = bindingLogsProvider.bindingLogs
}
