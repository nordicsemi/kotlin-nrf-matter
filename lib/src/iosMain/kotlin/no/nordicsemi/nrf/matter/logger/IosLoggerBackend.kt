package no.nordicsemi.nrf.matter.logger

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal object IosLoggerBackend : NordicLoggerBackend {

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

    var logger: IOSLogger = NoOpLogger

    override fun getLogs(): Flow<List<LogEntity>> {
        return callbackFlow {
            logger.getLogs { trySend(it) }

            awaitClose {

            }
        }
    }

    override fun info(message: String, tag: String) {
        logger.info(tag, message)
    }

    override fun debug(message: String, tag: String) {
        logger.debug(tag, message)
    }

    override fun error(message: String, t: Throwable?, tag: String) {
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

val NordicLogger.logsChannel: Channel<String>
    get() = IosLoggerBackend.logger.logsChannel

fun NordicLogger.setLogger(logger: IOSLogger) {
    IosLoggerBackend.logger = logger
    setBackend(IosLoggerBackend)
}
