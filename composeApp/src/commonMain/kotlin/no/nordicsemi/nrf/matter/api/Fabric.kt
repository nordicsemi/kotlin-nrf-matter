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

class Fabric internal constructor(
    val id: FabricId,
    private val dependencies: MatterDependencies,
) {

    private val devicesRepository get() = dependencies.devicesRepository
    private val devicesStateRepository get() = dependencies.devicesStateRepository
    private val deviceInfoProvider get() = dependencies.finaliseCommissioningUseCase
    private val matterDecommissioner get() = dependencies.matterDecommissioner
    private val bindingRepository get() = dependencies.bindingRepository
    private val bindingController get() = dependencies.bindingController
    private val bindingLogsProvider get() = dependencies.bindingLogsProvider

    val devices: Flow<List<Device>>
        get() = devicesRepository.devicesFlow.map { it.devicesList }

    val deviceStates: Flow<List<DeviceState>>
        get() = devicesStateRepository.devicesStateFlow.map { it.devicesStateList }

    suspend fun getDevices(): List<Device> {
        return devicesRepository.getAllDevices().devicesList
    }

    suspend fun commissionDevice(deviceId: DeviceId): Device {
        val device = deviceInfoProvider.readDevice(deviceId)

        devicesRepository.addDevice(device)
        devicesStateRepository.addDeviceState(deviceId, isOnline = true, isOn = false)

        return device
    }

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

    suspend fun forceRemoveDevice(deviceId: DeviceId) = withContext(Dispatchers.IO) {
        forget(deviceId)
    }

    private suspend fun forget(deviceId: DeviceId) {
        devicesStateRepository.removeDevice(deviceId)
        devicesRepository.removeDevice(deviceId)
        bindingRepository.delete(deviceId)
    }

    val bindings: Flow<List<DeviceBinding>>
        get() = bindingRepository.getAllBinding()

    suspend fun getBindingSourceDevices(): List<Device> {
        return getDevices().filter { it.isBindingCapable() != null }
    }

    fun getEligibleTargetDevices(sourceDeviceId: DeviceId): Flow<List<Device>> {
        return combine(
            devices,
            bindingRepository.getTargetsForDevice(sourceDeviceId),
        ) { devices, bindings ->
            val boundTargets = bindings.map { it.targetNodeId }.toSet()

            devices.filter { it.isBindingSource() != null && it.deviceId !in boundTargets }
        }
    }

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

    val bindingLogs: Flow<String>
        get() = bindingLogsProvider.bindingLogs

    internal suspend fun nextDeviceId(): DeviceId =
        devicesRepository.incrementAndReturnLastDeviceId()

    override fun toString(): String = "Fabric(id=$id)"
}
