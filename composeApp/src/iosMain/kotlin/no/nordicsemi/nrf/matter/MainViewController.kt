package no.nordicsemi.nrf.matter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation3.runtime.NavKey
import no.nordicsemi.nrf.matter.binding.BindingsScreen
import no.nordicsemi.nrf.matter.commission.CommissioningScreen
import no.nordicsemi.nrf.matter.logger.LoggerScreen
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.navigation.BindingRoute
import no.nordicsemi.nrf.matter.navigation.CommissioningRoute
import no.nordicsemi.nrf.matter.navigation.HomeRoute
import no.nordicsemi.nrf.matter.navigation.LoggerRoute
import no.nordicsemi.nrf.matter.screens.HomeScreen
import no.nordicsemi.nrf.matter.theme.NordicTheme
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.module
import platform.UIKit.UIViewController

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

/**
 * One-time app bootstrap: wires the native Swift Matter implementations into the shared
 * code and starts Koin. Must be called exactly once, before any [TabRootViewController]
 * or [DetailViewController] is created. Called from `iOSApp.init()`.
 */
fun newCommissioningRoute(): NavKey = CommissioningRoute()

fun InitApp(swiftCodeProvider: SwiftCodeProvider) {
    NordicLogger.setLogger(swiftCodeProvider.getLogger())

    initKoin(
        module {
            single { swiftCodeProvider }
        }
    )
}

/**
 * Renders the root content of a bottom-bar tab (Home / Bindings / Logger) for hosting
 * inside a SwiftUI `NavigationStack`. Carries no navigation chrome of its own.
 */
fun TabRootViewController(
    route: NavKey,
    onNavigate: (NavKey) -> Unit,
): UIViewController = ComposeUIViewController {
    NordicTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (route) {
                is HomeRoute -> HomeScreen(
                    homeViewModel = koinViewModel(),
                    onCommissionClick = { onNavigate(CommissioningRoute()) }
                )
                is BindingRoute -> BindingsScreen()
                is LoggerRoute -> LoggerScreen()
                else -> Unit
            }
        }
    }
}

/**
 * Renders a screen pushed on top of a tab's native navigation stack (e.g. Commissioning).
 */
fun DetailViewController(
    route: NavKey,
    onBack: () -> Unit,
    onNavigateToTab: (NavKey) -> Unit,
): UIViewController = ComposeUIViewController {
    NordicTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (route) {
                is CommissioningRoute -> CommissioningScreen(
                    onBack = onBack,
                    navigateToLogs = { onNavigateToTab(LoggerRoute) }
                )
                else -> Unit
            }
        }
    }
}
