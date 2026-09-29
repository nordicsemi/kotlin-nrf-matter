package no.nordicsemi.nrf.matter.docs

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeViewport
import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.di.uiModule
import no.nordicsemi.nrf.matter.docs.demo.InMemoryLoggerBackend
import no.nordicsemi.nrf.matter.docs.demo.WebMatterPlatform
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.nordic.registerNordicClusters
import no.nordicsemi.nrf.matter.platform.AppEnvironment
import no.nordicsemi.nrf.matter.platform.PlatformType
import no.nordicsemi.nrf.matter.ui.MatterOverlayHostState
import org.koin.core.context.startKoin
import org.koin.dsl.module

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    NordicLogger.setBackend(InMemoryLoggerBackend)
    NordicMatters.configure(WebMatterPlatform)
    registerNordicClusters()
    startKoin {
        modules(
            uiModule,
            module {
                single {
                    AppEnvironment(
                        platformType = PlatformType.IOS,
                        appVersion = "web-demo",
                        blur = { blur(16.dp) },
                        overlayHost = MatterOverlayHostState(),
                    )
                }
            },
        )
    }

    ComposeViewport {
        App()
    }
}
