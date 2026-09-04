package no.nordicsemi.nrf.matter.commission

import no.nordicsemi.nrf.matter.cluster.BasicInformationCluster
import no.nordicsemi.nrf.matter.cluster.DescriptorCluster
import no.nordicsemi.nrf.matter.cluster.ManufacturerSpecCluster
import no.nordicsemi.nrf.matter.cluster.ManufacturerSpecClusterInfo
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceMatterInfo
import no.nordicsemi.nrf.matter.model.DeviceType
import no.nordicsemi.nrf.matter.model.ManufacturerSpecificData
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
            vendorId = basicInfo.vendorId?.toString(),
            vendorName = basicInfo.vendorName,
            productId = basicInfo.productId?.toString(),
            productName = basicInfo.productName,
            deviceType = endpoints.deviceType(),
            name = namesFromCommissioning.remove(deviceId),
            uniqueId = basicInfo.uniqueId,
            softwareVersion = basicInfo.softwareVersion,
            serialNumer = basicInfo.serialNumber,
            specificationVersion = basicInfo.specificationVersion,
            deviceMatterInfo = endpoints,
        )
    }

    /**
     * Walks the device from the root node down, one [DeviceMatterInfo] per endpoint.
     *
     * Endpoint 0 is the root node and is included, because the app reads Basic Information there;
     * [deviceType] is what skips it when deciding what the device *is*.
     */
    private suspend fun readEndpoints(deviceId: DeviceId): List<DeviceMatterInfo> {
        val collected = mutableListOf<DeviceMatterInfo>()
        readEndpoint(deviceId, endpoint = ROOT_ENDPOINT, into = collected)

        return collected
    }

    private suspend fun readEndpoint(
        deviceId: DeviceId,
        endpoint: Int,
        into: MutableList<DeviceMatterInfo>,
    ) {
        if (into.any { it.endpoint == endpoint }) return

        val descriptor = DescriptorCluster(deviceId, endpoint, client)

        val serverClusters = descriptor.serverClusters()
        val clientClusters = descriptor.clientClusters()
        val deviceTypes = descriptor.deviceTypes()
        val parts = descriptor.parts()

        into += DeviceMatterInfo(
            endpoint = endpoint,
            types = deviceTypes,
            serverClusters = serverClusters,
            clientClusters = clientClusters,
            manufacturerSpecificData = readManufacturerSpecificData(
                deviceId = deviceId,
                endpoint = endpoint,
                serverClusters = serverClusters,
            ),
        )

        parts.forEach { child ->
            // An endpoint listed in PartsList may not answer the Descriptor cluster itself, and one
            // unreadable endpoint should not cost us the rest of the device.
            try {
                readEndpoint(deviceId, child, into)
            } catch (c: CancellationException) {
                throw c
            } catch (t: Throwable) {
                NordicLogger.error(
                    "Endpoint $child of device $deviceId could not be read, skipping...",
                    t,
                    tag = TAG,
                )
            }
        }
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
     * What the device is, taken from the first endpoint that names a type the library knows.
     *
     * The root node is skipped: its device type describes the node, not what the device does.
     */
    private fun List<DeviceMatterInfo>.deviceType(): DeviceType =
        filter { it.endpoint != ROOT_ENDPOINT }
            .flatMap { it.types }
            .map { DeviceType.parse(it) }
            .firstOrNull { it != DeviceType.UNSUPPORTED }
            ?: DeviceType.UNSUPPORTED

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

        /** The root node, present on every device. */
        private const val ROOT_ENDPOINT = 0
    }
}
