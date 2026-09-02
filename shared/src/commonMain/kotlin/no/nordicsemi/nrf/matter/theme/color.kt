package no.nordicsemi.nrf.matter.theme

import androidx.compose.ui.graphics.Color

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

// Nordic colors

val NordicBlue = Color(0xFF00A9CE)
val NordicSky = Color(0xFF6AD1E3)
val NordicBlueslate = Color(0xFF0033A0)
val NordicLake = Color(0xFF0077C8)
val NordicGrass = Color(0xFFD0DF00)
val NordicGreen = Color(0xFF00A651)
val NordicSun = Color(0xFFFFCD00)
val NordicRed = Color(0xFFEE2F4E)
val NordicFall = Color(0xFFF58220)
val NordicLightGray = Color(0xFFD9E1E2)
val NordicMiddleGray = Color(0xFF768692)
val NordicDarkGray = Color(0xFF333f48)

// Light theme colors

val light_md_theme_primary = NordicBlue
val light_md_theme_onPrimary = Color(0xFFFFFFFF)
val light_md_theme_primaryContainer = Color(0xFFB3EBFF)
val light_md_theme_onPrimaryContainer = Color(0xFF001F29)
val light_md_theme_secondary = NordicLake
val light_md_theme_onSecondary = Color(0xFFFFFFFF)
val light_md_theme_secondaryContainer = NordicLake
val light_md_theme_onSecondaryContainer = Color(0xFFFFFFFF)
val light_md_theme_tertiary = NordicBlueslate
val light_md_theme_onTertiary = Color(0xFFFFFFFF)
val light_md_theme_tertiaryContainer = Color(0xFFD0E4FF)
val light_md_theme_onTertiaryContainer = Color(0xFF41000A)
val light_md_theme_error = Color(0xFFBA1B1B)
val light_md_theme_errorContainer = Color(0xFFFFDAD4)
val light_md_theme_onError = Color(0xFFFFFFFF)
val light_md_theme_onErrorContainer = Color(0xFF410001)
val light_md_theme_background = Color(0xFFFFFFFF)
val light_md_theme_onBackground = Color(0xFF191C1E)
val light_md_theme_surface = Color(0xFFF5F5F5)
val light_md_theme_surfaceContainer = Color(0xFFE4EFF2)
val light_md_theme_surfaceContainerLow = Color(0xFFEAF1F4)
val light_md_theme_surfaceContainerHigh = Color(0xFFE1E8EB)
val light_md_theme_surfaceContainerHighest = Color(0xFFDDE4E7)
val light_md_theme_onSurface = Color(0xFF191C1E)
val light_md_theme_surfaceVariant = Color(0xFFDCE4E8)
val light_md_theme_onSurfaceVariant = Color(0xFF40484B)
val light_md_theme_outline = Color(0xFF70787C)
val light_md_theme_inverseOnSurface = Color(0xFFF0F1F4)
val light_md_theme_inverseSurface = Color(0xFF2E3133)
val light_md_theme_primaryInverse = Color(0xFF57D5FC)

val light_md_appBarColor = Color(0xFF00A9CE)
val light_md_statusBarColor = Color(0xFF00A9CE)
val light_md_navigationBarColor = Color(0xFFE1EFF2)

// Dark Theme colors
val dark_md_theme_primary = NordicBlue
val dark_md_theme_onPrimary = Color(0xFFFFFFFF)
val dark_md_theme_primaryContainer = NordicSky
val dark_md_theme_onPrimaryContainer = Color(0xFFFFFFFF)
val dark_md_theme_secondary = NordicBlue
val dark_md_theme_onSecondary = Color(0xFFFFFFFF)
val dark_md_theme_secondaryContainer = NordicBlue
val dark_md_theme_onSecondaryContainer = Color(0xFFFFFFFF)
val dark_md_theme_tertiary = Color(0xFF670016)
val dark_md_theme_onTertiary = Color(0xFFFFB2B6)
val dark_md_theme_tertiaryContainer = Color(0xFFFFB2B6)
val dark_md_theme_onTertiaryContainer = Color(0xFF670016)
val dark_md_theme_error = Color(0xFFFFB4A9)
val dark_md_theme_errorContainer = Color(0xFF930006)
val dark_md_theme_onError = Color(0x00000000)
val dark_md_theme_onErrorContainer = Color(0xFFFFDAD4)
val dark_md_theme_background = Color(0x00000000)
val dark_md_theme_onBackground = Color(0xFFE1E2E5)
val dark_md_theme_surface = Color(0xFF191C1E)
val dark_md_theme_surfaceContainer = Color(0xFF1B282B)
val dark_md_theme_surfaceContainerLow = Color(0xFF1B1F22)
val dark_md_theme_surfaceContainerHigh = Color(0xFF2D3336)
val dark_md_theme_surfaceContainerHighest = Color(0xFF42484B)
val dark_md_theme_onSurface = Color(0xFFE1E2E5)
val dark_md_theme_surfaceVariant = Color(0xFF40484B)
val dark_md_theme_onSurfaceVariant = Color(0xFFBFC8CC)
val dark_md_theme_outline = Color(0xFF899296)
val dark_md_theme_inverseOnSurface = Color(0xFF191C1E)
val dark_md_theme_inverseSurface = Color(0xFFE1E2E5)
val dark_md_theme_primaryInverse = Color(0xFF006780)

val dark_md_appBarColor = Color(0xFF333f48)
val dark_md_statusBarColor = NordicDarkGray
val dark_md_navigationBarColor = Color(0xFF17282C)
