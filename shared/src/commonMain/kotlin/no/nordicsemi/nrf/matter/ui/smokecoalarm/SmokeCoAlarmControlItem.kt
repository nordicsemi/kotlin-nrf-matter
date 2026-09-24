package no.nordicsemi.nrf.matter.ui.smokecoalarm

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.nordicsemi.nrf.matter.model.AlarmState
import no.nordicsemi.nrf.matter.theme.NordicFall
import no.nordicsemi.nrf.matter.theme.NordicRed
import no.nordicsemi.nrf.matter.ui.UiState

@Composable
fun SmokeCoAlarmControlItem(
    state: SmokeCoAlarmState,
    selfTestState: UiState<Unit>,
    onRunSelfTest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AlarmStatusRow("Smoke", state.smokeState)
        AlarmStatusRow("Carbon monoxide", state.coState)
        AlarmStatusRow("Battery", state.batteryAlert)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (state.isMuted) "Alarm muted" else "Alarm not muted",
                style = MaterialTheme.typography.labelMedium,
            )
            if (state.hasHardwareFault || state.isEndOfService) {
                Text(
                    text = if (state.hasHardwareFault) "Hardware fault" else "End of service",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = onRunSelfTest,
                enabled = !state.isTestInProgress,
            ) {
                Text(if (state.isTestInProgress) "Testing..." else "Run self-test")
            }

            if (state.isTestInProgress || selfTestState is UiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            }

            if (selfTestState is UiState.Error) {
                Icon(
                    imageVector = Icons.Outlined.Error,
                    contentDescription = "Self-test failed",
                    tint = NordicRed,
                )
            }
        }
    }
}

// Blink period: warning pulses at a calm pace, critical pulses noticeably faster to read as more urgent.
private const val WARNING_BLINK_MILLIS = 900
private const val CRITICAL_BLINK_MILLIS = 350

@Composable
private fun AlarmStatusRow(label: String, state: AlarmState) {
    val blinkAlpha = when (state) {
        AlarmState.NORMAL -> 1f
        AlarmState.WARNING -> rememberBlinkAlpha(periodMillis = WARNING_BLINK_MILLIS)
        AlarmState.CRITICAL -> rememberBlinkAlpha(periodMillis = CRITICAL_BLINK_MILLIS)
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.alpha(0.6f),
        )

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            AlarmState.entries.forEachIndexed { index, option ->
                val selected = option == state
                val activeColor = when (option) {
                    AlarmState.NORMAL -> MaterialTheme.colorScheme.primary
                    AlarmState.WARNING -> NordicFall
                    AlarmState.CRITICAL -> NordicRed
                }
                SegmentedButton(
                    selected = selected,
                    onClick = {},
                    shape = SegmentedButtonDefaults.itemShape(index, AlarmState.entries.size),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = activeColor.copy(alpha = 0.12f * blinkAlpha),
                        activeContentColor = activeColor.copy(alpha = blinkAlpha),
                    ),
                    label = {
                        Text(
                            text = when (option) {
                                AlarmState.NORMAL -> "Normal"
                                AlarmState.WARNING -> "Warning"
                                AlarmState.CRITICAL -> "Critical"
                            },
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun rememberBlinkAlpha(periodMillis: Int): Float {
    val infiniteTransition = rememberInfiniteTransition(label = "alarm-blink")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = periodMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "alarm-blink-alpha",
    )
    return alpha
}