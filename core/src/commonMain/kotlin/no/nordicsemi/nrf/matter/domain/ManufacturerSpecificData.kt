package no.nordicsemi.nrf.matter.domain

import kotlinx.serialization.Serializable

@Serializable
data class ManufacturerSpecificData (
    val name: String,
    val led: Boolean,
    val button: Boolean,
)
