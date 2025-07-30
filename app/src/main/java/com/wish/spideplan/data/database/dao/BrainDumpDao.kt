package com.wish.spideplan.data.database.dao

import androidx.room.*
import com.wish.spideplan.data.model.BrainDump
import kotlinx.coroutines.flow.Flow

@Dao
interface BrainDumpDao {
    
    @Query("SELECT * FROM brain_dumps WHERE isArchived = 0 ORDER BY updatedAt DESC")
    fun getActiveBrainDumps(): Flow<List<BrainDump>>
    
    @Query("SELECT * FROM brain_dumps WHERE isArchived = 1 ORDER BY updatedAt DESC")
    fun getArchivedBrainDumps(): Flow<List<BrainDump>>
    
    @Query("SELECT * FROM brain_dumps ORDER BY updatedAt DESC")
    fun getAllBrainDumps(): Flow<List<BrainDump>>
    
    @Query("SELECT * FROM brain_dumps WHERE id = :id")
    suspend fun getBrainDumpById(id: Long): BrainDump?
    
    @Query("SELECT * FROM brain_dumps WHERE content LIKE '%' || :searchQuery || '%' AND isArchived = 0 ORDER BY updatedAt DESC")
    fun searchBrainDumps(searchQuery: String): Flow<List<BrainDump>>
    
    @Insert
    suspend fun insertBrainDump(brainDump: BrainDump): Long
    
    @Update
    suspend fun updateBrainDump(brainDump: BrainDump)
    
    @Delete
    suspend fun deleteBrainDump(brainDump: BrainDump)
    
    @Query("DELETE FROM brain_dumps WHERE id = :id")
    suspend fun deleteBrainDumpById(id: Long)
    
    @Query("UPDATE brain_dumps SET isArchived = :isArchived WHERE id = :id")
    suspend fun updateBrainDumpArchiveStatus(id: Long, isArchived: Boolean)
    
    @Query("DELETE FROM brain_dumps WHERE isArchived = 1")
    suspend fun deleteAllArchivedBrainDumps()
    
    @Query("SELECT COUNT(*) FROM brain_dumps WHERE isArchived = 0")
    fun getActiveBrainDumpCount(): Flow<Int>
}