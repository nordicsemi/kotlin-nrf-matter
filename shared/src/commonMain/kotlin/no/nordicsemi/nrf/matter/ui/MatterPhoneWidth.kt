package no.nordicsemi.nrf.matter.ui

import androidx.compose.ui.Modifier

/**
 * No-op on Android/iOS, where a full-width sheet/dialog is correct -- the window really is the
 * phone. On the web docs demo the whole browser page is the "window" that `ModalBottomSheet`/
 * `Dialog` size themselves to, so without this they'd span the full page instead of the phone
 * frame drawn inside it; this narrows and centers them to match.
 */
internal expect fun Modifier.matterPhoneWidth(): Modifier
