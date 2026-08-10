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
import no.nordicsemi.nrf.matter.repository.GroupBindingRepository

class BindGroupDevicesUseCase(
    private val groupBindingController: GroupBindingController,
    private val bindingLogsProvider: BindingLogsProvider,
    private val groupBindingRepository: GroupBindingRepository,
) {
    operator fun invoke(
        switchNodeId: DeviceId,
        lightNodeId: DeviceId,
    ): Flow<UiState<GroupBinding>> = flow {
        emit(UiState.Loading())
        try {
            val existingBinding = groupBindingRepository.getAllBinding()
                .first()
                .firstOrNull {
                    it.sourceNodeId == switchNodeId &&
                            it.targetNodeId == lightNodeId &&
                            it.clusterId == 0x006L
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
            )
            groupBindingRepository.save(binding)
            emit(UiState.Success(binding))
        } catch (e: Exception) {
            NordicLogger.error("Group binding failed: ${e.message}", e)
            emit(UiState.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    val bindingLogs: Flow<String>
        get() = bindingLogsProvider.bindingLogs
}
