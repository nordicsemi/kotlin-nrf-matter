package no.nordicsemi.nrf.matter.logger

import kotlinx.coroutines.channels.Channel

interface IOSLogger {

    val logsChannel: Channel<String>

    fun getLogs(onReady: (List<LogEntity>) -> Unit)

    fun info(tag: String, message: String)

    fun debug(tag: String, message: String)

    fun error(tag: String, message: String)
}
