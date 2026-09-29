package no.nordicsemi.nrf.matter.ui.device

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import no.nordicsemi.nrf.matter.cluster.SmokeCoAlarmCluster
import no.nordicsemi.nrf.matter.model.AlarmState
import no.nordicsemi.nrf.matter.model.Device
import no.nordicsemi.nrf.matter.model.ExpressedState
import no.nordicsemi.nrf.matter.model.toDeviceId
import no.nordicsemi.nrf.matter.ui.smokecoalarm.FakeSmokeCoAlarmClient
import no.nordicsemi.nrf.matter.ui.smokecoalarm.SmokeCoAlarmController
import kotlin.test.Test

/**
 * Renders the real device-list card, expands it, and drives the same fake device wire used by
 * SmokeCoAlarmControllerTest to check that steps 6-8 (trigger smoke, trigger CO, stop smoke)
 * actually show up correctly on screen, not just in the controller's state object.
 */
@OptIn(ExperimentalTestApi::class)
class DeviceItemSmokeCoAlarmUiTest {

    private val device = Device(deviceId = 1L.toDeviceId())
    private val client = FakeSmokeCoAlarmClient()
    private val cluster = SmokeCoAlarmCluster(
        deviceId = device.deviceId,
        endpoint = 1,
        controller = client,
    )
    private val controller = SmokeCoAlarmController(cluster, CoroutineScope(Dispatchers.Unconfined))

    @Test
    fun steps6to8_smokeAndCoAlarmTriggers_reflectOnDeviceCard() = runComposeUiTest {
        setContent {
            DeviceItem(device = device, clusters = listOf(controller), onDecommission = {})
        }

        // Expand the device card from the device list.
        onNodeWithTag("device_item_${device.deviceId.stringValue}").performClick()

        onNodeWithTag("Smoke_NORMAL").assertIsSelected()
        deviceStatus().assertTextEquals("Normal")
        muteStatus().assertTextEquals("Sound on")
        alarmIcon().assertContentDescriptionEquals("No alarm")

        // Step 6: trigger smoke alarm.
        client.smokeState.value = AlarmState.CRITICAL.value
        client.expressedState.value = ExpressedState.SMOKE_ALARM.value
        waitForIdle()

        onNodeWithTag("Smoke_CRITICAL").assertIsSelected()
        deviceStatus().assertTextEquals("Smoke alarm")
        alarmIcon().assertContentDescriptionEquals("Alarm active")

        // Step 7: trigger CO alarm on top of the active smoke alarm. Smoke has display priority,
        // so the device keeps expressing the smoke alarm.
        client.coState.value = AlarmState.CRITICAL.value
        waitForIdle()

        onNodeWithTag("Smoke_CRITICAL").assertIsSelected()
        deviceStatus().assertTextEquals("Smoke alarm")
        alarmIcon().assertContentDescriptionEquals("Alarm active")

        // Step 8: stop smoke alarm; CO alarm must keep the card marked as active.
        client.smokeState.value = AlarmState.NORMAL.value
        client.expressedState.value = ExpressedState.CO_ALARM.value
        waitForIdle()

        onNodeWithTag("Smoke_NORMAL").assertIsSelected()
        deviceStatus().assertTextEquals("CO alarm")
        alarmIcon().assertContentDescriptionEquals("Alarm active")
    }

    @Test
    fun muteStatus_reflectsDeviceMuted() = runComposeUiTest {
        setContent {
            DeviceItem(device = device, clusters = listOf(controller), onDecommission = {})
        }
        onNodeWithTag("device_item_${device.deviceId.stringValue}").performClick()

        muteStatus().assertTextEquals("Sound on")

        client.deviceMuted.value = 1
        waitForIdle()

        muteStatus().assertTextEquals("Muted")
    }

    // The icon's testTag only survives in the unmerged semantics tree: DeviceHeader's Row
    // merges its children's semantics (including the icon's) into a single merged node.
    private fun ComposeUiTest.deviceStatus() = onNodeWithTag("device_status", useUnmergedTree = true)

    private fun ComposeUiTest.muteStatus() = onNodeWithTag("mute_status", useUnmergedTree = true)

    private fun ComposeUiTest.alarmIcon() = onNodeWithTag("smoke_co_alarm_icon", useUnmergedTree = true)
}