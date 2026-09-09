@file:OptIn(ExperimentalForeignApi::class)

package no.nordicsemi.nrf.matter.api

import iosMatter.MatterCommissioner
import iosMatter.SharedConsts
import iosMatter.SharedStorage
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.adapters.IOSException
import no.nordicsemi.nrf.matter.adapters.IOSLoggerImpl
import no.nordicsemi.nrf.matter.adapters.handleResult
import no.nordicsemi.nrf.matter.commission.CommissioningException
import no.nordicsemi.nrf.matter.logger.NordicLogger

private const val TAG = "AppExtension"

private val appExtensionCommissioner = MatterCommissioner()
private val appExtensionStorage = SharedStorage()

fun NordicMatters.initializeAppExtension() {
    NordicLogger.setLogger(IOSLoggerImpl())
}

fun NordicMatters.appExtensionRooms(): List<String> {
    val names = appExtensionStorage.getStringArrayWithKey(SharedConsts.roomsKey)
        ?.filterIsInstance<String>()
        ?: commissioningRooms

    NordicLogger.info("Offering ${names.size} rooms.", tag = TAG)
    return names
}

fun NordicMatters.onAppExtensionThreadNetworksDetected(names: List<String>) {
    NordicLogger.info("Selecting Thread network from ${names.size} scan results.", tag = TAG)

    names.forEach {
        NordicLogger.info("Detected thread network: $it.", tag = TAG)
    }
}

@Throws(
    CommissioningException::class,
    IOSException::class,
    IllegalStateException::class,
)
suspend fun Fabric.commissionAppExtensionDevice(payload: String) {
    val nodeId = appExtensionStorage.getNumberWithKey(SharedConsts.nodeIdKey)
        ?: error("No node ID found in shared storage.")

    NordicLogger.info("Commissioning node $nodeId onto $this.", tag = TAG)
    suspendCancellableCoroutine<Unit> { continuation ->
        appExtensionCommissioner.commissionWithPayload(payload, nodeId) { error ->
            continuation.handleResult(error)
        }
    }
}

fun Fabric.configureAppExtensionDevice(name: String) {
    NordicLogger.info("Device configured as \"$name\". Reporting success.", tag = TAG)

    appExtensionStorage.storeStringWithKey(SharedConsts.deviceNameKey, name)
    appExtensionStorage.storeBoolWithKey(SharedConsts.resultKey, true)
    appExtensionCommissioner.releaseCommissioner()
}
