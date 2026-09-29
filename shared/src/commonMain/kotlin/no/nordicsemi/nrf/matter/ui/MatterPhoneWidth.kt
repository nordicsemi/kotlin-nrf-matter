package no.nordicsemi.nrf.matter.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

val PHONE_WIDTH = 412.dp
val PHONE_BORDER_WIDTH = 8.dp
val PHONE_MARGIN = 16.dp

internal expect fun Modifier.matterPhoneWidth(): Modifier

@Composable
internal expect fun matterPhoneSheetShape(): Shape
