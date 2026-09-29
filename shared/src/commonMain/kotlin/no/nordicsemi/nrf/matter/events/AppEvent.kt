package no.nordicsemi.nrf.matter.events

import no.nordicsemi.nrf.matter.model.DeviceId

sealed class AppEvent {
    data class ClusterAttributeObserved(
        val deviceId: DeviceId,
        val endpoint: Int,
        val clusterId: Long,
        val attributeId: Long,
    ) : AppEvent()

    data class ClusterCommandExecuted(
        val deviceId: DeviceId,
        val endpoint: Int,
        val clusterId: Long,
        val commandId: Long,
    ) : AppEvent()

    data object BindingStarted : AppEvent()
    data class BindingCompleted(val sourceNodeId: DeviceId, val targetNodeId: DeviceId) : AppEvent()
    data class DeviceDecommissioned(val deviceId: DeviceId) : AppEvent()
    data object CommissioningStarted : AppEvent()
    data class CommissioningSucceeded(val deviceId: DeviceId) : AppEvent()
    data object CommissioningFailed : AppEvent()

    data object AddNewDevice : AppEvent()
    data object WhatIsMatter : AppEvent()
    data object SourceCode : AppEvent()
    data class NavTabSelected(val tabTitle: String) : AppEvent()
    data object DeviceCardExpand : AppEvent()
    data object MatterDeviceInformation : AppEvent()
    data object EndpointsClusters : AppEvent()
}
