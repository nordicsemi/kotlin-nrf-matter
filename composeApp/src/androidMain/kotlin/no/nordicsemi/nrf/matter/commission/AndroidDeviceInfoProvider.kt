package no.nordicsemi.nrf.matter.commission

import chip.devicecontroller.ChipDeviceControllerException
import no.nordicsemi.nrf.matter.chip.ClustersHelper
import no.nordicsemi.nrf.matter.chip.MatterBasicInfoProvider
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceType
import kotlin.time.Clock

/**
 * Reads a commissioned device through the CHIP device controller.
 */
internal class AndroidDeviceInfoProvider(
    private val basicInfoProvider: MatterBasicInfoProvider,
    private val clustersHelper: ClustersHelper,
) : DeviceInfoProvider {

    private val namesFromCommissioning = mutableMapOf<DeviceId, String>()

    /**
     * Records the name the Google Home flow gave the device.
     *
     * The name is chosen by the user during that flow and is not an attribute of the device, so it
     * cannot be read back in [readDevice] - it has to be carried over from the commissioning
     * result.
     */
    fun rememberName(deviceId: DeviceId, name: String?) {
        name?.let { namesFromCommissioning[deviceId] = it }
    }

    override suspend fun readDevice(deviceId: DeviceId): Device {
        val basicInfo = catchAndThrow(deviceId, Stage.READ_BASIC_INFORMATION) {
            basicInfoProvider.fetchBasicInfo(deviceId)
        }

        val deviceMatterInfoList = catchAndThrow(deviceId, Stage.READ_DESCRIPTOR_CLUSTER) {
            clustersHelper.fetchDeviceMatterInfo(deviceId)
        }

        val deviceType = deviceMatterInfoList
            // Ignore the first endpoint because this is the root node.
            .filter { it.endpoint != 0 }
            .flatMap { it.types }
            .map { DeviceType.parse(it) }

        return Device(
            vendorName = basicInfo.vendorName,
            productName = basicInfo.productName,
            dateCommissioned = Clock.System.now()
                .toEpochMilliseconds(), // Date when the device was commissioned.
            vendorId = basicInfo.vendorId.toString(),
            productId = basicInfo.productId.toString(),
            deviceType = deviceType.firstOrNull() ?: DeviceType.UNSUPPORTED,
            deviceId = deviceId,
            name = namesFromCommissioning.remove(deviceId),
            uniqueId = basicInfo.uniqueId.toString(),
            softwareVersion = basicInfo.softwareVersion,
            serialNumer = basicInfo.serialNumber,
            specificationVersion = basicInfo.specificationVersion,
            deviceMatterInfo = deviceMatterInfoList,
        )
    }

    private suspend fun <T> catchAndThrow(
        deviceId: DeviceId,
        stage: Stage,
        block: suspend () -> T
    ): T {
        try {
            return block()
        } catch (t: ChipDeviceControllerException) {
            throw CommissioningException(
                deviceId,
                stage,
                t.errorCode.toInt(),
                t.message ?: ""
            )
        } catch (t: Throwable) {
            throw CommissioningException(
                deviceId,
                stage,
                null,
                t.message ?: ""
            )
        }
    }
}
