package no.nordicsemi.nrf.matter.docs

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import no.nordicsemi.nrf.matter.di.uiModule
import no.nordicsemi.nrf.matter.nordic.registerNordicClusters
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    registerNordicClusters()
    startKoin { modules(uiModule) }

    ComposeViewport {
        App()
    }
}
