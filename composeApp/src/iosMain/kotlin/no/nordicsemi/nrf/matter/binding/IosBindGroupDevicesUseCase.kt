package no.nordicsemi.nrf.matter.binding

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.GroupBinding

class IosBindGroupDevicesUseCase(
    private val bindingLogsProvider: BindingLogsProvider,
) : BindGroupDevicesUseCase {
    override val isSupported: Boolean = false

    override val bindingLogs: Flow<String>
        get() = bindingLogsProvider.bindingLogs

    override fun invoke(
        switchNodeId: DeviceId,
        lightNodeId: DeviceId,
        groupId: Int?,
        groupName: String?,
    ): Flow<UiState<GroupBinding>> {
        // TODO: Replace this placeholder once iOS group binding support is implemented.
        return flowOf(UiState.Error("Group binding is not supported on iOS yet."))
    }
}
