package no.nordicsemi.nrf.matter.logger

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import no.nordicsemi.nrf.matter.logger.db.LogDatabase
import no.nordicsemi.nrf.matter.logger.db.toDomain
import no.nordicsemi.nrf.matter.logger.db.toEntity

actual object NordicLogger {

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    private val db by lazy { LogDatabase.getDatabase() }

    actual fun getLogs(): Flow<List<LogEntity>> {
        return db.logDao().getAllLogs().map { it.map { it.toDomain() } }
    }

    actual fun info(message: String, tag: String) {
        Log.i(tag, message)
        val logEntity = LogEntity(
            date = System.currentTimeMillis(),
            level = LogLevel.INFO,
            tag = tag,
            message = message,
        )
        scope.launch {
            db.logDao().insertLog(logEntity.toEntity())
        }
    }

    actual fun debug(message: String, tag: String) {
        Log.d(tag, message)
        val logEntity = LogEntity(
            date = System.currentTimeMillis(),
            level = LogLevel.DEBUG,
            tag = tag,
            message = message,
        )
        scope.launch {
            db.logDao().insertLog(logEntity.toEntity())
        }
    }

    actual fun error(message: String, t: Throwable?, tag: String) {
        Log.e(tag, message, t)
        val logEntity = LogEntity(
            date = System.currentTimeMillis(),
            level = LogLevel.ERROR,
            tag = tag,
            message = message,
        )
        scope.launch {
            db.logDao().insertLog(logEntity.toEntity())
        }
    }
}
