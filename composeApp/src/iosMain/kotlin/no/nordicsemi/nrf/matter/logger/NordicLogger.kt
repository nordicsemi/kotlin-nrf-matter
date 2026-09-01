package no.nordicsemi.nrf.matter.logger

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual object NordicLogger {

    /**
     * Used until [setLogger] installs the real logger.
     *
     * Every process running Kotlin code has to install its own logger, and the app extension runs
     * in a process of its own. Falling back here keeps a missing installation from turning a log
     * call into an unhandled Kotlin exception, which is fatal once it crosses the Swift boundary.
     */
    private object NoOpLogger : IOSLogger {
        override val logsChannel: Channel<String> = Channel(Channel.RENDEZVOUS)

        override fun getLogs(onReady: (List<LogEntity>) -> Unit) = onReady(emptyList())

        override fun info(tag: String, message: String) = println("$tag: $message")

        override fun debug(tag: String, message: String) = println("$tag: $message")

        override fun error(tag: String, message: String) = println("$tag: $message")
    }

    private var logger: IOSLogger = NoOpLogger

    val logsChannel
        get() = logger.logsChannel

    fun setLogger(logger: IOSLogger) {
        this.logger = logger
    }

    actual fun getLogs(): Flow<List<LogEntity>> {
        return callbackFlow {
            logger.getLogs { trySend(it) }

            awaitClose {

            }
        }
    }

    actual fun info(message: String, tag: String) {
        logger.info(tag, message)
    }

    actual fun debug(message: String, tag: String) {
        logger.debug(tag, message)
    }

    actual fun error(message: String, t: Throwable?, tag: String) {
        val fullMessage = buildString {
            append(message)
            if (t != null) {
                appendLine()
                append(t.stackTraceToString())
            }
        }
        logger.error(tag, fullMessage)
    }
}
