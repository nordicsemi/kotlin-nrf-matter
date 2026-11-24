package no.nordicsemi.nrf.matter.ui.windowcovering

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.cluster.WindowCoveringCluster
import no.nordicsemi.nrf.matter.cluster.WindowCoveringClusterInfo.Feature
import no.nordicsemi.nrf.matter.logger.NordicLogger
import no.nordicsemi.nrf.matter.ui.device.ClusterController

data class Commands(val moves: Int = 0)

data class WindowCoveringState(
    val featureMap: Int = 0,
    /** 0 = fully open, 100 = fully closed. Null while unknown. */
    val liftPercent: Int? = null,
    /** 0 = fully open (horizontal), 100 = fully closed (vertical). Null while unknown. */
    val tiltPercent: Int? = null,
    /** Position the device was told to move to. Null when none is set. */
    val liftTargetPercent: Int? = null,
    val tiltTargetPercent: Int? = null,
    /** True from an Open/Close/slider command until the move is stopped or has finished. */
    val liftMoveRequested: Boolean = false,
    val tiltMoveRequested: Boolean = false,
    /**
     * Whether the device has changed its current position since the last command. Until it does, the target is
     * the best known position, as some devices only update the target and leave the current position alone.
     */
    val liftTracked: Boolean = false,
    val tiltTracked: Boolean = false,
    /**
     * True once the device has reported its position during a move. A device that only sets the target, like the
     * Matter Virtual Device, never moves on its own, so Stop has nothing to stop.
     */
    val reportsProgress: Boolean = false,
    /** True after Stop until the next move or position report: the display stays where it stopped. */
    val liftHeld: Boolean = false,
    val tiltHeld: Boolean = false,
    /** Counts moves, so the display glides again on a move to where it already is. */
    val commands: Commands = Commands(),
) {
    fun supports(feature: Int) = featureMap and feature != 0

    // Until the FeatureMap is read, offer whatever the device reports a position for.
    val supportsLift get() = supports(Feature.LIFT) || (featureMap == 0 && liftPercent != null)
    val supportsTilt get() = supports(Feature.TILT) || (featureMap == 0 && tiltPercent != null)

    /** Where the covering is now; the target is only a fallback until a position is known. */
    val liftDestination get() = if (liftTracked) liftPercent ?: liftTargetPercent else liftTargetPercent ?: liftPercent
    val tiltDestination get() = if (tiltTracked) tiltPercent ?: tiltTargetPercent else tiltTargetPercent ?: tiltPercent

    /** True until the device has reported (or been given) any lift or tilt position. */
    val isPositionUnknown get() = liftDestination == null && tiltDestination == null

    /**
     * True while a move this app started is under way. A device's target attribute alone says nothing about
     * motion: some never clear it, and some rewrite it when stopped, so it can't tell moving from idle.
     */
    val isLiftMoving: Boolean
        get() = supportsLift && liftMoveRequested && liftTargetPercent != null && liftPercent != liftTargetPercent
    val isTiltMoving: Boolean
        get() = supportsTilt && tiltMoveRequested && tiltTargetPercent != null && tiltPercent != tiltTargetPercent
    val isMoving: Boolean get() = isLiftMoving || isTiltMoving

    /** Applies a reported lift position; a change from a known position shows the device reports it. */
    fun withLift(percent: Int?) = copy(
        liftPercent = percent,
        liftTracked = liftTracked || (liftPercent != null && percent != null && percent != liftPercent),
        liftHeld = false,
        reportsProgress = reportsProgress || (liftMoveRequested && percent != liftPercent),
    )

    fun withTilt(percent: Int?) = copy(
        tiltPercent = percent,
        tiltTracked = tiltTracked || (tiltPercent != null && percent != null && percent != tiltPercent),
        tiltHeld = false,
        reportsProgress = reportsProgress || (tiltMoveRequested && percent != tiltPercent),
    )

    /** A move is over once the device reports the position it was sent to. */
    fun settled() = copy(
        liftMoveRequested = liftMoveRequested && liftPercent != liftTargetPercent,
        tiltMoveRequested = tiltMoveRequested && tiltPercent != tiltTargetPercent,
    )

    val isOpen: Boolean
        get() = (liftDestination ?: 0) < 100 || (tiltDestination ?: 0) < 100

    val summary: String
        get() = listOfNotNull(
            liftPercent?.let { "Lift: $it% closed" + movingSuffix(it, liftTargetPercent) },
            tiltPercent?.let { "Tilt: $it% closed" + movingSuffix(it, tiltTargetPercent) },
        ).joinToString(" · ").ifEmpty { "Window covering" }
}

/** Controls a device's Window Covering cluster (0x0102). */
class WindowCoveringController(
    private val cluster: WindowCoveringCluster,
    scope: CoroutineScope,
) : ClusterController(scope) {

    private val _state = MutableStateFlow(WindowCoveringState())
    val state = _state.asStateFlow()

    init {
        scope.launch {
            runCatching { cluster.featureMap() }
                .onSuccess { map -> _state.update { it.copy(featureMap = map) } }
        }
        observe("CurrentLiftPercentage(0x08)", flow = cluster.observeLiftPercentage()) { s, v -> s.withLift(v?.toInt()?.coerceIn(0, 100)) }
        observe("CurrentTiltPercentage(0x09)", flow = cluster.observeTiltPercentage()) { s, v -> s.withTilt(v?.toInt()?.coerceIn(0, 100)) }
        observe("TargetLift100ths(0x0B)", flow = cluster.observeTargetLiftPosition()) { s, v -> s.copy(liftTargetPercent = v?.toPercent()) }
        observe("TargetTilt100ths(0x0C)", flow = cluster.observeTargetTiltPosition()) { s, v -> s.copy(tiltTargetPercent = v?.toPercent()) }
        observe("CurrentLift100ths(0x0E)", flow = cluster.observeLiftPosition()) { s, v -> s.withLift(v?.toPercent()) }
        observe("CurrentTilt100ths(0x0F)", flow = cluster.observeTiltPosition()) { s, v -> s.withTilt(v?.toPercent()) }
    }

    private fun <T> observe(
        name: String,
        flow: Flow<T>,
        reducer: (WindowCoveringState, T) -> WindowCoveringState,
    ) {
        flow
            .onEach { value ->
                NordicLogger.debug("report $name = $value", tag = TAG)
                _state.update { reducer(it, value).settled() }
            }
            .catch { /* attribute not supported by this device, keep the default */ }
            .launchIn(scope)
    }

    private var settleJob: Job? = null

    fun open() {
        NordicLogger.debug("command open", tag = TAG)
        move(lift = 0, tilt = 0) { cluster.open() }
    }

    fun close() {
        NordicLogger.debug("command close", tag = TAG)
        move(lift = 100, tilt = 100) { cluster.close() }
    }

    fun stop() {
        NordicLogger.debug("command stop", tag = TAG)
        settleJob?.cancel()
        _state.update {
            it.copy(
                liftTargetPercent = null,
                tiltTargetPercent = null,
                liftMoveRequested = false,
                tiltMoveRequested = false,
                liftTracked = false,
                tiltTracked = false,
                liftHeld = true,
                tiltHeld = true,
            )
        }
        send { cluster.stop() }
    }

    fun setLift(percent: Int) = move(lift = percent) { cluster.goToLiftPercentage(percent * 100) }

    fun setTilt(percent: Int) = move(tilt = percent) { cluster.goToTiltPercentage(percent * 100) }

    private fun move(lift: Int? = null, tilt: Int? = null, action: suspend () -> Unit) {
        _state.update {
            it.copy(
                liftTargetPercent = lift ?: it.liftTargetPercent,
                tiltTargetPercent = tilt ?: it.tiltTargetPercent,
                liftMoveRequested = lift != null || it.liftMoveRequested,
                tiltMoveRequested = tilt != null || it.tiltMoveRequested,
                liftTracked = lift == null && it.liftTracked,
                tiltTracked = tilt == null && it.tiltTracked,
                liftHeld = lift == null && it.liftHeld,
                tiltHeld = tilt == null && it.tiltHeld,
                commands = it.commands.copy(moves = it.commands.moves + 1),
            )
        }
        send(action)
        settleWhenDone()
    }

    /**
     * A device that reports no position while moving (it only sets the target) would stay "moving" for ever.
     * Once the move should have taken its time, treat it as done unless the device has been reporting its position.
     */
    private fun settleWhenDone() {
        settleJob?.cancel()
        val s = _state.value
        val distance = maxOf(
            if (s.liftMoveRequested) kotlin.math.abs((s.liftTargetPercent ?: 0) - (s.liftPercent ?: 0)) else 0,
            if (s.tiltMoveRequested) kotlin.math.abs((s.tiltTargetPercent ?: 0) - (s.tiltPercent ?: 0)) else 0,
        )
        settleJob = scope.launch {
            delay((distance * MOVE_MS_PER_PERCENT).toLong() + SETTLE_MARGIN_MS)
            _state.update {
                it.copy(
                    liftMoveRequested = it.liftMoveRequested && it.liftTracked,
                    tiltMoveRequested = it.tiltMoveRequested && it.tiltTracked,
                )
            }
        }
    }

    private fun send(action: suspend () -> Unit) {
        scope.launch { runCatching { action() } }
    }
}

/** Time the display takes to move 1 %, and so how long a move without reported progress is shown. */
internal const val MOVE_MS_PER_PERCENT = 80L
private const val SETTLE_MARGIN_MS = 500L

private fun movingSuffix(current: Int, target: Int?) =
    if (target != null && target != current) " → $target%" else ""

private fun Number.toPercent(): Int = (toInt() / 100).coerceIn(0, 100)

private const val TAG = "WindowCovering"
