package no.nordicsemi.nrf.matter.ui.smokecoalarm

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import no.nordicsemi.nrf.matter.cluster.MatterClient
import no.nordicsemi.nrf.matter.cluster.SmokeCoAlarmCluster
import no.nordicsemi.nrf.matter.cluster.SmokeCoAlarmClusterInfo
import no.nordicsemi.nrf.matter.model.AlarmState
import no.nordicsemi.nrf.matter.model.DeviceId
import no.nordicsemi.nrf.matter.model.ExpressedState
import no.nordicsemi.nrf.matter.model.toDeviceId
import no.nordicsemi.nrf.matter.ui.UiState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

/**
 * Fakes the wire (MatterClient) so alarm attributes can be driven directly from a test,
 * standing in for what TestEventTrigger commands would push from a real/simulated device.
 *
 * Internal (not private) so [no.nordicsemi.nrf.matter.ui.device.DeviceItemSmokeCoAlarmUiTest]
 * can reuse it to drive the same fake device through the Compose UI.
 */
internal class FakeSmokeCoAlarmClient : MatterClient() {

    val expressedState = MutableStateFlow<Number>(ExpressedState.NORMAL.value)
    val smokeState = MutableStateFlow<Number>(AlarmState.NORMAL.value)
    val coState = MutableStateFlow<Number>(AlarmState.NORMAL.value)
    val batteryAlert = MutableStateFlow<Number>(AlarmState.NORMAL.value)
    val deviceMuted = MutableStateFlow<Number>(0)
    val testInProgress = MutableStateFlow(false)
    val hardwareFaultAlert = MutableStateFlow(false)
    val endOfServiceAlert = MutableStateFlow<Number>(0)

    val executedCommands = mutableListOf<Long>()

    /** When set, commands fail with this error instead of succeeding. */
    var commandError: Throwable? = null

    override suspend fun <T> setAttribute(
        value: T,
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long,
    ) = error("Not used by SmokeCoAlarmController")

    override suspend fun <T> readAttribute(
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long,
    ): T = error("Not used by SmokeCoAlarmController")

    @Suppress("UNCHECKED_CAST")
    override fun <T> observeAttribute(
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        attributeId: Long,
    ): Flow<T> {
        val flow = when (attributeId) {
            SmokeCoAlarmClusterInfo.Attribute.EXPRESSED_STATE -> expressedState
            SmokeCoAlarmClusterInfo.Attribute.SMOKE_STATE -> smokeState
            SmokeCoAlarmClusterInfo.Attribute.CO_STATE -> coState
            SmokeCoAlarmClusterInfo.Attribute.BATTERY_ALERT -> batteryAlert
            SmokeCoAlarmClusterInfo.Attribute.DEVICE_MUTED -> deviceMuted
            SmokeCoAlarmClusterInfo.Attribute.TEST_IN_PROGRESS -> testInProgress
            SmokeCoAlarmClusterInfo.Attribute.HARDWARE_FAULT_ALERT -> hardwareFaultAlert
            SmokeCoAlarmClusterInfo.Attribute.END_OF_SERVICE_ALERT -> endOfServiceAlert
            else -> error("Unexpected attribute id $attributeId")
        }
        return flow as Flow<T>
    }

    override suspend fun <T> executeCommand(
        value: T,
        deviceId: DeviceId,
        endpoint: Int,
        clusterId: Long,
        commandId: Long,
        timedInvokeTimeoutMs: Int?,
    ) {
        executedCommands += commandId
        commandError?.let { throw it }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class SmokeCoAlarmControllerTest {

    private val client = FakeSmokeCoAlarmClient()
    private val cluster = SmokeCoAlarmCluster(
        deviceId = 1L.toDeviceId(),
        endpoint = 1,
        controller = client,
    )

    // Step 6: Trigger smoke alarm (mocks TestEventTrigger 0x005c...9c / ForceSmokeCritical).
    @Test
    fun step6_triggerSmokeAlarm_movesSmokeStateToCritical() = runTest(UnconfinedTestDispatcher()) {
        val controller = SmokeCoAlarmController(cluster, backgroundScope)

        client.smokeState.value = AlarmState.CRITICAL.value

        val state = controller.state.value
        assertEquals(AlarmState.CRITICAL, state.smokeState)
        assertEquals(AlarmState.NORMAL, state.coState)
        assertTrue(state.isAlarmActive)
    }

    // Step 7: Trigger CO alarm on top of an active smoke alarm (mocks TestEventTrigger 0x005c...9d / ForceCOCritical).
    // The device shows no visible change (smoke has display priority), but the CO attribute must still update.
    @Test
    fun step7_triggerCoAlarm_updatesCoStateWithoutClearingSmoke() = runTest(UnconfinedTestDispatcher()) {
        val controller = SmokeCoAlarmController(cluster, backgroundScope)

        client.smokeState.value = AlarmState.CRITICAL.value // precondition from step 6
        client.coState.value = AlarmState.CRITICAL.value

        val state = controller.state.value
        assertEquals(AlarmState.CRITICAL, state.smokeState)
        assertEquals(AlarmState.CRITICAL, state.coState)
        assertTrue(state.isAlarmActive)
    }

    // Step 8: Stop smoke alarm while CO alarm stays active (mocks TestEventTrigger 0x005c...a0 / ClearSmoke).
    // Regression check: isAlarmActive must stay true because it is still driven by the CO alarm.
    @Test
    fun step8_stopSmokeAlarm_returnsSmokeToNormalButCoStaysCritical() = runTest(UnconfinedTestDispatcher()) {
        val controller = SmokeCoAlarmController(cluster, backgroundScope)

        client.smokeState.value = AlarmState.CRITICAL.value
        client.coState.value = AlarmState.CRITICAL.value

        client.smokeState.value = AlarmState.NORMAL.value

        val state = controller.state.value
        assertEquals(AlarmState.NORMAL, state.smokeState)
        assertEquals(AlarmState.CRITICAL, state.coState)
        assertTrue(state.isAlarmActive)
    }

    // Sanity check: with everything normal, no alarm should be reported active.
    @Test
    fun idleState_isNotAlarmActive() = runTest(UnconfinedTestDispatcher()) {
        val controller = SmokeCoAlarmController(cluster, backgroundScope)

        assertFalse(controller.state.value.isAlarmActive)
        assertTrue(client.executedCommands.isEmpty())
    }

    @Test
    fun expressedState_isMappedFromAttribute() = runTest(UnconfinedTestDispatcher()) {
        val controller = SmokeCoAlarmController(cluster, backgroundScope)

        client.expressedState.value = ExpressedState.CO_ALARM.value

        assertEquals(ExpressedState.CO_ALARM, controller.state.value.expressedState)
    }

    @Test
    fun selfTestFailure_withoutDeviceStatus_showsConnectionMessage() = runTest(UnconfinedTestDispatcher()) {
        val controller = SmokeCoAlarmController(cluster, backgroundScope)
        client.commandError = IllegalStateException("device not connected")

        controller.runSelfTest()

        val error = assertIs<UiState.Error>(controller.selfTestState.value)
        assertEquals(
            "Couldn't reach the device to start the self-test. Check the connection and try again.",
            error.message,
        )
    }

    @Test
    fun selfTest_finishingWithinTimeout_returnsToIdleWithoutError() = runTest(UnconfinedTestDispatcher()) {
        val controller = SmokeCoAlarmController(cluster, backgroundScope)

        controller.runSelfTest()
        client.expressedState.value = ExpressedState.TESTING.value
        assertTrue(controller.state.value.isSelfTesting)

        advanceTimeBy(5.seconds)
        client.expressedState.value = ExpressedState.NORMAL.value
        advanceTimeBy(SmokeCoAlarmController.SELF_TEST_TIMEOUT)

        assertFalse(controller.state.value.isSelfTesting)
        assertIs<UiState.Idle<Unit>>(controller.selfTestState.value)
    }

    @Test
    fun selfTest_stuckInTesting_timesOutWithError() = runTest(UnconfinedTestDispatcher()) {
        val controller = SmokeCoAlarmController(cluster, backgroundScope)

        controller.runSelfTest()
        client.expressedState.value = ExpressedState.TESTING.value

        advanceTimeBy(SmokeCoAlarmController.SELF_TEST_TIMEOUT - 1.seconds)
        assertIs<UiState.Idle<Unit>>(controller.selfTestState.value)

        advanceTimeBy(2.seconds)
        val error = assertIs<UiState.Error>(controller.selfTestState.value)
        assertEquals(
            "The self-test didn't finish within 30 seconds. Check the alarm, then try again.",
            error.message,
        )
    }

    @Test
    fun selfTestError_clearsWhenExpressedStateChanges() = runTest(UnconfinedTestDispatcher()) {
        val controller = SmokeCoAlarmController(cluster, backgroundScope)
        client.commandError = IllegalStateException("device not connected")

        controller.runSelfTest()
        assertIs<UiState.Error>(controller.selfTestState.value)

        client.expressedState.value = ExpressedState.SMOKE_ALARM.value

        assertIs<UiState.Idle<Unit>>(controller.selfTestState.value)
    }
}
