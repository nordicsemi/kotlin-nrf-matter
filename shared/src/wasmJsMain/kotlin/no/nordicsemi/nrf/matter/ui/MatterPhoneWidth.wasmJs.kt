package no.nordicsemi.nrf.matter.ui

import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

internal actual fun Modifier.matterPhoneWidth(): Modifier = this.widthIn(max = 420.dp)
