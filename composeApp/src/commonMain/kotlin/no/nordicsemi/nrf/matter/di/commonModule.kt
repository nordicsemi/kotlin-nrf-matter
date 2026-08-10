package no.nordicsemi.nrf.matter.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import no.nordicsemi.nrf.matter.binding.BaseBindingDataSource
import no.nordicsemi.nrf.matter.binding.BaseGroupBindingDataSource
import no.nordicsemi.nrf.matter.binding.BindDevicesUseCase
import no.nordicsemi.nrf.matter.binding.BindingDataSource
import no.nordicsemi.nrf.matter.binding.GroupBindingDataSource
import no.nordicsemi.nrf.matter.commission.DecommissionUseCases
import no.nordicsemi.nrf.matter.repository.BindingRepository
import no.nordicsemi.nrf.matter.repository.GroupBindingRepository
import no.nordicsemi.nrf.matter.repository.DevicesRepository
import no.nordicsemi.nrf.matter.repository.DevicesStateRepository
import no.nordicsemi.nrf.matter.ui.MatterControllerCache
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

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

val commonModule = module {

    // Define CoroutineScope as a singleton
    single { CoroutineScope(Dispatchers.Default + SupervisorJob()) }

    // Repositories
    singleOf(::DevicesRepository)
    singleOf(::DevicesStateRepository)
    singleOf(::BindingRepository)
    singleOf(::GroupBindingRepository)
    single {
        BindDevicesUseCase(
            get(),
            get(),
            get(),
        )
    }
    single {
        DecommissionUseCases(
            get(),
            get(),
            get(),
            get(),
        )
    }

    single { MatterControllerCache() }

    single<BindingDataSource> {
        BaseBindingDataSource(get())
    }
    single<GroupBindingDataSource> {
        BaseGroupBindingDataSource(get())
    }
}
