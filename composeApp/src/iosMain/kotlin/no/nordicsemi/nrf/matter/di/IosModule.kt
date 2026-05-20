package no.nordicsemi.nrf.matter.di

import no.nordicsemi.nrf.matter.CommissioningViewModel
import no.nordicsemi.nrf.matter.HomeViewModel
import no.nordicsemi.nrf.matter.SwiftCodeProvider
import no.nordicsemi.nrf.matter.datasource.DeviceStateDataSource
import no.nordicsemi.nrf.matter.datasource.DevicesDataSource
import no.nordicsemi.nrf.matter.datasource.UserPreferencesDataSource
import no.nordicsemi.nrf.matter.device.DevicePresenter
import no.nordicsemi.nrf.matter.domain.DeviceCommandHandler
import no.nordicsemi.nrf.matter.hub.ActivateHubViewModel
import no.nordicsemi.nrf.matter.logger.LoggerViewModel
import no.nordicsemi.nrf.matter.model.DeviceController
import no.nordicsemi.nrf.matter.model.IosDeviceController
import no.nordicsemi.nrf.matter.repository.DevicesRepository
import no.nordicsemi.nrf.matter.repository.DevicesStateRepository
import no.nordicsemi.nrf.matter.repository.IosDevicesDataSource
import no.nordicsemi.nrf.matter.repository.IosDevicesStateDataSource
import no.nordicsemi.nrf.matter.repository.IosUserPreferencesDataSource
import no.nordicsemi.nrf.matter.repository.UserPreferencesRepository
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
    single<UserPreferencesDataSource> {
        IosUserPreferencesDataSource()
    }

    // Repositories.
    single<DevicesRepository> {
        DevicesRepository(dataSource = get())
    }
    single<DevicesStateRepository> {
        DevicesStateRepository(dataSource = get())
    }
    single<UserPreferencesRepository> {
        UserPreferencesRepository(dataSource = get())
    }

    // Device Controller
    single<DeviceController> { IosDeviceController(
        get<SwiftCodeProvider>().getMatterOnOffController(),
        get<SwiftCodeProvider>().getDecommissioner(),
        get<SwiftCodeProvider>().getMatterBinder(),
        get<SwiftCodeProvider>().getMatterDoorController(),
        get<SwiftCodeProvider>().getMatterOutletController(),
        get<SwiftCodeProvider>().getMatterManufacturerCustomDataController(),
        get<SwiftCodeProvider>().getMatterClusterExtensionController(),
    ) }

    // View models.
    viewModelOf(::HomeViewModel)

    viewModel { CommissioningViewModel(get()) }

    viewModel {
        LoggerViewModel(get<SwiftCodeProvider>().getLogger())
    }

    viewModel {
        ActivateHubViewModel(get<SwiftCodeProvider>().getHubController())
    }

    single {
        DevicePresenter(
            get<DevicesRepository>(),
            get<DevicesStateRepository>(),
            get<DeviceController>(),
            get<DeviceCommandHandler>()
        )
    }

}
