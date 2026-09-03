package no.nordicsemi.nrf.matter.api

internal actual object MatterDependenciesProvider {
    actual fun createMatterDependencies(): MatterDependencies {
        return MatterDependencies(MatterPlatformDependencies())
    }
}
