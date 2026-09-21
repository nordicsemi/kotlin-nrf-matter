package no.nordicsemi.nrf.matter.docs.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import no.nordicsemi.nrf.matter.docs.docs.DocAnchor
import no.nordicsemi.nrf.matter.docs.docs.DocLinks
import no.nordicsemi.nrf.matter.webdemo.WebDemoEvents

/**
 * Observes the real `:lib` web fakes' [WebDemoEvents.lastAction] and reveals the matching doc
 * section. This is the whole "reveal docs instead of acting" seam: the real `:shared` screens
 * and controllers are unmodified and really do act -- this just also shows why.
 */
@Composable
fun ObserveWebDemoEvents(onReveal: (DocAnchor) -> Unit) {
    val action by WebDemoEvents.lastAction.collectAsState()
    LaunchedEffect(action) {
        action?.let { DocLinks.forWebDemoAction(it) }?.let(onReveal)
    }
}
