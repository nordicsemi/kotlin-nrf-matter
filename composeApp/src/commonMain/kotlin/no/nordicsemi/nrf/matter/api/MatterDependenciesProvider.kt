package no.nordicsemi.nrf.matter.api

internal expect object MatterDependenciesProvider {

    fun createMatterDependencies(): MatterDependencies
}