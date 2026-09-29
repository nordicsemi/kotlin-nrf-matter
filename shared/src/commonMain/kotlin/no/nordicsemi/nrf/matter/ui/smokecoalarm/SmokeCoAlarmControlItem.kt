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
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.nordicsemi.nrf.matter.model.AlarmState
import no.nordicsemi.nrf.matter.model.ExpressedState
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
        DeviceStatusSection(state)
        AlarmStatusRow("Smoke", state.smokeState)

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
                    modifier = Modifier.testTag("${label.replace(" ", "_")}_${option.name}"),
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

/** Current ExpressedState of the device, with a highlighted pill showing whether the alarm is muted. */
@Composable
private fun DeviceStatusSection(state: SmokeCoAlarmState) {
    val expressedState = state.expressedState
    val tint = expressedState.toColor()

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Device status",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.alpha(0.6f),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                imageVector = expressedState.toIcon(),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = expressedState.toLabel(),
                style = MaterialTheme.typography.titleMedium,
                color = tint,
                modifier = Modifier
                    .weight(1f)
                    .testTag("device_status"),
            )
            MuteStatusPill(isMuted = state.isMuted)
        }

        // A higher priority state (e.g. a smoke alarm) can hide a fault from the device status,
        // so surface it separately unless the status is already showing it.
        val fault = when {
            state.hasHardwareFault && expressedState != ExpressedState.HARDWARE_FAULT -> "Hardware fault"
            state.isEndOfService && expressedState != ExpressedState.END_OF_SERVICE -> "End of service"
            else -> null
        }
        fault?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun MuteStatusPill(isMuted: Boolean) {
    // A muted alarm won't sound during an emergency, so call it out in the warning color.
    val tint = if (isMuted) NordicFall else MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(tint.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = if (isMuted) "Muted" else "Sound on",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = tint,
            modifier = Modifier.testTag("mute_status"),
        )
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