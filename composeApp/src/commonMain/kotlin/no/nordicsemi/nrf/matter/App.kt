package no.nordicsemi.nrf.matter

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import no.nordicsemi.nrf.matter.commission.CommissionHandler
import no.nordicsemi.nrf.matter.hub.ActivateHubScreen
import no.nordicsemi.nrf.matter.logger.LoggerScreen
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DevicesListUiModel
import no.nordicsemi.nrf.matter.navigation.AppBar
import no.nordicsemi.nrf.matter.navigation.DetailsRoute
import no.nordicsemi.nrf.matter.navigation.HomeRoute
import no.nordicsemi.nrf.matter.navigation.HubRoute
import no.nordicsemi.nrf.matter.navigation.LoggerRoute
import no.nordicsemi.nrf.matter.navigation.config
import no.nordicsemi.nrf.matter.screens.DeviceScreen
import no.nordicsemi.nrf.matter.screens.HomeScreen
import no.nordicsemi.nrf.matter.theme.NordicTheme

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

val LocalCommissionHandler =
    staticCompositionLocalOf<CommissionHandler> {
        error("CommissionHandler not provided")
    }

@Composable
fun App(homeViewModel: HomeViewModel) {
    val devicesUiModel by homeViewModel.devicesUiModelFlow.collectAsState()

    val backStack: NavBackStack<NavKey> = rememberNavBackStack(config, HomeRoute)
    val onBack: () -> Unit = {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }

    val commissionHandler = LocalCommissionHandler.current
    val snackbarHostState = remember { SnackbarHostState() }

    NordicTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Scaffold(
                topBar = {
                    AppBar(
                        topAppBarTitle = rememberTopBarTitle(
                            backStack = backStack,
                            devicesUiModel = devicesUiModel
                        ),
                        onNavigationIconClick = {
                            if (backStack.size > 1) {
                                backStack.removeLastOrNull()
                            }
                        },
                        onLoggerIconClick = {
                            backStack.add(LoggerRoute)
                        }
                    )
                },
                floatingActionButton = {
                    if (devicesUiModel.devices.isNotEmpty()) {
                        FloatingActionButton(
                            onClick = {
                                // invoke onCommission click action.
                                commissionHandler.onCommissioningStarted()
                            }
                        ) {
                            Icon(Icons.Default.Add, null)
                        }
                    }
                }
            ) { padding ->
                NavDisplay(
                    backStack = backStack,
                    onBack = onBack,
                    entryProvider = entryProvider {
                        screens(
                            padding = padding,
                            snackbarHostState = snackbarHostState,
                            onCommissioningStarted = {
                                commissionHandler.onCommissioningStarted()
                            },
                            backStack = backStack,
                            homeViewModel = homeViewModel,
                            onDeviceClick = { deviceId ->
                                backStack.add(DetailsRoute(deviceId))
                            },
                            onActivateHubClick = {
                                backStack.add(HubRoute)
                            }
                        )
                    },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                )
            }
        }
    }
}

private fun EntryProviderScope<NavKey>.screens(
    padding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    homeViewModel: HomeViewModel,
    backStack: NavBackStack<NavKey>,
    onCommissioningStarted: () -> Unit,
    onActivateHubClick: () -> Unit,
    onDeviceClick: (deviceId: DeviceId) -> Unit,
) {
    entry<HomeRoute> {
        HomeScreen(
            innerPaddings = padding,
            homeViewModel = homeViewModel,
            onCommissionClick = onCommissioningStarted,
            onActivateHubClick = onActivateHubClick,
            onDeviceClick = { onDeviceClick(it) }
        )

    }
    entry<DetailsRoute> { key ->
        DeviceScreen(
            deviceId = key.id,
            padding = padding,
            snackbarHostState = snackbarHostState,
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            }
        )
    }
    entry<LoggerRoute> { key ->
        LoggerScreen(padding = padding)
    }
    entry<HubRoute> { key ->
        ActivateHubScreen(
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            }
        )
    }
}

@Composable
private fun rememberTopBarTitle(
    backStack: NavBackStack<NavKey>,
    devicesUiModel: DevicesListUiModel
): String {
    return remember {
        derivedStateOf {
            when (backStack.lastOrNull()) {
                HomeRoute ->
                    if (devicesUiModel.devices.isEmpty()) "nRF Matter"
                    else "Home"

                is DetailsRoute -> "Device" // TODO: device name
                else -> "nRF Matter"
            }
        }
    }.value
}
