@file:OptIn(ExperimentalAtomicApi::class)

package no.nordicsemi.nrf.matter.api

import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

private val defaultCommissioningRooms =
    listOf("Living Room", "Bedroom", "Office", "Kitchen", "Dining Room")

private val commissioningRoomsRef = AtomicReference(defaultCommissioningRooms)

var NordicMatters.commissioningRooms: List<String>
    get() = commissioningRoomsRef.load()
    set(value) = commissioningRoomsRef.store(value)
