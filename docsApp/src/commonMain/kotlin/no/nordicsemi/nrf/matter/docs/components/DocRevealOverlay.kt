package no.nordicsemi.nrf.matter.docs.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import no.nordicsemi.nrf.matter.docs.docs.DocAnchor
import no.nordicsemi.nrf.matter.docs.docs.DocLinks
import no.nordicsemi.nrf.matter.events.AppEvents

/**
 * Observes the real `:lib` web fakes' [AppEvents.events] and reveals the matching doc
 * section. This is the whole "reveal docs instead of acting" seam: the real `:shared` screens
 * and controllers are unmodified and really do act -- this just also shows why.
 */
@Composable
fun ObserveAppEvents(onReveal: (DocAnchor) -> Unit) {
    LaunchedEffect(Unit) {
        AppEvents.events.collect { action ->
            DocLinks.forAppEvent(action)?.let(onReveal)
        }
    }
}
