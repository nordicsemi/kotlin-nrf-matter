package no.nordicsemi.nrf.matter.iosdeps

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSNumber
import no.nordicsemi.nrf.matter.commission.CommissioningException
import no.nordicsemi.nrf.matter.commission.Stage
import no.nordicsemi.nrf.matter.domain.ManufacturerSpecificData
import no.nordicsemi.nrf.matter.logger.LogEntity
import no.nordicsemi.nrf.matter.logger.LogLevel
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.DeviceMatterInfo
import no.nordicsemi.nrf.matter.model.DeviceType
import no.nordicsemi.nrf.matter.model.toDeviceId

/**
 * Conversions between `:core`'s model types and iosDeps's plain-data mirrors.
 *
 * iosDeps's public API never references a `:core`-compiled type (see the module-level comment
 * in composeApp/build.gradle.kts for why), so every model that crosses the cinterop boundary is
 * converted explicitly here rather than shared by reference.
 */

@OptIn(ExperimentalForeignApi::class)
internal fun ManufacturerSpecificData.toSwift(): SwiftManufacturerSpecificData =
    SwiftManufacturerSpecificData(name = name, led = led, button = button)

@OptIn(ExperimentalForeignApi::class)
internal fun SwiftManufacturerSpecificData.toCore(): ManufacturerSpecificData =
    ManufacturerSpecificData(name = name, led = led, button = button)

@OptIn(ExperimentalForeignApi::class)
internal fun deviceTypeFromRaw(raw: Int): DeviceType = DeviceType.entries.getOrElse(raw) { DeviceType.UNSUPPORTED }

@OptIn(ExperimentalForeignApi::class)
internal fun SwiftDeviceMatterInfo.toCore(): DeviceMatterInfo = DeviceMatterInfo(
    endpoint = endpoint,
    types = types.map { (it as NSNumber).longValue },
    serverClusters = serverClusters.map { (it as NSNumber).longValue },
    clientClusters = clientClusters.map { (it as NSNumber).longValue },
    manufacturerSpecificData = manufacturerSpecificData?.toCore(),
)

@OptIn(ExperimentalForeignApi::class)
internal fun SwiftDevice.toCore(): Device = Device(
    deviceId = deviceId.toDeviceId(),
    dateCommissioned = dateCommissioned?.longValue,
    vendorId = vendorId,
    productId = productId,
    deviceType = deviceTypeFromRaw(deviceType),
    name = name,
    productName = productName,
    vendorName = vendorName,
    uniqueId = uniqueId,
    softwareVersion = softwareVersion,
    specificationVersion = specificationVersion?.longValue,
    serialNumer = serialNumber,
    deviceMatterInfo = deviceMatterInfo.map { (it as SwiftDeviceMatterInfo).toCore() },
)

@OptIn(ExperimentalForeignApi::class)
internal fun stageFromRaw(raw: Int): Stage = Stage.entries.getOrElse(raw) { Stage.COMMISSIONING }

@OptIn(ExperimentalForeignApi::class)
internal fun SwiftCommissioningError.toCore(deviceId: DeviceId): CommissioningException = CommissioningException(
    deviceId = deviceId,
    stage = stageFromRaw(stage),
    errorCode = errorCode?.intValue,
    displayMessage = displayMessage,
    fabricId = fabricId,
)

@OptIn(ExperimentalForeignApi::class)
internal fun logLevelFromRaw(raw: Int): LogLevel = LogLevel.entries.getOrElse(raw) { LogLevel.INFO }

@OptIn(ExperimentalForeignApi::class)
internal fun SwiftLogEntity.toCore(): LogEntity = LogEntity(
    date = date,
    level = logLevelFromRaw(level),
    tag = tag,
    message = message,
)
