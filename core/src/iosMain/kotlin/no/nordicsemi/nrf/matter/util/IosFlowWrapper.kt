package no.nordicsemi.nrf.matter.util

import kotlinx.coroutines.flow.MutableSharedFlow

class IosFlowWrapper<T> {
    val flow = MutableSharedFlow<T>(replay = 1)

    fun emit(value: T) {
        flow.tryEmit(value)
    }
}
