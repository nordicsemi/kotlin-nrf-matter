package no.nordicsemi.nrf.matter.commission

import no.nordicsemi.nrf.matter.cluster.BasicInformationCluster
import no.nordicsemi.nrf.matter.cluster.DescriptorCluster
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.ROOT_ENDPOINT
import no.nordicsemi.nrf.matter.model.deviceType
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock

internal class FinaliseCommissioningUseCase(
    private val client: MatterClient,
) {

    private val namesFromCommissioning = mutableMapOf<DeviceId, String>()

    fun rememberName(deviceId: DeviceId, name: String?) {
        name?.let { namesFromCommissioning[deviceId] = it }
    }

    suspend fun readDevice(deviceId: DeviceId): Device {
        val basicInfo = catchAndThrow(deviceId, Stage.READ_BASIC_INFORMATION) {
            BasicInformationCluster(deviceId, client).read()
        }

        val endpoints = catchAndThrow(deviceId, Stage.READ_DESCRIPTOR_CLUSTER) {
            DescriptorCluster(deviceId, ROOT_ENDPOINT, client).endpoints()
        }

        return Device(
            deviceId = deviceId,
            dateCommissioned = Clock.System.now().toEpochMilliseconds(),
            deviceType = endpoints.deviceType(),
            name = namesFromCommissioning.remove(deviceId),
            basicInformation = basicInfo,
            endpoints = endpoints,
        )
    }

    private suspend fun <T> catchAndThrow(
        deviceId: DeviceId,
        stage: Stage,
        block: suspend () -> T,
    ): T = try {
        block()
    } catch (c: CancellationException) {
        throw c
    } catch (t: Throwable) {
        throw CommissioningException(
            deviceId = deviceId,
            stage = stage,
            errorCode = t.toMatterErrorCode(),
            displayMessage = t.message ?: "",
        )
    }
}
