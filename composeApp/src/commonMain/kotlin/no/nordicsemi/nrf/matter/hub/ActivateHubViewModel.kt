package no.nordicsemi.nrf.matter.hub

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.model.GoogleHub

class ActivateHubViewModel(
    private val hubController: GoogleHubController,
) : ViewModel() {

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )

    val hubs = flow { emit(hubController.getHubs()) }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    fun activateHub(hub: GoogleHub) {
        scope.launch {
            hubController.activateHub(hub)
        }
    }
}
