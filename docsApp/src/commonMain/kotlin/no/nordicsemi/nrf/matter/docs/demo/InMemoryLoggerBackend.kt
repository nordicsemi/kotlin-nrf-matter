package no.nordicsemi.nrf.matter.docs.demo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.nordicsemi.nrf.matter.logger.LogEntity
import no.nordicsemi.nrf.matter.logger.LogLevel
import no.nordicsemi.nrf.matter.logger.NordicLoggerBackend
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal object InMemoryLoggerBackend : NordicLoggerBackend {

    private val logs = MutableStateFlow<List<LogEntity>>(emptyList())

    override fun getLogs(): Flow<List<LogEntity>> = logs.asStateFlow()

    override fun info(message: String, tag: String) = append(LogLevel.INFO, message, tag)

    override fun debug(message: String, tag: String) = append(LogLevel.DEBUG, message, tag)

    override fun error(message: String, t: Throwable?, tag: String) =
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
