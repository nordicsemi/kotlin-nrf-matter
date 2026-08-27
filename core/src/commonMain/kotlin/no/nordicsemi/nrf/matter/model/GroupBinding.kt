package no.nordicsemi.nrf.matter.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupBinding(
    val id: String,
    val sourceNodeId: DeviceId,
    val sourceEndpoint: Int,
    val targetNodeId: DeviceId,
    val targetEndpoint: Int,
    val clusterId: Long,
    val groupId: Int,
    val groupName: String,
    val keySetId: Int,
    val fabricIndex: Int? = null,
)
