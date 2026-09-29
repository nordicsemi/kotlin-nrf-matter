package no.nordicsemi.nrf.matter.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import no.nordicsemi.nrf.matter.platform.AppEnvironment
import org.koin.compose.koinInject

@Composable
internal fun Modifier.matterBlur(): Modifier = koinInject<AppEnvironment>().blur(this)
