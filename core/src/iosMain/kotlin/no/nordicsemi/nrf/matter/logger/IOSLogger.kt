package no.nordicsemi.nrf.matter.logger

interface IOSLogger {

    fun getLogs(onReady: (List<LogEntity>) -> Unit)

    fun info(tag: String, message: String)

    fun debug(tag: String, message: String)

    fun error(tag: String, message: String)
}