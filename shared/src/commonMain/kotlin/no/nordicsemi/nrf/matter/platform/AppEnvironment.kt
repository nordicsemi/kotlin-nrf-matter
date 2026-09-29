package no.nordicsemi.nrf.matter.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import no.nordicsemi.nrf.matter.ui.MatterOverlayHostState

class AppEnvironment(
    val platformType: PlatformType,
    val appVersion: String,
    val blur: @Composable Modifier.() -> Modifier,
    val overlayHost: MatterOverlayHostState? = null,
)
