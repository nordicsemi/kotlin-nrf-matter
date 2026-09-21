package no.nordicsemi.nrf.matter.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * The blur behind a busy overlay (Matter Device information sheet, decommission-in-progress
 * list, binding-write-in-progress list). Android/iOS use `cloudy` (no wasmJs artifact); the web
 * target uses Compose's own Skia-backed blur instead.
 */
@Composable
internal expect fun Modifier.matterBlur(): Modifier
