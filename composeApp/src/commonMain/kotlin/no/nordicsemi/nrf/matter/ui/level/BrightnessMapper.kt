package no.nordicsemi.nrf.matter.ui.level

import kotlin.math.roundToInt

private const val MIN_LEVEL = 1
private const val MAX_LEVEL = 254
private const val LEVEL_RANGE = (MAX_LEVEL - MIN_LEVEL).toFloat()

/** Maps a raw device level ([MIN_LEVEL]..[MAX_LEVEL]) to a 0f..1f brightness. */
fun Number.toBrightness(): Float = ((toFloat() - MIN_LEVEL) / LEVEL_RANGE).coerceIn(0f, 1f)

/** Maps a 0f..1f brightness to a raw device level. */
fun Float.toLevel(): Int =
    (MIN_LEVEL + this * LEVEL_RANGE).roundToInt().coerceIn(MIN_LEVEL, MAX_LEVEL)
