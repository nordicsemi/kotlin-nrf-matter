package no.nordicsemi.nrf.matter.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

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

@Composable
fun NordicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = dark_md_theme_primary,
            onPrimary = dark_md_theme_onPrimary,
            primaryContainer = dark_md_theme_primaryContainer,
            onPrimaryContainer = dark_md_theme_onPrimaryContainer,
            inversePrimary = dark_md_theme_primaryInverse,

            secondary = dark_md_theme_secondary,
            onSecondary = dark_md_theme_onSecondary,
            secondaryContainer = dark_md_theme_secondaryContainer,
            onSecondaryContainer = dark_md_theme_onSecondaryContainer,

            background = dark_md_theme_background,
            onBackground = dark_md_theme_onBackground,

            surface = dark_md_theme_surface,
            onSurface = dark_md_theme_onSurface,
            surfaceVariant = dark_md_theme_surfaceVariant,
            onSurfaceVariant = dark_md_theme_onSurfaceVariant,

            error = dark_md_theme_error,
            onError = dark_md_theme_onError,
            errorContainer = dark_md_theme_errorContainer,
            onErrorContainer = dark_md_theme_onErrorContainer,

            outline = dark_md_theme_outline,
            inverseSurface = dark_md_theme_inverseSurface,
            inverseOnSurface = dark_md_theme_inverseOnSurface,

            tertiary = dark_md_theme_tertiary,
            tertiaryContainer = dark_md_theme_tertiaryContainer,
            onTertiary = dark_md_theme_onTertiary,
            onTertiaryContainer = dark_md_theme_onTertiaryContainer,

            surfaceContainer = dark_md_theme_surfaceContainer,
            surfaceContainerLow = dark_md_theme_surfaceContainerLow,
            surfaceContainerHigh = dark_md_theme_surfaceContainerHigh,
            surfaceContainerHighest = dark_md_theme_surfaceContainerHighest

        )
    } else {
        lightColorScheme(
            primary = light_md_theme_primary,
            onPrimary = light_md_theme_onPrimary,
            primaryContainer = light_md_theme_primaryContainer,
            onPrimaryContainer = light_md_theme_onPrimaryContainer,
            inversePrimary = light_md_theme_primaryInverse,

            secondary = light_md_theme_secondary,
            onSecondary = light_md_theme_onSecondary,
            secondaryContainer = light_md_theme_secondaryContainer,
            onSecondaryContainer = light_md_theme_onSecondaryContainer,

            background = light_md_theme_background,
            onBackground = light_md_theme_onBackground,

            surface = light_md_theme_surface,
            onSurface = light_md_theme_onSurface,
            surfaceVariant = light_md_theme_surfaceVariant,
            onSurfaceVariant = light_md_theme_onSurfaceVariant,

            error = light_md_theme_error,
            onError = light_md_theme_onError,
            errorContainer = light_md_theme_errorContainer,
            onErrorContainer = light_md_theme_onErrorContainer,

            outline = light_md_theme_outline,
            inverseSurface = light_md_theme_inverseSurface,
            inverseOnSurface = light_md_theme_inverseOnSurface,

            tertiary = light_md_theme_tertiary,
            tertiaryContainer = light_md_theme_tertiaryContainer,
            onTertiary = light_md_theme_onTertiary,
            onTertiaryContainer = light_md_theme_onTertiaryContainer,

            surfaceContainer = light_md_theme_surfaceContainer,
            surfaceContainerLow = light_md_theme_surfaceContainerLow,
            surfaceContainerHigh = light_md_theme_surfaceContainerHigh,
            surfaceContainerHighest = light_md_theme_surfaceContainerHighest
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = nordicTypography,
    ) {
        val background = colorScheme.background

        CompositionLocalProvider(
            LocalContentColor provides contentColorFor(background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background)
            ) {
                content()
            }
        }
    }
}
