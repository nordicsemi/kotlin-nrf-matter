package no.nordicsemi.nrf.matter.logger

import kotlinx.coroutines.flow.Flow

expect object NordicLogger {

    fun getLogs(): Flow<List<LogEntity>>

    fun info(message: String, tag: String = "")

    fun debug(message: String, tag: String = "")

    fun error(message: String, t: Throwable? = null, tag: String = "")
}
