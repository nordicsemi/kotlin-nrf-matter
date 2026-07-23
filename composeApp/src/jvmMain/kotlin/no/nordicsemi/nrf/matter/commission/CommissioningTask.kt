package no.nordicsemi.nrf.matter.commission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import no.nordicsemi.nrf.matter.model.Device

/**
 * Matter commissioning has no JVM/desktop implementation (there is no MatterSupport-equivalent
 * API to commission against) — this immediately reports an error rather than hanging silently.
 */
@Composable
actual fun CommissioningTask(onSuccess: (Device) -> Unit, onError: (CommissioningException) -> Unit) {
    LaunchedEffect(Unit) {
        onError(
            CommissioningException(
                deviceId = null,
                stage = Stage.COMMISSIONING,
                errorCode = null,
                displayMessage = "Matter commissioning is not supported on this platform.",
            )
        )
    }
}
