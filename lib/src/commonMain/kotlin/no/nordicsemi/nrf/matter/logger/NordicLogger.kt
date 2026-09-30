package no.nordicsemi.nrf.matter.logger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface NordicLoggerBackend {

    fun getLogs(): Flow<List<LogEntity>>

    fun info(message: String, tag: String)

    fun debug(message: String, tag: String)

    fun error(message: String, t: Throwable?, tag: String)
}

object NordicLogger {

    private object PrintLoggerBackend : NordicLoggerBackend {
        override fun getLogs(): Flow<List<LogEntity>> = flowOf(emptyList())
        override fun info(message: String, tag: String) = println("$tag: $message")
        override fun debug(message: String, tag: String) = println("$tag: $message")
        override fun error(message: String, t: Throwable?, tag: String) = println("$tag: $message")
    }

    private var backend: NordicLoggerBackend = PrintLoggerBackend

    fun setBackend(backend: NordicLoggerBackend) {
        this.backend = backend
    }

    fun getLogs(): Flow<List<LogEntity>> = backend.getLogs()

    fun info(message: String, tag: String = "") = backend.info(message, tag)

    fun debug(message: String, tag: String = "") = backend.debug(message, tag)

    fun error(message: String, t: Throwable? = null, tag: String = "") = backend.error(message, t, tag)
}
