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

        /**
         * Opens the log database, unless it is already open.
         *
         * `NordicMatterInitializer` calls this before any app component runs, so an app normally
         * has no reason to. Calling it again is a no-op: a second `databaseBuilder` over the same
         * file would leave two Room instances writing to it.
         */
        fun initialize(context: Context) {
            if (instance != null) return

            synchronized(this) {
                if (instance != null) return

                instance = Room.databaseBuilder(
                    context.applicationContext,
                    LogDatabase::class.java,
                    "log_database"
                ).build()
            }
        }

        fun getDatabase(): LogDatabase {
            return instance ?: error(
                "The log database is not open. It is opened by the library's App Startup " +
                        "initializer; if you removed that provider from your manifest, call " +
                        "LogDatabase.initialize(context) before using the library."
            )
        }
    }
}
