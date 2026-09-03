package no.nordicsemi.nrf.matter.ui.lock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.nordicsemi.nrf.matter.domain.UiState
import no.nordicsemi.nrf.matter.model.LockDeviceState

@Composable
fun LockActionItem(
    lockState: UiState<LockDeviceState>,
    isLocked: Boolean,
    onLockUnlockDoor: (isLocked: Boolean) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color.LightGray.copy(alpha = 0.2f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    onLockUnlockDoor(!isLocked)
                }.alpha(
                    if (lockState is UiState.Success) 1f else 0f
                )
        ) {
            Text(
                text = if (isLocked) "Locked" else "Unlocked",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE11D48)
            )
        }

        CircularProgressIndicator(
            modifier = Modifier
                .size(24.dp)
                .alpha(if (lockState is UiState.Success) 0f else 1f)
        )
    }
}
