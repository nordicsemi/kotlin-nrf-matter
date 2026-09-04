package no.nordicsemi.nrf.matter.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import no.nordicsemi.nrf.matter.binding.isBindingCapable
import no.nordicsemi.nrf.matter.binding.isBindingSource
import no.nordicsemi.nrf.matter.cluster.OnOffClusterInfo
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceBinding
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceState
import kotlin.coroutines.cancellation.CancellationException

/**
 * The devices this app has commissioned, the bindings between them, and the operations that change
 * either.
 *
 * Obtained from [NordicMatters], never constructed directly.
 */
class Fabric internal constructor(
    val id: FabricId,
    private val dependencies: MatterDependencies,
) {

    private val devicesRepository get() = dependencies.devicesRepository
    private val devicesStateRepository get() = dependencies.devicesStateRepository
    private val deviceInfoProvider get() = dependencies.deviceInfoProvider
    private val matterDecommissioner get() = dependencies.matterDecommissioner
    private val bindingRepository get() = dependencies.bindingRepository
    private val bindingController get() = dependencies.bindingController
    private val bindingLogsProvider get() = dependencies.bindingLogsProvider

    /** The commissioned devices, re-emitted whenever a device is added or removed. */
    val devices: Flow<List<Device>>
        get() = devicesRepository.devicesFlow.map { it.devicesList }

    /**
     * The last captured online and on/off state of the commissioned devices.
     *
     * A device only appears here once a state has been captured for it, so entries can be missing
     * for devices present in [devices].
     */
    val deviceStates: Flow<List<DeviceState>>
        get() = devicesStateRepository.devicesStateFlow.map { it.devicesStateList }

    /** A snapshot of [devices]. */
    suspend fun getDevices(): List<Device> {
        return devicesRepository.getAllDevices().devicesList
    }

    /**
     * Adds the device that the platform commissioning flow paired as [deviceId] to this fabric.
     *
     * Reads the device's Basic Information and Descriptor clusters - so it only succeeds while the
     * device is reachable - and stores what it read. Meant to be called from the `onSuccess`
     * callback of `rememberCommissioningTask`, which is what hands out [deviceId].
     *
     * @throws no.nordicsemi.nrf.matter.commission.CommissioningException if the device cannot be
     * read.
     */
    suspend fun commissionDevice(deviceId: DeviceId): Device {
        val device = deviceInfoProvider.readDevice(deviceId)

        devicesRepository.addDevice(device)
        devicesStateRepository.addDeviceState(deviceId, isOnline = true, isOn = false)

        return device
    }

    /**
     * Removes the device from this fabric: first the fabric is removed from the device, then the
     * device is dropped from the store along with its bindings.
     *
     * Unlinking an offline device can take a while and then throw, which is what
     * [forceRemoveDevice] is for. Nothing is forgotten when the unlink fails - the device stays in
     * [devices] so the app can offer the forced removal.
     *
     * @throws Exception whatever the platform Matter stack raises when the unlink fails.
     */
    suspend fun decommissionDevice(deviceId: DeviceId) = withContext(Dispatchers.IO) {
        try {
            matterDecommissioner.decommission(deviceId)
        } catch (c: CancellationException) {
            throw c
        } catch (e: Exception) {
            NordicLogger.error("Decommissioning failed: ${e.message}", e)
            throw e
        }

        forget(deviceId)
    }

    /**
     * Drops the device from the store without unlinking the fabric at the device itself.
     */
    suspend fun forceRemoveDevice(deviceId: DeviceId) = withContext(Dispatchers.IO) {
        forget(deviceId)
    }

    /** Drops everything this fabric remembers about the device. */
    private suspend fun forget(deviceId: DeviceId) {
        devicesStateRepository.removeDevice(deviceId)
        devicesRepository.removeDevice(deviceId)
        // The bindings too: a device that has left the fabric can no longer drive anything.
        bindingRepository.delete(deviceId)
    }

    /** The bindings written from this fabric, re-emitted whenever one is added or removed. */
    val bindings: Flow<List<DeviceBinding>>
        get() = bindingRepository.getAllBinding()

    /**
     * The devices that can be the client side of a binding - the ones worth offering as a source.
     */
    suspend fun getBindingSourceDevices(): List<Device> {
        return getDevices().filter { it.isBindingCapable() != null }
    }

    /**
     * The devices [sourceDeviceId] can still be bound to: the ones that implement OnOff as a
     * server and are not already bound to it.
     *
     * Re-emitted as bindings are written and as devices are commissioned or removed.
     */
    fun getEligibleTargetDevices(sourceDeviceId: DeviceId): Flow<List<Device>> {
        return combine(
            devices,
            bindingRepository.getTargetsForDevice(sourceDeviceId),
        ) { devices, bindings ->
            val boundTargets = bindings.map { it.targetNodeId }.toSet()

            devices.filter { it.isBindingSource() != null && it.deviceId !in boundTargets }
        }
    }

    /**
     * Writes a binding into [sourceDeviceId]'s Binding cluster so that it drives
     * [targetDeviceId]'s OnOff cluster directly, and records it in [bindings].
     *
     * The endpoints come from what each device reported in its Descriptor cluster when it was
     * commissioned: the source is bound on the endpoint that holds OnOff as a client, the target
     * on the endpoint that serves it. Neither side is assumed to be endpoint 1.
     *
     * Suspends until the write completes and returns the binding it recorded. [bindingLogs]
     * carries what the Matter stack logged while it ran; how that progress is presented is the
     * caller's to decide.
     *
     * @throws IllegalStateException if either device is unknown to this fabric, or if it has no
     * endpoint that can take its side of the binding.
     * @throws Exception whatever the platform Matter stack raises when the write fails.
     */
    suspend fun bindDevices(
        sourceDeviceId: DeviceId,
        targetDeviceId: DeviceId,
    ): DeviceBinding = withContext(Dispatchers.IO) {
        val devices = getDevices()
        val source = devices.firstOrNull { it.deviceId == sourceDeviceId }
            ?: error("Device $sourceDeviceId is not commissioned into this fabric.")
        val target = devices.firstOrNull { it.deviceId == targetDeviceId }
            ?: error("Device $targetDeviceId is not commissioned into this fabric.")

        val sourceEndpoint = source.isBindingCapable()
            ?: error("Device $sourceDeviceId has no endpoint holding OnOff as a client.")
        val targetEndpoint = target.isBindingSource()
            ?: error("Device $targetDeviceId has no endpoint serving OnOff.")

        try {
            bindingController.bind(
                sourceNodeId = sourceDeviceId,
                sourceEndpoint = sourceEndpoint,
                targetNodeId = targetDeviceId,
                targetEndpoint = targetEndpoint,
                clusterId = OnOffClusterInfo.ID,
            )
        } catch (c: CancellationException) {
            throw c
        } catch (e: Exception) {
            NordicLogger.error("Binding failed: ${e.message}", e)
            throw e
        }

        val binding = DeviceBinding(
            id = "${sourceDeviceId.longValue}_${targetDeviceId.longValue}",
            sourceNodeId = sourceDeviceId,
            sourceEndpoint = sourceEndpoint,
            targetNodeId = targetDeviceId,
            targetEndpoint = targetEndpoint,
            clusterId = OnOffClusterInfo.ID,
        )
        bindingRepository.save(binding)

        binding
    }

    /** What the platform Matter stack logs while a binding is being written. */
    val bindingLogs: Flow<String>
        get() = bindingLogsProvider.bindingLogs

    /**
     * Reserves the node id for the next device commissioned into this fabric.
     *
     * Reserved before the platform flow starts, because the commissioning callbacks - the Android
     * commissioning service, the iOS app extension - have to know which node id they are pairing.
     */
    internal suspend fun nextDeviceId(): DeviceId =
        devicesRepository.incrementAndReturnLastDeviceId()

    override fun toString(): String = "Fabric(id=$id)"
}
