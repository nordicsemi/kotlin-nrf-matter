package no.nordicsemi.nrf.matter.logger.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [LogDbEntity::class], version = 1, exportSchema = false)
@TypeConverters(LogConverters::class)
abstract class LogDatabase : RoomDatabase() {

    abstract fun logDao(): LogDao

    companion object {

        @Volatile
        private var instance: LogDatabase? = null

        fun initialize(context: Context) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                LogDatabase::class.java,
                "log_database"
            ).build()
        }

        fun getDatabase(): LogDatabase {
            return instance!!
        }
    }
}
