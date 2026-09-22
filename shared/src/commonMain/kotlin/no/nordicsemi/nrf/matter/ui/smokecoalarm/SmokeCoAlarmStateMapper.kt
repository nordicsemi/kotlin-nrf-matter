package no.nordicsemi.nrf.matter.ui.smokecoalarm

import no.nordicsemi.nrf.matter.model.AlarmState

fun Number.toAlarmState(): AlarmState =
    AlarmState.entries.firstOrNull { it.value == toInt() } ?: AlarmState.NORMAL