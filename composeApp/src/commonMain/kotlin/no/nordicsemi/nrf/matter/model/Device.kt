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
    val vendorId: String? = null,
    val productId: String? = null,
    val deviceType: DeviceType = DeviceType.UNSUPPORTED, // TODO: device type is no longer provided by the DeviceDescriptor.
    val name: String? = null,
//    val room: String? = null, todo: Removed since it is deprecated in the Matter API.
    val productName: String? = null,
    val vendorName: String? = null,
    val uniqueId: String? = null,
    val softwareVersion: String? = null,
    val specificationVersion: Long ? = null,
    val serialNumer: String? = null,
    val deviceMatterInfo: List<DeviceMatterInfo>,
)

@Serializable
enum class DeviceType {
    UNSUPPORTED,
    LIGHT_ON_OFF,
    DIMMABLE_LIGHT,
    LIGHT_SWITCH,
    OUTLET,
    DOOR_LOCK,
    COLOR_TEMPERATURE_LIGHT,
    EXTENDED_COLOR_LIGHT,
    MANUFACTURER_SPECIFIC_DEVICE,
    ;

    override fun toString(): String {
        return when (this) {
            UNSUPPORTED -> "Not Supported"
            LIGHT_ON_OFF -> "Light On/Off"
            DIMMABLE_LIGHT -> "Dimmable Light"
            LIGHT_SWITCH -> "Light Switch"
            OUTLET -> "Outlet"
            COLOR_TEMPERATURE_LIGHT -> "Color Temperature Light"
            EXTENDED_COLOR_LIGHT -> "Extended Color Light"
            DOOR_LOCK -> "Door Lock"
            MANUFACTURER_SPECIFIC_DEVICE -> "Manufacturer Specific Device"
        }
    }

    companion object {
        fun parse(matterDeviceType: Long): DeviceType {
            return when (matterDeviceType) {
                256L -> DeviceType.LIGHT_ON_OFF // 0x0100 On/Off Light
                257L -> DeviceType.DIMMABLE_LIGHT // 0x0101 Dimmable Light
                259L -> DeviceType.LIGHT_SWITCH // 0x0103 On/Off Light Switch
                260L -> DeviceType.LIGHT_SWITCH // 0x0104 Dimmer Switch

                266L -> DeviceType.OUTLET // 0x010A (On/Off Plug-in Unit)
                268L -> DeviceType.COLOR_TEMPERATURE_LIGHT // 0x010C Color Temperature Light
                269L -> DeviceType.EXTENDED_COLOR_LIGHT // 0x010D Extended Color Light
                10L -> DeviceType.DOOR_LOCK // 0x000A door lock // todo need to review the hex value
//            11L ->   Door Lock Controller // (0x000B)
                0xFFF10001 -> DeviceType.MANUFACTURER_SPECIFIC_DEVICE
                else -> DeviceType.UNSUPPORTED
            }
        }
    }
}

@Serializable
data class Devices(
    val lastDeviceId: DeviceId = DeviceId.Zero,
    val devicesList: List<Device> = emptyList()
)

/**
 * Group devices by category (Lights, Security, etc.)
 */
enum class DeviceSection {
    LIGHTS,
    SECURITY,
    OUTLETS,
    OTHER
}
