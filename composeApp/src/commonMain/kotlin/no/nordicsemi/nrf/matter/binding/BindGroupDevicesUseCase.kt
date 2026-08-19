package no.nordicsemi.nrf.matter.binding

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.GroupBinding

interface BindGroupDevicesUseCase {
    val isSupported: Boolean
    val bindingLogs: Flow<String>

    operator fun invoke(
        switchNodeId: DeviceId,
        lightNodeId: DeviceId,
        groupId: Int? = null,
        groupName: String? = null,
    ): Flow<UiState<GroupBinding>>
}
