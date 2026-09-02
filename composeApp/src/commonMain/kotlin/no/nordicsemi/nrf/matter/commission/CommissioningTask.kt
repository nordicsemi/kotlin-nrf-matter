package no.nordicsemi.nrf.matter.commission

import androidx.compose.runtime.Composable
import no.nordicsemi.nrf.matter.api.Fabric
import no.nordicsemi.nrf.matter.api.NordicMatters
import no.nordicsemi.nrf.matter.model.DeviceId

/**
 * Runs the platform's add-device flow: the Google Home commissioning flow on Android, the
 * MatterSupport add-device flow on iOS.
 *
 * The task pairs the device onto the fabric and reports the node id it was given; recording the
 * device is up to the caller, which is what [Fabric.commissionDevice] does.
 */
interface CommissioningTask {

    /**
     * Starts the platform add-device flow. Does nothing while a previous run is still going.
     */
    fun startCommissioning()
}

/**
 * Creates the [CommissioningTask] for [fabric], surviving recomposition.
 *
 * ```
 * val commissioningTask = rememberCommissioningTask(
 *     onSuccess = { fabric.commissionDevice(it) },
 *     onError = { showErrorScreen() },
 * )
 *
 * Button(onClick = { commissioningTask.startCommissioning() }) {
 *     Text("Commission.")
 * }
 * ```
 *
 * @param fabric the fabric to commission into; it reserves the node id for the new device.
 * @param onSuccess called with the node id of the paired device. Anything it throws is reported to
 * [onError], so a failing [Fabric.commissionDevice] surfaces like any other commissioning failure.
 * @param onError called when the flow fails or the user cancels it.
 */
@Composable
expect fun rememberCommissioningTask(
    fabric: Fabric = NordicMatters.defaultFabric,
    onSuccess: suspend (DeviceId) -> Unit,
    onError: (CommissioningException) -> Unit,
): CommissioningTask
