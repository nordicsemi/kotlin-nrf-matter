package no.nordicsemi.nrf.matter.cluster

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.webdemo.WebDemoAction
import no.nordicsemi.nrf.matter.webdemo.WebDemoEvents
import kotlin.random.Random

private data class AttributeKey(
    val deviceId: DeviceId,
    val endpoint: Int,
    val clusterId: Long,
    val attributeId: Long,
)

/**
 * Web/demo actual [MatterClient]: an in-memory attribute store standing in for the real CHIP/
 * MatterSupport wire. Every real cluster class (`OnOffCluster`, `DoorLockCluster`,
 * `LevelControlCluster`, the Nordic manufacturer-specific clusters, ...) is unmodified `:lib`/
 * `:shared` code -- faking only this class is what lets that real business logic run against
 * demo data. [seed] is the one addition beyond [MatterClient]'s contract, used by the web
 * commissioning fake to populate a newly "discovered" device before it's read back.
 */
internal class WebMatterClient : MatterClient() {

    private val store = mutableMapOf<AttributeKey, MutableStateFlow<Any?>>()

    private fun flowFor(deviceId: DeviceId, endpoint: Int, clusterId: Long, attributeId: Long): MutableStateFlow<Any?> =
        store.getOrPut(AttributeKey(deviceId, endpoint, clusterId, attributeId)) { MutableStateFlow(null) }

    fun seed(deviceId: DeviceId, endpoint: Int, clusterId: Long, attributeId: Long, value: Any?) {
        flowFor(deviceId, endpoint, clusterId, attributeId).value = value
    }

    override suspend fun <T> setAttribute(
        value: T,
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long,
    ) {
        flowFor(deviceId, endpoint, clusterId, attributeId).value = value
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> readAttribute(deviceId: DeviceId, endpoint: Int, clusterId: Long, attributeId: Long): T {
        val key = AttributeKey(deviceId, endpoint, clusterId, attributeId)
        val flow = store[key] ?: error("Web demo: no fake attribute seeded for $key")
        return flow.value as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> observeAttribute(deviceId: DeviceId, endpoint: Int, clusterId: Long, attributeId: Long): Flow<T> =
        flowFor(deviceId, endpoint, clusterId, attributeId) as Flow<T>

    override suspend fun <T> executeCommand(
        value: T,
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        commandId: Long,
        timedInvokeTimeoutMs: Int?,
    ) {
        when (clusterId) {
            OnOffClusterInfo.ID -> {
                val isOn = commandId == 0x01L
                flowFor(deviceId, endpoint, clusterId, OnOffClusterInfo.Attribute.ON_OFF).value = isOn
            }

            DoorLockClusterInfo.ID -> {
                val locked = commandId == DoorLockClusterInfo.Command.LOCK
                flowFor(deviceId, endpoint, clusterId, DoorLockClusterInfo.Attribute.LOCK_STATE).value =
                    if (locked) 1 else 2
            }

            LevelControlClusterInfo.ID -> {
                val level = (value as? UByte)?.toInt() ?: (value as? Number)?.toInt() ?: 0
                flowFor(deviceId, endpoint, clusterId, LevelControlClusterInfo.Attribute.CURRENT_LEVEL).value = level
            }

            MANUFACTURER_SPEC_CLUSTER_ID -> {
                val isOn = (value as? UByte)?.toInt() == 1
                flowFor(deviceId, endpoint, clusterId, MANUFACTURER_SPEC_LED_ATTRIBUTE_ID).value = isOn
            }

            BasicInfoClusterInfo.ID -> {
                // Nordic's Basic Information cluster extension (`:shared`'s BasicInfoExtCluster)
                // shares the standard Basic Information cluster ID -- only its "generate random
                // number" command ever reaches this branch, since the standard cluster has none.
                flowFor(deviceId, endpoint, clusterId, BASIC_INFO_EXT_RANDOM_NUMBER_ATTRIBUTE_ID).value =
                    Random.nextLong(0, 100_000)
            }
        }
        WebDemoEvents.publish(WebDemoAction.ClusterCommandExecuted(deviceId, endpoint, clusterId, commandId))
    }
}

/** `no.nordicsemi.nrf.matter.nordic.ManufacturerSpecClusterInfo.ID` in `:shared` -- `:lib` can't depend on `:shared`. */
internal const val MANUFACTURER_SPEC_CLUSTER_ID: Long = 0xFFF1FC01
internal const val MANUFACTURER_SPEC_LED_ATTRIBUTE_ID: Long = 0xFFF10001
internal const val MANUFACTURER_SPEC_BUTTON_ATTRIBUTE_ID: Long = 0xFFF10002
internal const val MANUFACTURER_SPEC_NAME_ATTRIBUTE_ID: Long = 0xFFF10000

/** `no.nordicsemi.nrf.matter.nordic.BasicInfoExtClusterInfo.Attribute.RANDOM_NUMBER` in `:shared`. */
internal const val BASIC_INFO_EXT_RANDOM_NUMBER_ATTRIBUTE_ID: Long = 0x17
