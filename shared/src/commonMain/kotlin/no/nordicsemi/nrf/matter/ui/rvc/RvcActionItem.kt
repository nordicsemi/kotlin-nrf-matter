package no.nordicsemi.nrf.matter.ui.rvc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import no.nordicsemi.nrf.matter.model.RvcOperationalState
import no.nordicsemi.nrf.matter.ui.UiState

@Composable
fun RvcActionItem(
    operationalState: UiState<RvcOperationalStateData>,
    onPlay: () -> Unit,
    onStop: () -> Unit,
) {
    val data = (operationalState as? UiState.Success)?.data
    val isRunning = data?.state == RvcOperationalState.RUNNING

    Box(contentAlignment = Alignment.Center) {
        IconButton(
            onClick = { if (isRunning) onStop() else onPlay() },
            modifier = Modifier.alpha(if (data != null) 1f else 0f),
        ) {
            Icon(
                imageVector = if (isRunning) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                contentDescription = if (isRunning) "Stop cleaning" else "Start cleaning",
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        CircularProgressIndicator(
            modifier = Modifier
                .size(24.dp)
                .alpha(if (data != null) 0f else 1f)
        )
    }
}
