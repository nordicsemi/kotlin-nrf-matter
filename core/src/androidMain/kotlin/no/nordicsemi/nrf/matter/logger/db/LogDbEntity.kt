package no.nordicsemi.nrf.matter.logger.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import no.nordicsemi.nrf.matter.logger.LogLevel

@Entity(tableName = "logs")
data class LogDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    val level: LogLevel,
    val tag: String,
    val message: String
)
