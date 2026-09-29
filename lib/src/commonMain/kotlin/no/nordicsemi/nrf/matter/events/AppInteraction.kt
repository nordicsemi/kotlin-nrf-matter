package no.nordicsemi.nrf.matter.events

sealed class AppInteraction {
    data object AddNewDevice : AppInteraction()
    data object WhatIsMatter : AppInteraction()
    data object SourceCode : AppInteraction()
    data class NavTabSelected(val tabTitle: String) : AppInteraction()
    data object DeviceCardExpand : AppInteraction()
    data object MatterDeviceInformation : AppInteraction()
    data object EndpointsClusters : AppInteraction()
}
