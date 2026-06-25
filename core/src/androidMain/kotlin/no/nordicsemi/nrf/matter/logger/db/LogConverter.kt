package no.nordicsemi.nrf.matter.logger.db

import androidx.room.TypeConverter
import no.nordicsemi.nrf.matter.logger.LogLevel

class LogConverters {
    @TypeConverter
    fun fromLogLevel(level: LogLevel): String {
        return level.name
    }

    @TypeConverter
    fun toLogLevel(level: String): LogLevel {
        return LogLevel.valueOf(level)
    }
}
