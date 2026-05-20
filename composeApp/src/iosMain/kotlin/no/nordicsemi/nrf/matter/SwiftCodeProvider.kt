package no.nordicsemi.nrf.matter

import no.nordicsemi.nrf.matter.domain.ManufacturerSpecificData
import no.nordicsemi.nrf.matter.logger.PlatformLogger
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.GoogleHub

interface SwiftCodeProvider {

    fun getMatterCommissioner(): MatterCommissioner

    fun getMatterOnOffController(): MatterOnOffController

    fun getDecommissioner(): MatterDecommissioner

    fun getMatterBinder(): MatterBinder

    fun getMatterDoorController(): MatterDoorController

    fun getMatterOutletController(): MatterOutletController

    fun getMatterManufacturerCustomDataController(): MatterManufacturerCustomDataController

    fun getMatterClusterExtensionController(): MatterClusterExtensionController

    fun getLogger(): PlatformLogger

    fun getHubController(): GoogleHubController
}

interface MatterCommissioner {

    suspend fun startIosCommissioning(onError: () -> Unit): Device?
}

interface MatterDecommissioner {

    suspend fun decommission(deviceId: DeviceId)
}

interface MatterOnOffController {

    suspend fun setDeviceOnOff(
        deviceId: DeviceId,
        isOn: Boolean,
        endpoint: Int,
    )
}

interface MatterBinder {

    suspend fun bind(
        sourceNodeId: DeviceId,
        sourceEndpoint: Int,
        targetNodeId: DeviceId,
        targetEndpoint: Int,
        clusterId: Long
    )
}

interface MatterDoorController {

    suspend fun lockUnlockDoor(
        deviceId: DeviceId,
        isLocked: Boolean,
        endpoint: Int
    )
}

interface MatterOutletController {

    suspend fun handleOutlet(
        deviceId: DeviceId,
        isSwitchOn: Boolean,
        endpoint: Int
    )
}

interface MatterManufacturerCustomDataController {

    suspend fun setLed(
        deviceId: DeviceId,
        isOn: Boolean,
        endpoint: Int,
    )

    suspend fun getData(deviceId: DeviceId, endpoint: Int): ManufacturerSpecificData

    suspend fun subscribeToButtonChanges(
        deviceId: DeviceId,
        endpoint: Int,
        onUpdate: (Boolean) -> Unit
    )
}

interface MatterClusterExtensionController {

    suspend fun getRandomNumber(deviceId: DeviceId): Int

    suspend fun generateRandomNumber(deviceId: DeviceId): Int
}

interface GoogleHubController {

    suspend fun getHubs(): List<GoogleHub>

    suspend fun activateHub(hub: GoogleHub)
}
