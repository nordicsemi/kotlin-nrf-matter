package no.nordicsemi.nrf.matter.docs.docs

import no.nordicsemi.nrf.matter.cluster.BasicInfoClusterInfo
import no.nordicsemi.nrf.matter.cluster.DoorLockClusterInfo
import no.nordicsemi.nrf.matter.cluster.LevelControlClusterInfo
import no.nordicsemi.nrf.matter.cluster.OnOffClusterInfo
import no.nordicsemi.nrf.matter.events.AppEvent
import no.nordicsemi.nrf.matter.events.AppInteraction

object DocLinks {
    val AppIntro = DocAnchor(DocPage.INDEX)
    val SourceCode = DocAnchor(DocPage.INDEX, "application-source-code")

    val CommonInterface = DocAnchor(DocPage.OVERVIEW, "common-interface")
    val Dashboard = DocAnchor(DocPage.OVERVIEW, "dashboard")
    val GettingStartedScreen = DocAnchor(DocPage.OVERVIEW, "getting-started-screen")
    val DeviceCards = DocAnchor(DocPage.OVERVIEW, "dashboard-device-cards")
    val SupportedDeviceTypes = DocAnchor(DocPage.OVERVIEW, "supported-device-types")
    val LightBulbControls = DocAnchor(DocPage.OVERVIEW, "light-bulb-controls")
    val DoorLockControls = DocAnchor(DocPage.OVERVIEW, "door-lock-controls")
    val LightSwitches = DocAnchor(DocPage.OVERVIEW, "light-switches")
    val ManufacturerSpecificControls = DocAnchor(DocPage.OVERVIEW, "manufacturer-specific-device-controls")
    val UnsupportedDeviceTypes = DocAnchor(DocPage.OVERVIEW, "unsupported-device-types")
    val MatterDeviceInformation = DocAnchor(DocPage.OVERVIEW, "matter-device-information")
    val EndpointsClusters = DocAnchor(DocPage.ENDPOINTS_CLUSTERS)
    val RemovingADevice = DocAnchor(DocPage.OVERVIEW, "removing-a-device")

    val BindingsIntro = DocAnchor(DocPage.BINDINGS)
    val WritingABinding = DocAnchor(DocPage.BINDINGS, "writing-a-binding")
    val WhatWritingABindingDoes = DocAnchor(DocPage.BINDINGS, "what-writing-a-binding-does")
    val ActiveBindingTableEntries = DocAnchor(DocPage.BINDINGS, "active-binding-table-entries")

    val OnboardingWalkthrough = DocAnchor(DocPage.ONBOARDING)

    val CommissioningIntro = DocAnchor(DocPage.COMMISSIONING)
    val CommissioningOnAndroid = DocAnchor(DocPage.COMMISSIONING, "commissioning-on-android")
    val CommissioningOnIos = DocAnchor(DocPage.COMMISSIONING, "commissioning-on-ios")
    val IfCommissioningFails = DocAnchor(DocPage.COMMISSIONING, "if-commissioning-fails")

    val LogsIntro = DocAnchor(DocPage.LOGS)
    val LogsUserInterface = DocAnchor(DocPage.LOGS, "user-interface")
    val ExportingLogs = DocAnchor(DocPage.LOGS, "exporting-logs")

    val PreparingDevice = DocAnchor(DocPage.PREPARING_DEVICE)
    val ThreadCredentials = DocAnchor(DocPage.THREAD_CREDENTIALS)
    val Requirements = DocAnchor(DocPage.REQUIREMENTS)
    val ReleaseNotes = DocAnchor(DocPage.RELEASE_NOTES)
    val RevisionHistory = DocAnchor(DocPage.REVISION_HISTORY)
    val Building = DocAnchor(DocPage.BUILDING)
    val ProjectStructure = DocAnchor(DocPage.PROJECT_STRUCTURE)
    val VendoredDeps = DocAnchor(DocPage.VENDORED_DEPS)

    private const val MANUFACTURER_SPEC_CLUSTER_ID = 0xFFF1FC01L

    private fun forClusterId(clusterId: Long): DocAnchor? = when (clusterId) {
        OnOffClusterInfo.ID, LevelControlClusterInfo.ID -> LightBulbControls
        DoorLockClusterInfo.ID -> DoorLockControls
        MANUFACTURER_SPEC_CLUSTER_ID, BasicInfoClusterInfo.ID -> ManufacturerSpecificControls
        else -> null
    }

    private fun forNamedInteraction(interaction: AppInteraction): DocAnchor? = when (interaction) {
        AppInteraction.AddNewDevice -> GettingStartedScreen
        AppInteraction.DeviceCardExpand -> SupportedDeviceTypes
        AppInteraction.MatterDeviceInformation -> MatterDeviceInformation
        AppInteraction.EndpointsClusters -> EndpointsClusters
        AppInteraction.WhatIsMatter -> AppIntro
        AppInteraction.SourceCode -> SourceCode
        is AppInteraction.NavTabSelected -> when (interaction.tabTitle) {
            "Dashboard" -> Dashboard
            "Bindings" -> BindingsIntro
            "Logs Panel" -> LogsIntro
            else -> null
        }
    }

    fun forAppEvent(action: AppEvent): DocAnchor? = when (action) {
        is AppEvent.ClusterCommandExecuted -> forClusterId(action.clusterId)
        is AppEvent.ClusterAttributeObserved -> forClusterId(action.clusterId)
        is AppEvent.NamedInteraction -> forNamedInteraction(action.interaction)
        AppEvent.BindingStarted -> WritingABinding
        is AppEvent.BindingCompleted -> ActiveBindingTableEntries
        is AppEvent.DeviceDecommissioned -> RemovingADevice
        AppEvent.CommissioningStarted -> OnboardingWalkthrough
        is AppEvent.CommissioningSucceeded -> DeviceCards
        AppEvent.CommissioningFailed -> IfCommissioningFails
    }
}
