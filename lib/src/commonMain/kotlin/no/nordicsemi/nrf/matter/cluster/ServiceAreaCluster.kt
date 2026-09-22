package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import no.nordicsemi.nrf.matter.model.DeviceId

data class ServiceArea(val areaId: Int, val name: String?)

object ServiceAreaClusterInfo {
    const val ID: Long = 0x0150

    object Attribute {
        const val SUPPORTED_AREAS: Long = 0x0000
        const val SELECTED_AREAS: Long = 0x0002
        const val CURRENT_AREA: Long = 0x0003
    }

    object Command {
        const val SELECT_AREAS: Long = 0x00
        const val SKIP_AREA: Long = 0x01
    }

    object AreaStruct {
        const val AREA_ID: Long = 0
        const val AREA_INFO: Long = 2
    }

    object AreaInfoStruct {
        const val LOCATION_INFO: Long = 0
    }

    object LocationDescriptorStruct {
        const val LOCATION_NAME: Long = 0
    }
}

class ServiceAreaCluster(
    override val deviceId: DeviceId,
    override val endpoint: Int,
    controller: MatterClient,
) : Cluster(controller) {

    override val id: Long = ServiceAreaClusterInfo.ID

    suspend fun supportedAreas(): List<ServiceArea> =
        readAttribute<List<*>?>(ServiceAreaClusterInfo.Attribute.SUPPORTED_AREAS)
            .orEmpty()
            .filterIsInstance<MatterStruct>()
            .mapNotNull { struct ->
                val areaId = struct.longOrNull(ServiceAreaClusterInfo.AreaStruct.AREA_ID)?.toInt()
                    ?: return@mapNotNull null
                val name = (struct[ServiceAreaClusterInfo.AreaStruct.AREA_INFO] as? MatterStruct)
                    ?.let { it[ServiceAreaClusterInfo.AreaInfoStruct.LOCATION_INFO] as? MatterStruct }
                    ?.let { it[ServiceAreaClusterInfo.LocationDescriptorStruct.LOCATION_NAME] as? String }
                ServiceArea(areaId, name)
            }

    fun observeSelectedAreas(): Flow<List<Number>> =
        observeAttribute(ServiceAreaClusterInfo.Attribute.SELECTED_AREAS)

    fun observeCurrentArea(): Flow<Number?> =
        observeAttribute(ServiceAreaClusterInfo.Attribute.CURRENT_AREA)

    suspend fun selectAreas(areaIds: List<Int>) {
        executeCommand(
            commandId = ServiceAreaClusterInfo.Command.SELECT_AREAS,
            value = areaIds.map { it.toUInt() },
        )
    }

    suspend fun skipArea(areaId: Int) {
        executeCommand(commandId = ServiceAreaClusterInfo.Command.SKIP_AREA, value = areaId.toUInt())
    }
}
