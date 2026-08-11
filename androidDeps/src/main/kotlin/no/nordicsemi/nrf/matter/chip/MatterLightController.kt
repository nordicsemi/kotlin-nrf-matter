package no.nordicsemi.nrf.matter.chip

import chip.devicecontroller.ChipClusters
import chip.devicecontroller.ReportCallback
import chip.devicecontroller.model.ChipAttributePath
import chip.devicecontroller.model.ChipEventPath
import chip.devicecontroller.model.NodeState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import no.nordicsemi.nrf.matter.controller.MatterLightController
import no.nordicsemi.nrf.matter.domain.DeviceOfflineException
import no.nordicsemi.nrf.matter.domain.OperationResult
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId
import kotlin.coroutines.resumeWithException

/*
 * Copyright (c) 2025, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * Controls and observes the state of a Matter light endpoint via the On/Off and Level Control
 * clusters.
 *
 * All commands are sent over an existing CASE session; callers are responsible for ensuring the
 * device has already been commissioned and is reachable through [chipClient].
 *
 * @property chipClient the underlying Matter stack used to resolve device pointers and send/subscribe
 * to cluster attributes.
 */
class MatterLightControllerImpl(
    private val chipClient: ChipClient,
) : MatterLightController {

    override suspend fun setBrightnessLevel(
        deviceId: DeviceId,
        brightnessLevel: Int,
        endpoint: Int
    ) {
        val connectedDevicePtr = connectedDevicePointerOrNull(deviceId)
            ?: throw DeviceOfflineException(deviceId)
        awaitClusterCallback { callback ->
            getLevelControlClusterForDevice(connectedDevicePtr, endpoint)
                .moveToLevelWithOnOff(
                    callback,
                    brightnessLevel,
                    0, // transitionTime (0 = instantaneous)
                    0, // optionsMask
                    0  // optionsOverride
                )
        }
    }

    /**
     * Turns the light on or off via the On/Off cluster.
     *
     * @param deviceId the commissioned device to control.
     * @param isOn `true` to send the On command, `false` to send the Off command.
     * @param endpoint the Matter endpoint exposing the On/Off cluster.
     * @throws DeviceOfflineException if the device is unreachable and no connected device pointer
     * can be resolved.
     * @throws Exception if the underlying cluster command fails (e.g. command rejected).
     */
    override suspend fun setDeviceOnOff(deviceId: DeviceId, isOn: Boolean, endpoint: Int) {
        val connectedDevicePtr = connectedDevicePointerOrNull(deviceId)
            ?: throw DeviceOfflineException(deviceId)
        val cluster = getOnOffClusterForDevice(connectedDevicePtr, endpoint)
        awaitClusterCallback { callback ->
            if (isOn) cluster.on(callback) else cluster.off(callback)
        }
    }

    override suspend fun observeLightState(
        deviceId: DeviceId,
        endpoint: Int
    ): Flow<OperationResult<Boolean>> =
        observeNodeState(deviceId, endpoint).mapSuccess { nodeState -> readOnOff(nodeState, endpoint) }

    override suspend fun observeBrightnessState(
        deviceId: DeviceId,
        endpoint: Int
    ): Flow<OperationResult<Float>> =
        observeNodeState(deviceId, endpoint).mapSuccess { nodeState -> readBrightness(nodeState, endpoint) }

    /**
     * Maps the [OperationResult.data] of a successful result, dropping reports that don't carry a
     * value for the attribute this flow cares about. An [OperationResult.Error] always passes
     * through unchanged so the caller never misses a report failure.
     */
    private fun <T, R> Flow<OperationResult<T>>.mapSuccess(extract: (T) -> R?): Flow<OperationResult<R>> =
        transform { result ->
            when (result) {
                is OperationResult.Success -> extract(result.data)?.let { emit(OperationResult.Success(it)) }
                is OperationResult.Error -> emit(OperationResult.Error(result.t))
            }
        }

    private fun readOnOff(nodeState: NodeState, endpoint: Int): Boolean? {
        val rawValue = nodeState.getEndpointState(endpoint)
            ?.getClusterState(ON_OFF_CLUSTER_ID)
            ?.getAttributeState(ON_OFF_ATTRIBUTE_ID)
            ?.value
        return (rawValue as? Boolean)?.also {
            NordicLogger.info("Received On/Off report: isLedOn=$it", tag = TAG)
        }
    }

    private fun readBrightness(nodeState: NodeState, endpoint: Int): Float? {
        val rawValue = nodeState.getEndpointState(endpoint)
            ?.getClusterState(LEVEL_CONTROL_CLUSTER_ID)
            ?.getAttributeState(CURRENT_LEVEL_ATTRIBUTE_ID)
            ?.value
        return (rawValue as? Number)?.let { level ->
            ((level.toFloat() - MIN_LEVEL) / LEVEL_RANGE).coerceIn(0f, 1f)
        }?.also {
            NordicLogger.info("Received Brightness report: brightnessPercentage=$it", tag = TAG)
        }
    }

    /**
     * Runs a cluster command that reports completion via [ChipClusters.DefaultClusterCallback],
     * suspending until it succeeds or fails.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun awaitClusterCallback(
        command: (ChipClusters.DefaultClusterCallback) -> Unit
    ) = suspendCancellableCoroutine { continuation ->
        command(object : ChipClusters.DefaultClusterCallback {
            override fun onSuccess() {
                continuation.resume(Unit, onCancellation = {})
            }

            override fun onError(ex: Exception) {
                continuation.resumeWithException(ex)
            }
        })
    }

    private fun getLevelControlClusterForDevice(
        devicePtr: Long,
        endpoint: Int
    ): ChipClusters.LevelControlCluster {
        return ChipClusters.LevelControlCluster(devicePtr, endpoint)
    }

    private fun getOnOffClusterForDevice(
        devicePtr: Long,
        endpoint: Int
    ): ChipClusters.OnOffCluster {
        return ChipClusters.OnOffCluster(devicePtr, endpoint)
    }

    /**
     * Emits every attribute report received for [deviceId]/[endpoint] as a raw [NodeState].
     *
     * On/Off and CurrentLevel live on the same endpoint for a Dimmable Light, so a single native
     * subscription covering both attributes (set up once per [deviceId]/[endpoint] by [nodeStateReports])
     * is shared between [observeLightState] and [observeBrightnessState] instead of each opening its own
     * competing subscription. A report error is forwarded as an [OperationResult.Error] rather than
     * closing the flow, since the underlying native subscription keeps running and can still recover.
     */
    private fun observeNodeState(deviceId: DeviceId, endpoint: Int): Flow<OperationResult<NodeState>> =
        callbackFlow {
            val reports = nodeStateReports(deviceId, endpoint)
            val job = launch { reports.collect { trySend(it) } }
            awaitClose { job.cancel() }
        }

    /**
     * Returns the shared report flow for [deviceId]/[endpoint], establishing the native subscription
     * covering both the On/Off and Level Control attributes on first access; later callers just attach
     * to the already-running subscription.
     */
    private suspend fun nodeStateReports(deviceId: DeviceId, endpoint: Int): Flow<OperationResult<NodeState>> {
        var isNew = false
        val subscription = synchronized(subscriptionsLock) {
            subscriptions.getOrPut(deviceId to endpoint) { isNew = true; NodeStateSubscription() }
        }
        if (isNew) {
            try {
                val devicePtr = connectedDevicePointerOrNull(deviceId)
                    ?: throw DeviceOfflineException(deviceId)
                chipClient.subscribeAttribute(
                    reportCallback = object : ReportCallback {
                        override fun onError(
                            attributePath: ChipAttributePath?,
                            eventPath: ChipEventPath?,
                            e: Exception
                        ) {
                            NordicLogger.error(
                                "Error receiving report from DK for path: $attributePath",
                                e,
                                tag = TAG
                            )
                            subscription.reports.tryEmit(OperationResult.Error(e))
                        }

                        override fun onReport(nodeState: NodeState) {
                            subscription.reports.tryEmit(OperationResult.Success(nodeState))
                        }
                    },
                    devicePtr = devicePtr,
                    attributePaths = listOf(
                        ChipAttributePath.newInstance(
                            endpoint,
                            ON_OFF_CLUSTER_ID,
                            ON_OFF_ATTRIBUTE_ID
                        ),
                        ChipAttributePath.newInstance(
                            endpoint,
                            LEVEL_CONTROL_CLUSTER_ID,
                            CURRENT_LEVEL_ATTRIBUTE_ID
                        ),
                    ),
                    minIntervalS = 0,    // Report changes instantly
                    maxIntervalS = 10,   // Heartbeat check every 10 seconds
                    timeoutMs = 10000    // 10 second network timeout for establishing the session
                )
            } catch (e: Exception) {
                NordicLogger.error("Failed to setup wrapper subscription", e, tag = TAG)
                subscription.establishFailure = e
            }
        }
        subscription.establishFailure?.let { throw it }
        return subscription.reports
    }

    private class NodeStateSubscription {
        val reports = MutableSharedFlow<OperationResult<NodeState>>(
            extraBufferCapacity = 64,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )

        @Volatile
        var establishFailure: Exception? = null
    }

    private val subscriptionsLock = Any()
    private val subscriptions = mutableMapOf<Pair<DeviceId, Int>, NodeStateSubscription>()

    /**
     * Resolves the connected device pointer for [deviceId], returning `null` when the device is
     * unreachable so callers can treat "device offline" as a distinct, non-throwing outcome.
     */
    private suspend fun connectedDevicePointerOrNull(deviceId: DeviceId): Long? =
        try {
            chipClient.getConnectedDevicePointer(deviceId.longValue)
        } catch (e: Exception) {
            NordicLogger.error(
                "Unable to resolve connected device pointer for ${deviceId.longValue}; device is offline",
                e,
                tag = TAG
            )
            null
        }

    companion object {
        private const val ON_OFF_CLUSTER_ID = 6L
        private const val ON_OFF_ATTRIBUTE_ID = 0L
        private const val LEVEL_CONTROL_CLUSTER_ID = 8L
        private const val CURRENT_LEVEL_ATTRIBUTE_ID = 0L
        private const val MIN_LEVEL = 1f
        private const val LEVEL_RANGE = 253f

        private val TAG: String
            get() = "MatterLightController"
    }
}