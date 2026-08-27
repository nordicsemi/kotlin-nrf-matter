package no.nordicsemi.nrf.matter.model

import kotlinx.serialization.Serializable

@Serializable
data class MatterGroup(
    val groupId: Int,
    val groupName: String,
)
