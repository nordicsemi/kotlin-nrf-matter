package no.nordicsemi.nrf.matter.model

import kotlinx.serialization.Serializable

/*
 * Copyright (c) 2025, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/** The endpoint every device answers on, the root node. */
const val ROOT_ENDPOINT: Int = 0

/**
 * One endpoint of a device: what it is, and what it implements.
 *
 * This is what the Descriptor cluster reports for an endpoint - its device types, the clusters it
 * serves, and the clusters it can drive as a client.
 */
@Serializable
data class Endpoint(
    val id: Int,
    val types: List<Long> = emptyList(),
    val serverClusters: List<Long> = emptyList(),
    val clientClusters: List<Long> = emptyList(),
    val manufacturerSpecificData: ManufacturerSpecificData? = null, // TODO: probably it's not the best place
) {
    /** Whether this is the root node, which describes the node rather than what the device does. */
    val isRoot: Boolean
        get() = id == ROOT_ENDPOINT
}

/**
 * The root node of the device, or `null` for a device that was never read.
 *
 * Endpoint 0 is present on every device and is always the first endpoint walked, so this is
 * non-`null` for anything the app has actually read.
 */
val List<Endpoint>.root: Endpoint?
    get() = endpoint(ROOT_ENDPOINT)

/** The endpoint with this id, or `null` if the device does not have one. */
fun List<Endpoint>.endpoint(id: Int): Endpoint? = firstOrNull { it.id == id }

/** The device types named by this device's endpoints, the root node aside. */
fun List<Endpoint>.deviceTypes(): List<DeviceType> =
    filterNot { it.isRoot }
        .flatMap { it.types }
        .map { DeviceType.parse(it) }

/**
 * What the device is, taken from the first endpoint that names a type the library knows.
 *
 * The root node is skipped: its device type describes the node, not what the device does.
 */
fun List<Endpoint>.deviceType(): DeviceType =
    deviceTypes().firstOrNull { it != DeviceType.UNSUPPORTED } ?: DeviceType.UNSUPPORTED
