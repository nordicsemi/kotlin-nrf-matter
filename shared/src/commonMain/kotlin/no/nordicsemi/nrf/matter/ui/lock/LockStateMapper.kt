package no.nordicsemi.nrf.matter.ui.lock

import no.nordicsemi.nrf.matter.model.LockDeviceState
import no.nordicsemi.nrf.matter.ui.UiState

fun Number.toLockDeviceState(): LockDeviceState? =
    LockDeviceState.entries.firstOrNull { it.value == toInt() }

fun LockDeviceState.toUiState(): UiState<LockDeviceState> = when (this) {
    LockDeviceState.LOCKED,
    LockDeviceState.UNLOCKED -> UiState.Success(this)

    LockDeviceState.NOT_FULLY_LOCKED,
    LockDeviceState.UNLATCHED -> UiState.Loading()
}
