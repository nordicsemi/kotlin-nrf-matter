package no.nordicsemi.nrf.matter.matter

import platform.Foundation.NSNumber

/** Matter cluster/attribute/command IDs are unsigned 32-bit; some vendor-specific ones (e.g. `0xFFF1xxxx`) overflow `Int`. */
internal fun UInt.toNSNumber(): NSNumber = NSNumber(unsignedInt = this)
