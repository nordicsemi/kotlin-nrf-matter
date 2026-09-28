package no.nordicsemi.nrf.matter.docs.docs

import no.nordicsemi.nrf.matter.cluster.BasicInfoClusterInfo
import no.nordicsemi.nrf.matter.cluster.DoorLockClusterInfo
import no.nordicsemi.nrf.matter.cluster.LevelControlClusterInfo
import no.nordicsemi.nrf.matter.cluster.OnOffClusterInfo
import no.nordicsemi.nrf.matter.webdemo.WebDemoAction

/**
 * Hand-authored wiring between a specific UI element in the demo shell and the doc section
 * that explains it, keyed against the real heading text in the doc/docs markdown files.
 */
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

    /** `no.nordicsemi.nrf.matter.nordic.ManufacturerSpecClusterInfo.ID` in `:shared`. */
    private const val MANUFACTURER_SPEC_CLUSTER_ID = 0xFFF1FC01L

    private fun forClusterId(clusterId: Long): DocAnchor? = when (clusterId) {
        OnOffClusterInfo.ID, LevelControlClusterInfo.ID -> LightBulbControls
        DoorLockClusterInfo.ID -> DoorLockControls
        MANUFACTURER_SPEC_CLUSTER_ID, BasicInfoClusterInfo.ID -> ManufacturerSpecificControls
        else -> null
    }

    private fun forNamedInteraction(key: String): DocAnchor? = when (key) {
        "device-card-expand" -> SupportedDeviceTypes
        "matter-device-information" -> MatterDeviceInformation
        "what-is-matter" -> AppIntro
        "source-code" -> SourceCode
        "nav-tab-Dashboard" -> Dashboard
        "nav-tab-Bindings" -> BindingsIntro
        "nav-tab-Logs Panel" -> LogsIntro
        else -> null
    }

    /**
     * Maps a live [WebDemoAction] published by the real `:lib`/`:shared` web fakes to the doc
     * section that explains it -- the seam that lets interacting with the *real* app shell reveal
     * documentation instead of just doing the real thing.
     */
    fun forWebDemoAction(action: WebDemoAction): DocAnchor? = when (action) {
        is WebDemoAction.ClusterCommandExecuted -> forClusterId(action.clusterId)
        is WebDemoAction.ClusterAttributeObserved -> forClusterId(action.clusterId)
        is WebDemoAction.NamedInteraction -> forNamedInteraction(action.key)
        WebDemoAction.BindingStarted -> WritingABinding
        is WebDemoAction.BindingCompleted -> ActiveBindingTableEntries
        is WebDemoAction.DeviceDecommissioned -> RemovingADevice
        WebDemoAction.CommissioningStarted -> OnboardingWalkthrough
        is WebDemoAction.CommissioningSucceeded -> DeviceCards
        WebDemoAction.CommissioningFailed -> IfCommissioningFails
    }
}
