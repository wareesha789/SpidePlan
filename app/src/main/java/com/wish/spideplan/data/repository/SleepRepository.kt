package com.wish.spideplan.data.repository

import com.wish.spideplan.data.database.dao.SleepDao
import com.wish.spideplan.data.model.SleepEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SleepRepository @Inject constructor(
    private val sleepDao: SleepDao
) {
    
    fun getAllSleepEntries(): Flow<List<SleepEntry>> = sleepDao.getAllSleepEntries()
    
    suspend fun getSleepEntryForDate(date: LocalDate): SleepEntry? = 
        sleepDao.getSleepEntryForDate(date)
    
    fun getSleepEntryForDateFlow(date: LocalDate): Flow<SleepEntry?> = 
        sleepDao.getSleepEntryForDateFlow(date)
    
    fun getSleepEntriesInRange(startDate: LocalDate, endDate: LocalDate): Flow<List<SleepEntry>> = 
        sleepDao.getSleepEntriesInRange(startDate, endDate)
    
    fun getRecentSleepEntries(limit: Int = 7): Flow<List<SleepEntry>> = 
        sleepDao.getRecentSleepEntries(limit)
    
    suspend fun getAverageSleepDuration(startDate: LocalDate, endDate: LocalDate): Double? = 
        sleepDao.getAverageSleepDuration(startDate, endDate)
    
    suspend fun getAverageSleepQuality(startDate: LocalDate, endDate: LocalDate): Double? = 
        sleepDao.getAverageSleepQuality(startDate, endDate)
    
    suspend fun insertSleepEntry(sleepEntry: SleepEntry): Long = 
        sleepDao.insertSleepEntry(sleepEntry)
    
    suspend fun updateSleepEntry(sleepEntry: SleepEntry) = sleepDao.updateSleepEntry(sleepEntry)
    
    suspend fun deleteSleepEntry(sleepEntry: SleepEntry) = sleepDao.deleteSleepEntry(sleepEntry)
    
    suspend fun deleteSleepEntryById(id: Long) = sleepDao.deleteSleepEntryById(id)
    
    fun getSleepEntryCount(): Flow<Int> = sleepDao.getSleepEntryCount()
    
    suspend fun getTodaySleepEntry(): SleepEntry? {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return getSleepEntryForDate(today)
    }
    
    suspend fun getWeeklySleepStats(): SleepStats {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val weekAgo = today.minus(7, kotlinx.datetime.DateTimeUnit.DAY)
        
        val avgDuration = getAverageSleepDuration(weekAgo, today) ?: 0.0
        val avgQuality = getAverageSleepQuality(weekAgo, today) ?: 0.0
        
        return SleepStats(
            averageDurationHours = avgDuration / 60.0,
            averageQuality = avgQuality,
            period = "Last 7 days"
        )
    }
}

data class SleepStats(
    val averageDurationHours: Double,
    val averageQuality: Double,
    val period: String
)