package no.nordicsemi.nrf.matter.di

import no.nordicsemi.nrf.matter.HomeViewModel
import no.nordicsemi.nrf.matter.MatterCommissioner
import no.nordicsemi.nrf.matter.adapters.BindingControllerImpl
import no.nordicsemi.nrf.matter.adapters.MatterClusterExtensionControllerImpl
import no.nordicsemi.nrf.matter.adapters.MatterCommissionerImpl
import no.nordicsemi.nrf.matter.adapters.MatterDecommissionerImpl
import no.nordicsemi.nrf.matter.adapters.MatterDoorLockControllerImpl
import no.nordicsemi.nrf.matter.adapters.MatterLightControllerImpl
import no.nordicsemi.nrf.matter.adapters.MatterManufacturerSpecificControllerImpl
import no.nordicsemi.nrf.matter.binding.BindGroupDevicesUseCase
import no.nordicsemi.nrf.matter.SwiftCodeProvider
import no.nordicsemi.nrf.matter.binding.BindingLogsProviderImpl
import no.nordicsemi.nrf.matter.binding.BindingViewModel
import no.nordicsemi.nrf.matter.binding.GroupBindingViewModel
import no.nordicsemi.nrf.matter.binding.DataStoreProvider
import no.nordicsemi.nrf.matter.binding.IosBindGroupDevicesUseCase
import no.nordicsemi.nrf.matter.commission.CommissioningViewModelIos
import no.nordicsemi.nrf.matter.controller.BindingController
import no.nordicsemi.nrf.matter.controller.BindingLogsProvider
import no.nordicsemi.nrf.matter.controller.MatterClusterExtensionController
import no.nordicsemi.nrf.matter.controller.MatterDecommissioner
import no.nordicsemi.nrf.matter.controller.MatterDoorLockController
import no.nordicsemi.nrf.matter.controller.MatterLightController
import no.nordicsemi.nrf.matter.controller.MatterManufacturerSpecificController
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.logger.LoggerViewModel
import no.nordicsemi.nrf.matter.repository.BindingRepository
import no.nordicsemi.nrf.matter.repository.GroupBindingRepository
import no.nordicsemi.nrf.matter.repository.DevicesRepository
import no.nordicsemi.nrf.matter.repository.DevicesStateRepository
import no.nordicsemi.nrf.matter.repository.IosDevicesDataSource
import no.nordicsemi.nrf.matter.repository.IosDevicesStateDataSource
import no.nordicsemi.nrf.matter.ui.light.LightCommandHandler
import no.nordicsemi.nrf.matter.ui.lock.LockCommandHandler
import no.nordicsemi.nrf.matter.ui.manspec.ManufacturerSpecCommandHandler
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
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

val iosModule = module {

    // Data sources.
    single<DevicesDataSource> {
        IosDevicesDataSource()
    }
    single<DeviceStateDataSource> {
        IosDevicesStateDataSource()
    }
    single {
        DataStoreProvider().createDataStore()
    }

    // Repositories.
    single<DevicesRepository> {
        DevicesRepository(dataSource = get())
    }
    single<DevicesStateRepository> {
        DevicesStateRepository(dataSource = get())
    }
    single<BindingRepository> {
        BindingRepository(get())
    }
    single<GroupBindingRepository> {
        GroupBindingRepository(get())
    }
    single<BindGroupDevicesUseCase> {
        IosBindGroupDevicesUseCase(get())
    }

    // View models.
    viewModelOf(::HomeViewModel)

    viewModel { CommissioningViewModelIos(get(), get()) }

    viewModel { LoggerViewModel() }
    viewModel { BindingViewModel(get(), get(), get()) }
    viewModel { GroupBindingViewModel(get(), get(), get()) }
    factory { LightCommandHandler(get()) }
    factory { LockCommandHandler(get()) }
    factory { ManufacturerSpecCommandHandler(get(), get(), get()) }

    single<MatterCommissioner> { MatterCommissionerImpl() }
    single<MatterDecommissioner> { MatterDecommissionerImpl() }
    single<BindingController> { BindingControllerImpl() }
    single<BindingLogsProvider> { BindingLogsProviderImpl() }
    single<MatterLightController> { MatterLightControllerImpl() }
    single<MatterDoorLockController> { MatterDoorLockControllerImpl() }
    single<MatterClusterExtensionController> { MatterClusterExtensionControllerImpl() }
    single<MatterManufacturerSpecificController> {
        MatterManufacturerSpecificControllerImpl()
    }
}
