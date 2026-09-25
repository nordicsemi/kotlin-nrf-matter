package no.nordicsemi.nrf.matter.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

internal actual fun Modifier.matterPhoneWidth(): Modifier = this
    .widthIn(max = PHONE_WIDTH - PHONE_BORDER_WIDTH * 2)
    .padding(vertical = PHONE_MARGIN)

@Composable
internal actual fun matterPhoneSheetShape(): Shape = RoundedCornerShape(28.dp)
