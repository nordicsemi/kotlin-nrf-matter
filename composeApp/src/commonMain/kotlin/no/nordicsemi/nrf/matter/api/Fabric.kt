package no.nordicsemi.nrf.matter.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import no.nordicsemi.nrf.matter.binding.isBindingCapable
import no.nordicsemi.nrf.matter.binding.isBindingSource
import no.nordicsemi.nrf.matter.commission.DecommissionState
import no.nordicsemi.nrf.matter.domain.BindingState
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceBinding
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceState

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
    private val decommissionUseCases get() = dependencies.decommissionUseCases
    private val bindingRepository get() = dependencies.bindingRepository
    private val bindDevicesUseCase get() = dependencies.bindDevicesUseCase

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
     * Unlinking an offline device can take a while and then fails, which is what
     * [forceRemoveDevice] is for.
     */
    fun decommissionDevice(deviceId: DeviceId): Flow<DecommissionState> =
        decommissionUseCases.decommissionDevice(deviceId)

    /**
     * Drops the device from the store without unlinking the fabric at the device itself.
     */
    fun forceRemoveDevice(deviceId: DeviceId): Flow<DecommissionState> =
        decommissionUseCases.forceRemoveDevice(deviceId)

    /** The bindings written from this fabric, re-emitted whenever one is added or removed. */
    val bindings: Flow<List<DeviceBinding>>
        get() = bindingRepository.getAllBinding()

    /**
     * The devices that can be the client side of a binding - the ones worth offering as a source.
     */
    suspend fun getBindingSourceDevices(): List<Device> {
        return getDevices().filter { it.isBindingSource() }
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

            devices.filter { it.isBindingCapable() && it.deviceId !in boundTargets }
        }
    }

    /**
     * Writes a binding into [sourceDeviceId]'s Binding cluster so that it drives
     * [targetDeviceId]'s OnOff cluster directly, and records it in [bindings].
     *
     * The returned flow reports the progress of the write; [bindingLogs] carries what the Matter
     * stack logged while it ran.
     */
    fun bindDevices(
        sourceDeviceId: DeviceId,
        targetDeviceId: DeviceId,
    ): Flow<BindingState> = bindDevicesUseCase(
        switchNodeId = sourceDeviceId,
        lightNodeId = targetDeviceId,
    )

    /** What the platform Matter stack logs while a binding is being written. */
    val bindingLogs: Flow<String>
        get() = bindDevicesUseCase.bindingLogs

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
