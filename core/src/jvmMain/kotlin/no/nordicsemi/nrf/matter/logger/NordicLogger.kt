package no.nordicsemi.nrf.matter.logger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Minimal in-memory logger backing the `jvm()` target, which this project does not otherwise
 * ship a UI for. There is no persistent store here (unlike androidMain's Room-backed
 * implementation) — logs only live for the process lifetime and are printed to stdout/stderr.
 */
actual object NordicLogger {

    private val logs = MutableStateFlow<List<LogEntity>>(emptyList())

    actual fun getLogs(): Flow<List<LogEntity>> = logs

    actual fun info(message: String, tag: String) {
        println("[$tag] $message")
        record(LogLevel.INFO, tag, message)
    }

    actual fun debug(message: String, tag: String) {
        println("[$tag] $message")
        record(LogLevel.DEBUG, tag, message)
    }

    actual fun error(message: String, t: Throwable?, tag: String) {
        System.err.println("[$tag] $message")
        t?.printStackTrace()
        record(LogLevel.ERROR, tag, message)
    }

    private fun record(level: LogLevel, tag: String, message: String) {
        val entry = LogEntity(
            date = System.currentTimeMillis(),
            level = level,
            tag = tag,
            message = message,
        )
        logs.value = logs.value + entry
    }
}
