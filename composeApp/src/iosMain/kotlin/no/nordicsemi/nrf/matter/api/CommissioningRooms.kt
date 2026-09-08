@file:OptIn(ExperimentalAtomicApi::class)

package no.nordicsemi.nrf.matter.api

import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

/** Rooms offered when an app sets none of its own. */
private val defaultCommissioningRooms =
    listOf("Living Room", "Bedroom", "Office", "Kitchen", "Dining Room")

private val commissioningRoomsRef = AtomicReference(defaultCommissioningRooms)

/**
 * The rooms iOS offers the user while adding a device.
 *
 * The "Add Device" UI belongs to a system app extension that runs ahead of - and in a different
 * process from - the rest of the app, so it cannot ask a [Fabric] what rooms exist. The list is
 * handed over through shared storage when commissioning starts instead, which means it has to be
 * set before then to take effect.
 *
 * Nothing is done with the room the user picks: it is offered because the system flow shows the
 * step regardless, and the library has no notion of rooms.
 *
 * iOS-only, and process-wide - the whole app shares one list.
 */
var NordicMatters.commissioningRooms: List<String>
    get() = commissioningRoomsRef.load()
    set(value) = commissioningRoomsRef.store(value)
