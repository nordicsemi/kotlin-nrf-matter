package no.nordicsemi.nrf.matter.ui.windowcovering

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun WindowCoveringControlItem(
    state: WindowCoveringState,
    onOpen: () -> Unit,
    onClose: () -> Unit,
    onStop: () -> Unit,
    onLiftChange: (Int) -> Unit,
    onTiltChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Window covering",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onOpen, modifier = Modifier.weight(1f).testTag("window_covering_open")) {
                Text("Open")
            }
            OutlinedButton(onClick = onStop, modifier = Modifier.weight(1f).testTag("window_covering_stop")) {
                Text("Stop")
            }
            OutlinedButton(onClick = onClose, modifier = Modifier.weight(1f).testTag("window_covering_close")) {
                Text("Close")
            }
        }

        if (state.supportsLift) {
            PositionSlider("Lift", state.liftPercent, onLiftChange, "window_covering_lift")
        }
        if (state.supportsTilt) {
            PositionSlider("Tilt", state.tiltPercent, onTiltChange, "window_covering_tilt")
        }
    }
}

/** A labelled slider from 0 % (open) to 100 % (closed) that sends the position when the user lets go. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PositionSlider(label: String, percent: Int?, onChangeFinished: (Int) -> Unit, tag: String) {
    var dragging by remember { mutableStateOf<Float?>(null) }
    val shown = dragging ?: (percent ?: 0).toFloat()
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = if (percent == null && dragging == null) "—" else "${shown.toInt()}%",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Slider(
            value = shown,
            onValueChange = { dragging = it },
            onValueChangeFinished = {
                dragging?.let { onChangeFinished(it.toInt()) }
                dragging = null
            },
            valueRange = 0f..100f,
            interactionSource = interactionSource,
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = interactionSource,
                    thumbSize = DpSize(6.dp, 36.dp),
                )
            },
            track = { state ->
                SliderDefaults.Track(
                    sliderState = state,
                    modifier = Modifier.height(24.dp),
                    drawStopIndicator = null,
                    thumbTrackGapSize = 6.dp,
                )
            },
            modifier = Modifier.testTag(tag),
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Open", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Closed", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
