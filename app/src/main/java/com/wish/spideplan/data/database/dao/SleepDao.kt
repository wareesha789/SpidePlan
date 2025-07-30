package com.wish.spideplan.data.database.dao

import androidx.room.*
import com.wish.spideplan.data.model.SleepEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface SleepDao {
    
    @Query("SELECT * FROM sleep_entries ORDER BY date DESC")
    fun getAllSleepEntries(): Flow<List<SleepEntry>>
    
    @Query("SELECT * FROM sleep_entries WHERE date = :date")
    suspend fun getSleepEntryForDate(date: LocalDate): SleepEntry?
    
    @Query("SELECT * FROM sleep_entries WHERE date = :date")
    fun getSleepEntryForDateFlow(date: LocalDate): Flow<SleepEntry?>
    
    @Query("SELECT * FROM sleep_entries WHERE date >= :startDate AND date <= :endDate ORDER BY date ASC")
    fun getSleepEntriesInRange(startDate: LocalDate, endDate: LocalDate): Flow<List<SleepEntry>>
    
    @Query("SELECT * FROM sleep_entries ORDER BY date DESC LIMIT :limit")
    fun getRecentSleepEntries(limit: Int): Flow<List<SleepEntry>>
    
    @Query("SELECT AVG(CASE WHEN wakeTime >= bedTime THEN (wakeTime - bedTime) ELSE (24 * 60 - bedTime + wakeTime) END) FROM sleep_entries WHERE date >= :startDate AND date <= :endDate")
    suspend fun getAverageSleepDuration(startDate: LocalDate, endDate: LocalDate): Double?
    
    @Query("SELECT AVG(CAST(sleepQuality AS REAL)) FROM sleep_entries WHERE date >= :startDate AND date <= :endDate")
    suspend fun getAverageSleepQuality(startDate: LocalDate, endDate: LocalDate): Double?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSleepEntry(sleepEntry: SleepEntry): Long
    
    @Update
    suspend fun updateSleepEntry(sleepEntry: SleepEntry)
    
    @Delete
    suspend fun deleteSleepEntry(sleepEntry: SleepEntry)
    
    @Query("DELETE FROM sleep_entries WHERE id = :id")
    suspend fun deleteSleepEntryById(id: Long)
    
    @Query("SELECT COUNT(*) FROM sleep_entries")
    fun getSleepEntryCount(): Flow<Int>
}