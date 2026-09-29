package no.nordicsemi.nrf.matter.docs.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import no.nordicsemi.nrf.matter.docs.docs.DocAnchor
import no.nordicsemi.nrf.matter.docs.docs.DocLinks
import no.nordicsemi.nrf.matter.docs.demo.AppEvents

@Composable
fun ObserveAppEvents(onReveal: (DocAnchor) -> Unit) {
    LaunchedEffect(Unit) {
        AppEvents.events.collect { action ->
            DocLinks.forAppEvent(action)?.let(onReveal)
        }
    }
}
