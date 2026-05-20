package no.nordicsemi.nrf.matter.hub

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import no.nordicsemi.nrf.matter.model.GoogleHub
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ActivateHubScreen(onBack: () -> Unit) {
    val homeViewModel: ActivateHubViewModel = koinViewModel()

    val hubs by homeViewModel.hubs.collectAsStateWithLifecycle()

    Column {
        hubs.forEach {
            HubItem(it) {
                homeViewModel.activateHub(it)
            }
        }
    }
}

@Composable
private fun HubItem(hub: GoogleHub, onItemClick: () -> Unit) {
    Button(onItemClick) {
        Text(hub.name)
    }
}
