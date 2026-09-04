@file:OptIn(ExperimentalForeignApi::class)

package no.nordicsemi.nrf.matter.adapters

import kotlinx.cinterop.ExperimentalForeignApi
import no.nordicsemi.nrf.matter.model.DeviceId
import platform.Foundation.NSNumber

fun Long.toNSNumber() = NSNumber(long = this)

fun DeviceId.toNSNumber() = NSNumber(long = this.longValue)
fun Int.toNSNumber() = NSNumber(int = this)
fun NSNumber.toDeviceId() = DeviceId(this.stringValue)
