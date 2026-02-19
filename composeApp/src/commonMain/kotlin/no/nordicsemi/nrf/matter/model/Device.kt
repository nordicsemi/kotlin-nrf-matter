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

@Serializable
data class Device(
    val dateCommissioned: Long? = null,
    val vendorId: String? = null,
    val productId: String? = null,
    val deviceType: DeviceType = DeviceType.UNKNOWN, // TODO: device type is no longer provided by the DeviceDescriptor.
    val deviceId: Long = 0L,
    val name: String? = null,
//    val room: String? = null, todo: Removed since it is deprecated in the Matter API.
    val productName: String? = null,
    val vendorName: String? = null
)

@Serializable
enum class DeviceType {
    UNKNOWN,
    LIGHT_ON_OFF,
    DIMMABLE_LIGHT,
    LIGHT_SWITCH,
    OUTLET,
    COLOR_TEMPERATURE_LIGHT,
    EXTENDED_COLOR_LIGHT, ;
}

@Serializable
data class Devices(
    val lastDeviceId: Long = 0L,
    val devicesList: List<Device> = emptyList()
)