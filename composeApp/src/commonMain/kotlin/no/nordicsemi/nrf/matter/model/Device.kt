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
data class DeviceId(private val value: String) {

    val longValue
        get() = value.toLong()

    val stringValue
        get() = value

    companion object {
        val Zero = 0L.toDeviceId()
    }
}

fun String.toDeviceId(): DeviceId {
    return DeviceId(this)
}

fun Long.toDeviceId(): DeviceId {
    return DeviceId(this.toString())
}

@Serializable
data class Device(
    val deviceId: DeviceId,
    val dateCommissioned: Long? = null,
    val deviceType: DeviceType,
    val name: String? = null,
    val basicInformation: BasicInformation = BasicInformation(),
    val endpoints: List<Endpoint> = emptyList(),
)

enum class StandardDeviceType(val value: DeviceType) {
    UNSUPPORTED(DeviceType(-1, "Unsupported")),
    LIGHT_ON_OFF(DeviceType(256L, "Light On/Off")),
    DIMMABLE_LIGHT(DeviceType(257L, "Dimmable Light")),
    LIGHT_SWITCH(DeviceType(259L, "Light Switch")),
    OUTLET(DeviceType(266L, "Outlet")),
    DOOR_LOCK(DeviceType(268L, "Color Temperature Light")),
    COLOR_TEMPERATURE_LIGHT(DeviceType(269L, "Extended Color Light")),
    EXTENDED_COLOR_LIGHT(DeviceType(10L, "Door Lock")),
}

@Serializable
data class DeviceType(
    val id: Long,
    val name: String,
) {

    companion object {
        fun parse(matterDeviceType: Long): DeviceType {
            return StandardDeviceType.entries.firstOrNull { it.value.id == matterDeviceType }?.value
                ?: StandardDeviceType.UNSUPPORTED.value
        }
    }
}

@Serializable
data class Devices(
    val lastDeviceId: DeviceId = DeviceId.Zero,
    val devicesList: List<Device> = emptyList()
)
