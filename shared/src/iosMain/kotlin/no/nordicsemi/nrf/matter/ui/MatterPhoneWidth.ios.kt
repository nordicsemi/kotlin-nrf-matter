package no.nordicsemi.nrf.matter.ui

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

internal actual fun Modifier.matterPhoneWidth(): Modifier = this

@Composable
internal actual fun matterPhoneSheetShape(): Shape = BottomSheetDefaults.ExpandedShape
