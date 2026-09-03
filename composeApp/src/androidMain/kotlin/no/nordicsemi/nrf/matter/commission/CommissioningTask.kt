package no.nordicsemi.nrf.matter.commission

import android.content.ComponentName
import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.home.matter.Matter
import com.google.android.gms.home.matter.commissioning.CommissioningRequest
import com.google.android.gms.home.matter.commissioning.CommissioningResult
import com.google.android.gms.home.matter.commissioning.MatterCommissioningApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.api.Fabric
import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.toDeviceId
import no.nordicsemi.nrf.matter.service.AppCommissioningService
import kotlin.coroutines.cancellation.CancellationException

private const val TAG = "Commissioning"

@Composable
actual fun rememberCommissioningTask(
    fabric: Fabric,
    onSuccess: suspend (DeviceId) -> Unit,
    onError: (CommissioningException) -> Unit,
): CommissioningTask {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val deviceInfoProvider = remember {
        NordicMatters.matterDependencies.platformDependencies.androidDeviceInfoProvider
    }
    val currentOnSuccess by rememberUpdatedState(onSuccess)
    val currentOnError by rememberUpdatedState(onError)

    // The node id is reserved before the flow starts, so it is the only thing known about the
    // device while the Google Home flow is running - and the only thing a failure can be reported
    // against.
    val reservedDeviceId = remember { mutableStateOf<DeviceId?>(null) }
    val isRunning = remember { mutableStateOf(false) }

    val commissionDeviceLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            scope.launch {
                isRunning.value = false
                try {
                    val commissioningResult =
                        CommissioningResult.fromIntentSenderResult(result.resultCode, result.data)
                    val deviceId = commissioningResult.token?.toDeviceId()
                        ?: error("Token is missing.")

                    deviceInfoProvider.rememberName(deviceId, commissioningResult.deviceName)
                    currentOnSuccess(deviceId)
                } catch (c: CancellationException) {
                    throw c
                } catch (t: Throwable) {
                    NordicLogger.error("Commissioning failed", t, tag = TAG)
                    currentOnError(t.toCommissioningException(reservedDeviceId.value))
                }
            }
        }

    return remember(fabric) {
        AndroidCommissioningTask(
            context = context,
            fabric = fabric,
            scope = scope,
            launcher = commissionDeviceLauncher,
            reservedDeviceId = reservedDeviceId,
            isRunning = isRunning,
            onError = { currentOnError(it) },
        )
    }
}

private class AndroidCommissioningTask(
    private val context: Context,
    private val fabric: Fabric,
    private val scope: CoroutineScope,
    private val launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    private val reservedDeviceId: MutableState<DeviceId?>,
    private val isRunning: MutableState<Boolean>,
    private val onError: (CommissioningException) -> Unit,
) : CommissioningTask {

    override fun startCommissioning() {
        if (isRunning.value) return
        isRunning.value = true

        scope.launch {
            // AppCommissioningService pairs the device under the id reserved here, so it has to be
            // reserved before the Google Home flow is asked for an intent sender.
            val deviceId = fabric.nextDeviceId()
            reservedDeviceId.value = deviceId

            commissionDevice(deviceId)
        }
    }

    /**
     * Asks the Google Home commissioning client for the intent sender of the add-device flow and
     * launches it.
     */
    private fun commissionDevice(deviceId: DeviceId) {
        val commissionDeviceRequest =
            CommissioningRequest.builder()
//            .setOnboardingPayload(payload) // Add device payload directly to commission a specific device, such as payload = "MT:6FCJ142C00KA0648G00"
                .setCommissioningService(
                    ComponentName(context, AppCommissioningService::class.java)
                )
                .build()

        Matter.getCommissioningClient(context)
            .commissionDevice(commissionDeviceRequest)
            .addOnSuccessListener { result ->
                launcher.launch(IntentSenderRequest.Builder(result).build())
            }
            .addOnFailureListener { error ->
                isRunning.value = false
                NordicLogger.error("Commissioning failed", error, tag = TAG)
                onError(error.toCommissioningException(deviceId))
            }
    }
}

fun Throwable.toCommissioningException(deviceId: DeviceId?): CommissioningException {
    return when (this) {
        is CommissioningException -> this
        is MatterCommissioningApiException -> CommissioningException(
            deviceId,
            Stage.COMMISSIONING,
            this.errorDetails.googleErrorCode,
            this.message ?: ""
        )

        is ApiException -> CommissioningException(
            deviceId,
            Stage.COMMISSIONING,
            this.status.statusCode,
            this.message ?: ""
        )

        else -> CommissioningException.unknown(Stage.COMMISSIONING)
    }
}
