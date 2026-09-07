package no.nordicsemi.nrf.matter.commission

import no.nordicsemi.nrf.matter.cluster.BasicInformationCluster
import no.nordicsemi.nrf.matter.cluster.DescriptorCluster
import no.nordicsemi.nrf.matter.cluster.ManufacturerSpecCluster
import no.nordicsemi.nrf.matter.cluster.ManufacturerSpecClusterInfo
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.Endpoint
import no.nordicsemi.nrf.matter.model.ManufacturerSpecificData
import no.nordicsemi.nrf.matter.model.ROOT_ENDPOINT
import no.nordicsemi.nrf.matter.model.deviceType
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock

/**
 * Reads a commissioned device through the Matter interaction model.
 *
 * Everything comes from cluster reads over [MatterClient] - Basic Information for what the device
 * is, Descriptor for what each of its endpoints implements - so there is one implementation for
 * both platforms rather than one per platform Matter stack.
 */
internal class ClusterDeviceInfoProvider(
    private val client: MatterClient,
) : DeviceInfoProvider {

    private val namesFromCommissioning = mutableMapOf<DeviceId, String>()

    /**
     * Records the name the platform commissioning flow gave the device.
     *
     * The name is chosen by the user during that flow and is not an attribute of the device, so it
     * cannot be read back in [readDevice] - it has to be carried over from the commissioning
     * result. Only Android's flow reports one.
     */
    fun rememberName(deviceId: DeviceId, name: String?) {
        name?.let { namesFromCommissioning[deviceId] = it }
    }

    override suspend fun readDevice(deviceId: DeviceId): Device {
        val basicInfo = catchAndThrow(deviceId, Stage.READ_BASIC_INFORMATION) {
            BasicInformationCluster(deviceId, client).read()
        }

        val endpoints = catchAndThrow(deviceId, Stage.READ_DESCRIPTOR_CLUSTER) {
            readEndpoints(deviceId)
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

    /**
     * Walks the device from the root node down, one [Endpoint] each.
     *
     * The Descriptor cluster describes an endpoint, so anything that is not part of it - the
     * manufacturer specific data - is read here and folded in afterwards.
     */
    private suspend fun readEndpoints(deviceId: DeviceId): List<Endpoint> =
        DescriptorCluster(deviceId, ROOT_ENDPOINT, client)
            .endpoints()
            .map { endpoint ->
                endpoint.copy(
                    manufacturerSpecificData = readManufacturerSpecificData(
                        deviceId = deviceId,
                        endpoint = endpoint.id,
                        serverClusters = endpoint.serverClusters,
                    )
                )
            }

    /**
     * The manufacturer specific data of an endpoint that carries Nordic's cluster, or `null`.
     *
     * Optional by nature, so a failed read is logged and dropped rather than failing the read of
     * the device.
     */
    private suspend fun readManufacturerSpecificData(
        deviceId: DeviceId,
        endpoint: Int,
        serverClusters: List<Long>,
    ): ManufacturerSpecificData? {
        if (ManufacturerSpecClusterInfo.ID !in serverClusters) return null

        val cluster = ManufacturerSpecCluster(deviceId, endpoint, client)

        return try {
            ManufacturerSpecificData(
                name = cluster.readName(),
                led = cluster.readLed(),
                button = cluster.readButton(),
            )
        } catch (c: CancellationException) {
            throw c
        } catch (t: Throwable) {
            NordicLogger.error(
                "Manufacturer specific data of device $deviceId could not be read",
                t,
                tag = TAG,
            )
            null
        }
    }

    /**
     * Reports a failed read as a [CommissioningException] naming the stage it failed at, which is
     * what the commissioning screens show.
     */
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
            errorCode = t.matterErrorCode(),
            displayMessage = t.message ?: "",
        )
    }

    companion object {
        private const val TAG = "DeviceInfo"
    }
}
