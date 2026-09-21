package no.nordicsemi.nrf.matter.logger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Web/demo actual: keeps logs in memory only (no persistence across page reloads), which is
 * appropriate for the documentation-site demo this target exists for.
 */
@OptIn(ExperimentalTime::class)
actual object NordicLogger {

    private val logs = MutableStateFlow<List<LogEntity>>(emptyList())

    actual fun getLogs(): Flow<List<LogEntity>> = logs.asStateFlow()

    actual fun info(message: String, tag: String) = append(LogLevel.INFO, message, tag)

    actual fun debug(message: String, tag: String) = append(LogLevel.DEBUG, message, tag)

    actual fun error(message: String, t: Throwable?, tag: String) =
        append(LogLevel.ERROR, t?.message?.let { "$message: $it" } ?: message, tag)

    private fun append(level: LogLevel, message: String, tag: String) {
        logs.update { current ->
            current + LogEntity(
                date = Clock.System.now().toEpochMilliseconds(),
                level = level,
                tag = tag,
                message = message,
            )
        }
    }
}
