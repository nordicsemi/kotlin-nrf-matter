package no.nordicsemi.nrf.matter

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import no.nordicsemi.nrf.matter.screens.BindingsScreen
import no.nordicsemi.nrf.matter.screens.CommissioningScreen
import no.nordicsemi.nrf.matter.screens.LoggerScreen
import no.nordicsemi.nrf.matter.model.DevicesListUiModel
import no.nordicsemi.nrf.matter.navigation.AppBar
import no.nordicsemi.nrf.matter.navigation.BindingRoute
import no.nordicsemi.nrf.matter.navigation.CommissioningRoute
import no.nordicsemi.nrf.matter.navigation.HomeRoute
import no.nordicsemi.nrf.matter.navigation.LoggerRoute
import no.nordicsemi.nrf.matter.navigation.config
import no.nordicsemi.nrf.matter.navigation.icon
import no.nordicsemi.nrf.matter.navigation.title
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

@Composable
fun App(homeViewModel: HomeViewModel) {
    val devicesUiModel by homeViewModel.devicesUiModelFlow.collectAsState()
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(config, HomeRoute)

    fun navigateToHome() = backStack.replaceWith(routesForTab(HomeRoute))

    fun navigateToTab(tabRoute: NavKey) = backStack.replaceWith(routesForTab(tabRoute))

    val onBack: () -> Unit = {
        when (backActionFor(backStack.lastOrNull(), backStack.size)) {
            BackAction.POP -> backStack.removeLastOrNull()
            BackAction.GO_HOME -> navigateToHome()
            BackAction.EXIT -> Unit
        }
    }

    val currentRoute = backStack.lastOrNull() ?: HomeRoute
    val tabs = remember { listOf(HomeRoute, BindingRoute, LoggerRoute) }

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
                        )
                    )
                },
                floatingActionButton = {
                    if (devicesUiModel.devices.isNotEmpty()) {
                        FloatingActionButton(
                            onClick = {
                                // Recreate the last destination if the route is already at the top.
                                val lastIndex = backStack.lastIndex
                                if (lastIndex >= 0 && backStack[lastIndex] is CommissioningRoute) {
                                    backStack[lastIndex] = CommissioningRoute()
                                } else {
                                    backStack.add(CommissioningRoute())
                                }
                            }
                        ) {
                            Icon(Icons.Default.Add, null)
                        }
                    }
                },
                bottomBar = {
                    NavigationBar(
                        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                        containerColor = MaterialTheme.colorScheme.background,
                        tonalElevation = 8.dp
                    ) {
                        tabs.forEach { tabRoute ->
                            val isSelected = currentRoute::class == tabRoute::class

                            NavigationBarItem(
                                modifier = Modifier.testTag(tabRoute.title),
                                selected = isSelected,
                                onClick = {
                                    if (!isSelected) {
                                        navigateToTab(tabRoute)
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = tabRoute.icon,
                                        contentDescription = tabRoute.title
                                    )
                                },
                                label = {
                                    Text(
                                        text = tabRoute.title,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                }
            ) { padding ->
                NavDisplay(
                    backStack = backStack,
                    onBack = onBack,
                    entryProvider = entryProvider {
                        screens(
                            onCommissioningStarted = {
                                backStack.add(CommissioningRoute())
                            },
                            backStack = backStack,
                            homeViewModel = homeViewModel
                        )
                    },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    transitionSpec = {
                        EnterTransition.None togetherWith ExitTransition.None
                    },
                    popTransitionSpec = {
                        EnterTransition.None togetherWith ExitTransition.None
                    },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

private fun EntryProviderScope<NavKey>.screens(
    homeViewModel: HomeViewModel,
    backStack: NavBackStack<NavKey>,
    onCommissioningStarted: () -> Unit,
) {
    entry<HomeRoute> {
        HomeScreen(
            homeViewModel = homeViewModel,
            onCommissionClick = onCommissioningStarted
        )

    }
    entry<LoggerRoute> { _ ->
        LoggerScreen()
    }
    entry<CommissioningRoute> { _ ->
        CommissioningScreen(
            onBack = {
                when (backActionFor(backStack.lastOrNull(), backStack.size)) {
                    BackAction.POP -> backStack.removeLastOrNull()
                    BackAction.GO_HOME -> backStack.replaceWith(routesForTab(HomeRoute))
                    BackAction.EXIT -> Unit
                }
            },
            navigateToLogs = {
                backStack.add(LoggerRoute)
            }
        )
    }
    entry<BindingRoute> {
        BindingsScreen()
    }
}

internal enum class BackAction {
    POP,
    GO_HOME,
    EXIT,
}

internal fun backActionFor(currentRoute: NavKey?, stackSize: Int): BackAction {
    return when {
        stackSize > 1 -> BackAction.POP
        currentRoute !is HomeRoute -> BackAction.GO_HOME
        else -> BackAction.EXIT
    }
}

internal fun routesForTab(tabRoute: NavKey): List<NavKey> {
    return if (tabRoute is HomeRoute) {
        listOf(HomeRoute)
    } else {
        listOf(HomeRoute, tabRoute)
    }
}

private fun NavBackStack<NavKey>.replaceWith(routes: List<NavKey>) {
    while (isNotEmpty()) {
        removeLastOrNull()
    }
    routes.forEach { add(it) }
}

@Composable
private fun rememberTopBarTitle(
    backStack: NavBackStack<NavKey>,
    devicesUiModel: DevicesListUiModel
): String {
    return remember {
        derivedStateOf {
            when (backStack.lastOrNull()) {
                HomeRoute -> if (devicesUiModel.devices.isEmpty()) "nRF Matter" else "Dashboard"
                is CommissioningRoute -> "Commissioning"
                is BindingRoute -> "Bindings"
                is LoggerRoute -> "Logs"
                else -> "nRF Matter"
            }
        }
    }.value
}
