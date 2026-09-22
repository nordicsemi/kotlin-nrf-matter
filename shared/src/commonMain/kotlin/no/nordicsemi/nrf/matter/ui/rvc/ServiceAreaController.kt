package no.nordicsemi.nrf.matter.ui.rvc

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.cluster.ServiceArea
import no.nordicsemi.nrf.matter.cluster.ServiceAreaCluster
import no.nordicsemi.nrf.matter.ui.UiState
import no.nordicsemi.nrf.matter.ui.device.ClusterController

data class ServiceAreaData(
    val supportedAreas: List<ServiceArea>,
    val selectedAreaIds: List<Int>,
    val currentAreaId: Int?,
)

class ServiceAreaController(
    private val cluster: ServiceAreaCluster,
    scope: CoroutineScope,
) : ClusterController(scope) {

    private val _state = MutableStateFlow<UiState<ServiceAreaData>>(UiState.Loading())
    val state = _state.asStateFlow()

    private val _supportedAreas = MutableStateFlow<List<ServiceArea>>(emptyList())

    // CurrentArea is an optional attribute: a device that does not implement it never reports
    // it, so it must not gate the combined state below on its own flow.
    private val _currentAreaId = MutableStateFlow<Int?>(null)

    init {
        execute { cluster.supportedAreas() }
            .catch { emit(emptyList()) }
            .onEach { areas -> _supportedAreas.update { areas } }
            .launchIn(scope)

        cluster.observeCurrentArea()
            .onEach { _currentAreaId.update { _ -> it?.toInt() } }
            .launchIn(scope)

        combine(
            _supportedAreas,
            cluster.observeSelectedAreas(),
            _currentAreaId,
        ) { supportedAreas, selectedAreas, currentAreaId ->
            ServiceAreaData(
                supportedAreas = supportedAreas,
                selectedAreaIds = selectedAreas.map { it.toInt() },
                currentAreaId = currentAreaId,
            )
        }
            .onEach { data -> _state.update { UiState.Success(data) } }
            .launchIn(scope)
    }

    fun selectAreas(areaIds: List<Int>) {
        execute { cluster.selectAreas(areaIds) }
            .catch { it.printStackTrace() }
            .launchIn(scope)
    }

    fun skipArea(areaId: Int) {
        execute { cluster.skipArea(areaId) }
            .catch { it.printStackTrace() }
            .launchIn(scope)
    }
}
