package no.nordicsemi.nrf.matter.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skydoves.cloudy.cloudy

@Composable
internal actual fun Modifier.matterBlur(): Modifier = this.cloudy()
