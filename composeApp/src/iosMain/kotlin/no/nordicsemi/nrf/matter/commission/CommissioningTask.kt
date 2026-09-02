package no.nordicsemi.nrf.matter.commission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.cancellation.CancellationException
import no.nordicsemi.nrf.matter.MatterCommissioner
import no.nordicsemi.nrf.matter.api.Fabric
import no.nordicsemi.nrf.matter.api.iosMatterPlatform
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId

private const val TAG = "Commissioning"

@Composable
actual fun rememberCommissioningTask(
    fabric: Fabric,
    onSuccess: suspend (DeviceId) -> Unit,
    onError: (CommissioningException) -> Unit,
): CommissioningTask {
    val commissioner = remember { iosMatterPlatform.matterCommissioner }
    val scope = rememberCoroutineScope()
    val currentOnSuccess by rememberUpdatedState(onSuccess)
    val currentOnError by rememberUpdatedState(onError)

    return remember(fabric) {
        IosCommissioningTask(
            fabric = fabric,
            commissioner = commissioner,
            scope = scope,
            onSuccess = { currentOnSuccess(it) },
            onError = { currentOnError(it) },
        )
    }
}

private class IosCommissioningTask(
    private val fabric: Fabric,
    private val commissioner: MatterCommissioner,
    private val scope: CoroutineScope,
    private val onSuccess: suspend (DeviceId) -> Unit,
    private val onError: (CommissioningException) -> Unit,
) : CommissioningTask {

    // Only one add-device flow can run at a time: the node id is reserved before the flow starts,
    // and a second flow would pair its device under the same id.
    private val mutex = Mutex()

    override fun startCommissioning() {
        if (mutex.isLocked) return

        scope.launch {
            mutex.withLock {
                val deviceId = fabric.nextDeviceId()
                NordicLogger.debug("iOS commissioning has started!", tag = TAG)
                NordicLogger.debug("New device id: $deviceId", tag = TAG)

                try {
                    commissioner.commission(deviceId)
                    onSuccess(deviceId)
                } catch (c: CancellationException) {
                    throw c
                } catch (t: Throwable) {
                    NordicLogger.error("Commissioning failed", t, tag = TAG)
                    onError(
                        t as? CommissioningException
                            ?: CommissioningException.unknown(Stage.COMMISSIONING)
                    )
                }
            }
        }
    }
}
