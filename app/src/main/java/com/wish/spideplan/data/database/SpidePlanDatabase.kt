package com.wish.spideplan.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.wish.spideplan.data.model.*
import com.wish.spideplan.data.database.dao.*

@Database(
    entities = [
        Task::class,
        SleepEntry::class,
        Quote::class,
        BrainDump::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class SpidePlanDatabase : RoomDatabase() {
    
    abstract fun taskDao(): TaskDao
    abstract fun sleepDao(): SleepDao
    abstract fun quoteDao(): QuoteDao
    abstract fun brainDumpDao(): BrainDumpDao
    
    companion object {
        @Volatile
        private var INSTANCE: SpidePlanDatabase? = null
        
        fun getDatabase(context: Context): SpidePlanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpidePlanDatabase::class.java,
                    "spideplan_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
    
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
            super.onCreate(db)
            // Pre-populate with Spider-Man quotes
            // This will be handled by the repository
        }
    }
}