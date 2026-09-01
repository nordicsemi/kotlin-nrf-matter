package no.nordicsemi.nrf.matter.model

import kotlinx.serialization.Serializable

@Serializable
data class ManufacturerSpecificData (
    val name: String,
    val led: Boolean,
    val button: Boolean,
)
