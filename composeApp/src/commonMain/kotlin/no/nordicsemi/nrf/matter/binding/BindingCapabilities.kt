package no.nordicsemi.nrf.matter.binding

import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.DeviceType

/** The OnOff cluster (0x0006): the only cluster this app writes bindings for. */
internal const val ON_OFF_CLUSTER_ID = 0x006L

/**
 * Whether the device can be the *target* of a binding, meaning it implements the OnOff cluster as
 * a server and so can be told to switch by another device.
 */
fun Device.isBindingCapable(): Boolean {
    return deviceMatterInfo.any { it.serverClusters.contains(ON_OFF_CLUSTER_ID) }
}

/**
 * Whether the device can be the *source* of a binding, meaning it sends OnOff commands of its own.
 *
 * Decided by device type rather than by a client cluster on some endpoint, which is what the app
 * has always done - a light switch or an outlet is offered as a source, nothing else.
 */
internal fun Device.isBindingSource(): Boolean {
    return deviceType == DeviceType.LIGHT_SWITCH || deviceType == DeviceType.OUTLET
}
