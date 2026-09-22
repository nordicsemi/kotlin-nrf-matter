package no.nordicsemi.nrf.matter.ui.rvc

import no.nordicsemi.nrf.matter.model.RvcOperationalState

fun Int.toRvcOperationalState(): RvcOperationalState? =
    RvcOperationalState.entries.firstOrNull { it.value == this }

fun RvcOperationalState?.toLabel(): String = when (this) {
    RvcOperationalState.RUNNING -> "Cleaning"
    RvcOperationalState.PAUSED -> "Paused"
    RvcOperationalState.STOPPED -> "Stopped"
    RvcOperationalState.ERROR -> "Error"
    RvcOperationalState.SEEKING_CHARGER -> "Returning to dock"
    RvcOperationalState.CHARGING -> "Charging"
    RvcOperationalState.DOCKED -> "Docked"
    null -> "Unknown"
}
