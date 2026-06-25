package no.nordicsemi.nrf.matter.logger

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual object NordicLogger {

    private lateinit var logger: IOSLogger

    internal fun setLogger(logger: IOSLogger) {
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
        logger.error(tag, message)
    }
}
