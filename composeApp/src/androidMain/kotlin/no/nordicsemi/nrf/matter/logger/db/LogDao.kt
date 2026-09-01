package no.nordicsemi.nrf.matter.logger.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertLog(log: LogDbEntity)

    @Query("SELECT * FROM logs ORDER BY date DESC")
    fun getAllLogs(): Flow<List<LogDbEntity>>
}
