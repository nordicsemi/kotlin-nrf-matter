package no.nordicsemi.nrf.matter.model

import kotlinx.serialization.Serializable
import no.nordicsemi.nrf.matter.api.NordicMatters

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

const val ROOT_ENDPOINT: Int = 0

@Serializable
data class Endpoint(
    val id: Int,
    val types: List<Long> = emptyList(),
    val serverClusters: List<Long> = emptyList(),
    val clientClusters: List<Long> = emptyList(),
    val parts: List<Int> = emptyList(),
) {
    val isRoot: Boolean
        get() = id == ROOT_ENDPOINT
}

fun List<Endpoint>.deviceTypes(): List<DeviceType> =
    filterNot { it.isRoot }
        .flatMap { it.types }
        .map { NordicMatters.parseDeviceType(it) }

private val UTILITY_DEVICE_TYPE_IDS = setOf(0x000EL, 0x0011L, 0x0013L, 0x0016L)

fun List<Endpoint>.deviceType(): DeviceType {
    val applicationEndpoints = filterNot { it.isRoot }
    val childIds = applicationEndpoints.flatMap { it.parts }.toSet()
    val topLevel = applicationEndpoints.filter { it.id !in childIds }.ifEmpty { applicationEndpoints }

    fun List<Endpoint>.firstType(): DeviceType? = asSequence()
        .flatMap { it.types }
        .filter { it !in UTILITY_DEVICE_TYPE_IDS }
        .map { NordicMatters.parseDeviceType(it) }
        .firstOrNull { it != SupportedDeviceType.UNKNOWN.value }

    return topLevel.firstType()
        ?: applicationEndpoints.firstType()
        ?: SupportedDeviceType.UNKNOWN.value
}
