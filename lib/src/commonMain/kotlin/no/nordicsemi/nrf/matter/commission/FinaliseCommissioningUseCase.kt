package no.nordicsemi.nrf.matter.commission

import no.nordicsemi.nrf.matter.cluster.BasicInformationCluster
import no.nordicsemi.nrf.matter.cluster.DescriptorCluster
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.ROOT_ENDPOINT
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock

internal class FinaliseCommissioningUseCase(
    private val client: MatterClient,
    private val errorCodeOf: (Throwable) -> Int?,
) {

    private val namesFromCommissioning = mutableMapOf<DeviceId, String>()

    fun rememberName(deviceId: DeviceId, name: String?) {
        name?.let { namesFromCommissioning[deviceId] = it }
    }

    suspend fun readDevice(deviceId: DeviceId): Device {
        NordicLogger.debug("--- Reading device info ---")

        val basicInfo = catchAndThrow(deviceId, Stage.READ_BASIC_INFORMATION) {
            BasicInformationCluster(deviceId, client).read()
        }

        val endpoints = catchAndThrow(deviceId, Stage.READ_DESCRIPTOR_CLUSTER) {
            DescriptorCluster(deviceId, ROOT_ENDPOINT, client).endpoints()
        }

        NordicLogger.debug("--- Successfully read device info ---")

        return Device(
            deviceId = deviceId,
            dateCommissioned = Clock.System.now().toEpochMilliseconds(),
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
            errorCode = errorCodeOf(t),
            displayMessage = t.message ?: "",
        )
    }
}
