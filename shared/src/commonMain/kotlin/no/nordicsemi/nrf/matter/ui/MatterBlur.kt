package no.nordicsemi.nrf.matter.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import no.nordicsemi.nrf.matter.platform.LocalAppEnvironment

@Composable
internal fun Modifier.matterBlur(): Modifier = LocalAppEnvironment.current.blur(this)
