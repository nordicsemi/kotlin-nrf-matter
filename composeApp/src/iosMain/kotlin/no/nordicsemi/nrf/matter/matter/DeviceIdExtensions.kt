package no.nordicsemi.nrf.matter.matter

import no.nordicsemi.nrf.matter.model.DeviceId
import platform.Foundation.NSNumber

/**
 * Mirrors `iosApp/iosApp/kotlin/common/DeviceId.swift`'s `.nsNumber()` extension.
 */
internal fun DeviceId.nsNumber(): NSNumber = NSNumber(long = longValue)
