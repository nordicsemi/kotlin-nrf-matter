package no.nordicsemi.nrf.matter.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

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
@SerialName("Home")
data object HomeRoute : NavKey

@Serializable
@SerialName("Binding")
data object BindingRoute : NavKey

@Serializable
@SerialName("Logger")
data object LoggerRoute : NavKey

@Serializable
@SerialName("Commissioning")
data class CommissioningRoute(
    val id: Int = nextId++
) : NavKey {

    companion object {
        var nextId: Int = 0
    }
}

val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(HomeRoute::class, HomeRoute.serializer())
            subclass(CommissioningRoute::class, CommissioningRoute.serializer())
            subclass(BindingRoute::class, BindingRoute.serializer())
            subclass(LoggerRoute::class, LoggerRoute.serializer())
        }
    }
}

val NavKey.title: String
    get() = when (this) {
        is HomeRoute -> "Dashboard"
        is BindingRoute -> "Bindings"
        is LoggerRoute -> "Logs Panel"
        is CommissioningRoute -> "Commissioning"
        else -> "Unknown"
    }

val NavKey.icon: ImageVector
    get() = when (this) {
        is HomeRoute -> Icons.Default.Home
        is BindingRoute -> Icons.Default.Cable
        is LoggerRoute -> Icons.Default.Terminal
        is CommissioningRoute -> Icons.Default.Add
        else -> Icons.Default.Home
    }
