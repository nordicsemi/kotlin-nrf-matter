package no.nordicsemi.nrf.matter.ui.windowcovering

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/** Header action: an expand arrow. The controls are shown in the expanded window covering card. */
@Composable
fun WindowCoveringActionItem() {
    Icon(
        imageVector = Icons.Outlined.ExpandMore,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
