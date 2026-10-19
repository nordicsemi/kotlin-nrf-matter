package no.nordicsemi.nrf.matter.ui.windowcovering

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt

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

        BlindsPreview(
            lift = state.liftDestination,
            tilt = state.tiltDestination.takeIf { state.supportsTilt },
        )

        if (state.supportsLift) {
            PositionSlider("Lift", state.liftPercent, state.liftTargetPercent, onLiftChange, "window_covering_lift")
        }
        if (state.supportsTilt) {
            PositionSlider("Tilt", state.tiltPercent, state.tiltTargetPercent, onTiltChange, "window_covering_tilt")
        }
    }
}

private val MoveAnimation = tween<Float>(durationMillis = 1500, easing = FastOutSlowInEasing)

/** A window with slats that lower with the lift position and rotate with the tilt position. */
@Composable
private fun BlindsPreview(lift: Int?, tilt: Int?) {
    val liftFraction by animateFloatAsState((lift ?: 0) / 100f, MoveAnimation, label = "blinds-lift")
    val tiltFraction by animateFloatAsState((tilt ?: 0) / 100f, MoveAnimation, label = "blinds-tilt")
    val frame = MaterialTheme.colorScheme.outline
    val sky = MaterialTheme.colorScheme.primaryContainer
    val slat = MaterialTheme.colorScheme.onSurfaceVariant

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .testTag("window_covering_preview"),
    ) {
        drawRect(sky)
        val slatCount = 12
        val pitch = size.height / slatCount
        val covered = (liftFraction * slatCount).coerceIn(0f, slatCount.toFloat())
        // Tilt 0 = slats open (thin edge-on lines), 100 = closed (slats fill the pitch).
        val thickness = pitch * (0.15f + 0.85f * tiltFraction)
        for (n in 0 until slatCount) {
            val reveal = (covered - n).coerceIn(0f, 1f)
            if (reveal > 0f) {
                drawRect(
                    color = slat,
                    topLeft = Offset(0f, n * pitch),
                    size = Size(size.width, thickness * reveal),
                )
            }
        }
        drawRect(frame, style = Stroke(width = 3.dp.toPx()))
    }
}

/**
 * A labelled slider from 0 % (open) to 100 % (closed) that sends the position when the user lets go.
 * The thumb glides to the target while the device is moving, with a "Moving" hint until it arrives.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PositionSlider(
    label: String,
    current: Int?,
    target: Int?,
    onChangeFinished: (Int) -> Unit,
    tag: String,
) {
    var dragging by remember { mutableStateOf<Float?>(null) }
    val destination = (target ?: current ?: 0).toFloat()
    val animated by animateFloatAsState(destination, MoveAnimation, label = "$label-position")
    val shown = dragging ?: animated
    val isMoving = dragging == null && abs(animated - destination) > 0.5f
    val interactionSource = remember { MutableInteractionSource() }

    val pulse by rememberInfiniteTransition(label = "$label-pulse").animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "$label-pulse-alpha",
    )

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
            Column {
                Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = when {
                        isMoving -> "Moving to ${destination.roundToInt()}%"
                        current != null && target != null && current != target -> "Target ${target}%"
                        else -> "Idle"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.alpha(if (isMoving) pulse else 1f),
                )
            }
            Text(
                text = if (current == null && target == null && dragging == null) "—" else "${shown.roundToInt()}%",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Slider(
            value = shown,
            onValueChange = { dragging = it },
            onValueChangeFinished = {
                dragging?.let { onChangeFinished(it.roundToInt()) }
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
