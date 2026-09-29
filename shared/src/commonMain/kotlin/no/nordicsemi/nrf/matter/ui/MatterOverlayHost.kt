package no.nordicsemi.nrf.matter.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MatterOverlayHostState {
    var sheet by mutableStateOf<(@Composable () -> Unit)?>(null)
        private set
    var onSheetDismissRequest by mutableStateOf<(() -> Unit)?>(null)
        private set
    var dialog by mutableStateOf<(@Composable () -> Unit)?>(null)
        private set

    fun showSheet(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
        onSheetDismissRequest = onDismissRequest
        sheet = content
    }

    fun clearSheet() {
        sheet = null
        onSheetDismissRequest = null
    }

    fun showDialog(content: @Composable () -> Unit) {
        dialog = content
    }

    fun clearDialog() {
        dialog = null
    }
}

internal val LocalMatterOverlayHost = compositionLocalOf<MatterOverlayHostState?> { null }
