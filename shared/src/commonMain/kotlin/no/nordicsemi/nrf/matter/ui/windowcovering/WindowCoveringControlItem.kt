package no.nordicsemi.nrf.matter.ui.windowcovering

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import no.nordicsemi.nrf.matter.cluster.WindowCoveringClusterInfo.Feature
import kotlin.math.abs
import kotlin.math.ceil
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
        Header(state)

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ActionButton(
                "Open",
                Icons.Filled.KeyboardArrowUp,
                MaterialTheme.colorScheme.primary,
                onOpen,
                "window_covering_open",
                Modifier.weight(1f)
            )
            ActionButton(
                "Stop",
                Icons.Filled.Stop,
                MaterialTheme.colorScheme.error,
                onStop,
                "window_covering_stop",
                Modifier.weight(1f)
            )
            ActionButton(
                "Close",
                Icons.Filled.KeyboardArrowDown,
                MaterialTheme.colorScheme.primary,
                onClose,
                "window_covering_close",
                Modifier.weight(1f)
            )
        }

        if (state.isPositionUnknown) {
            PositionUnknownNotice()
        } else {
            BlindsPreview(
                lift = state.liftDestination,
                tilt = state.tiltDestination.takeIf { state.supportsTilt },
            )

            if (state.supportsLift) {
                PositionSlider(
                    "Lift",
                    state.liftPercent,
                    state.liftTargetPercent,
                    onLiftChange,
                    "window_covering_lift"
                )
            }
            if (state.supportsTilt) {
                PositionSlider(
                    "Tilt",
                    state.tiltPercent,
                    state.tiltTargetPercent,
                    onTiltChange,
                    "window_covering_tilt"
                )
            }
        }

        SupportedFeatures(state.featureMap)
    }
}

/** Shown in place of the blinds and sliders until the device reports where the covering is. */
@Composable
private fun PositionUnknownNotice() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(12.dp)
            .testTag("window_covering_unknown"),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column {
            Text("Position unknown", style = MaterialTheme.typography.labelLarge)
            Text(
                text = "The device hasn't reported its position yet. Use Open, Stop or Close to move it.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun Header(state: WindowCoveringState) {
    val headline = if (state.supportsLift) state.liftDestination else state.tiltDestination
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "Window Covering",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = headline?.let { "$it%" } ?: "—",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (headline == null) muted else MaterialTheme.colorScheme.primary,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "Matter Window Covering Cluster (0x0102)",
                style = MaterialTheme.typography.labelSmall,
                color = muted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = when (headline) {
                    null -> "Unknown"
                    0 -> "Open"
                    100 -> "Closed"
                    else -> "Partially open"
                },
                style = MaterialTheme.typography.labelSmall,
                color = muted,
            )
        }
    }
}

@Composable
private fun ActionButton(
    label: String,
    icon: ImageVector,
    accent: Color,
    onClick: () -> Unit,
    tag: String,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.4f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = accent.copy(alpha = 0.08f),
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier.testTag(tag),
    ) {
        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(4.dp))
        Text(label, fontWeight = FontWeight.SemiBold)
    }
}

private val MoveAnimation = tween<Float>(durationMillis = 1500, easing = FastOutSlowInEasing)

/** A window with slats that lower with the lift position and rotate with the tilt position. */
@Composable
private fun BlindsPreview(lift: Int?, tilt: Int?) {
    val liftFraction by animateFloatAsState(
        (lift ?: 0) / 100f,
        MoveAnimation,
        label = "blinds-lift"
    )
    val tiltFraction by animateFloatAsState(
        (tilt ?: 0) / 100f,
        MoveAnimation,
        label = "blinds-tilt"
    )
    val slat = MaterialTheme.colorScheme.outline
    val edge = MaterialTheme.colorScheme.primary
    val well = MaterialTheme.colorScheme.surfaceContainerLowest

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(well)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp)
                .clip(RoundedCornerShape(12.dp))
                .testTag("window_covering_preview"),
        ) {
            drawRect(well)
            val slatCount = 12
            val pitch = size.height / slatCount
            val covered = (liftFraction * slatCount).coerceIn(0f, slatCount.toFloat())
            // Tilt 0 = slats open (thin edge-on lines), 100 = closed (slats fill the pitch).
            val thickness = pitch * (0.15f + 0.85f * tiltFraction)
            val lowest = ceil(covered).toInt() - 1
            for (n in 0 until slatCount) {
                val reveal = (covered - n).coerceIn(0f, 1f)
                if (reveal > 0f) {
                    drawRoundRect(
                        color = if (n == lowest) edge.copy(alpha = 0.8f) else slat,
                        topLeft = Offset(0f, n * pitch),
                        size = Size(size.width, thickness * reveal),
                        cornerRadius = CornerRadius(thickness / 2),
                    )
                }
            }
        }
        Text(
            text = lift?.let { "Current position: $it% lift" } ?: "Current position unknown",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
private fun SupportedFeatures(featureMap: Int) {
    val features = listOfNotNull(
        "Lift".takeIf { featureMap and Feature.LIFT != 0 },
        "Tilt".takeIf { featureMap and Feature.TILT != 0 },
        "Position Aware Lift".takeIf { featureMap and Feature.POSITION_AWARE_LIFT != 0 },
        "Absolute Positioning".takeIf { featureMap and Feature.ABSOLUTE_POSITION != 0 },
        "Position Aware Tilt".takeIf { featureMap and Feature.POSITION_AWARE_TILT != 0 },
    )
    if (features.isEmpty()) return

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Text(
            text = "Supported features",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            features.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                )
            }
        }
    }
}

/**
 * Alpha that pulses while [enabled]. Stays at 1 otherwise, and in previews, where a never-ending
 * animation would keep the render from ever settling.
 */
@Composable
private fun rememberPulse(label: String, enabled: Boolean): Float {
    if (!enabled || LocalInspectionMode.current) return 1f
    val pulse by rememberInfiniteTransition(label = "$label-pulse").animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "$label-pulse-alpha",
    )
    return pulse
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

    val pulse = rememberPulse(label, enabled = isMoving)

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
                Text(
                    label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = when {
                        isMoving -> "Moving to ${destination.roundToInt()}%"
                        current != null && target != null && current != target -> "Target ${target}%"
                        else -> "Idle"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.alpha(pulse),
                )
            }
            Text(
                text = if (current == null && target == null && dragging == null) "—" else "${shown.roundToInt()}%",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
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
            colors = SliderDefaults.colors(
                activeTrackColor = MaterialTheme.colorScheme.primaryContainer,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                thumbColor = MaterialTheme.colorScheme.primary,
            ),
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = remember { MutableInteractionSource() },
                    colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.size(20.dp),
                )
            },
            modifier = Modifier.fillMaxWidth().testTag(tag),
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                "Open (0%)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Closed (100%)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
