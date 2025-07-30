package com.wish.spideplan.data.repository

import com.wish.spideplan.data.database.dao.BrainDumpDao
import com.wish.spideplan.data.model.BrainDump
import kotlinx.coroutines.flow.Flow
class BrainDumpRepository(
    private val brainDumpDao: BrainDumpDao
) {
    
    fun getActiveBrainDumps(): Flow<List<BrainDump>> = brainDumpDao.getActiveBrainDumps()
    
    fun getArchivedBrainDumps(): Flow<List<BrainDump>> = brainDumpDao.getArchivedBrainDumps()
    
    fun getAllBrainDumps(): Flow<List<BrainDump>> = brainDumpDao.getAllBrainDumps()
    
    suspend fun getBrainDumpById(id: Long): BrainDump? = brainDumpDao.getBrainDumpById(id)
    
    fun searchBrainDumps(searchQuery: String): Flow<List<BrainDump>> = 
        brainDumpDao.searchBrainDumps(searchQuery)
    
    suspend fun insertBrainDump(brainDump: BrainDump): Long = 
        brainDumpDao.insertBrainDump(brainDump)
    
    suspend fun updateBrainDump(brainDump: BrainDump) = brainDumpDao.updateBrainDump(brainDump)
    
    suspend fun deleteBrainDump(brainDump: BrainDump) = brainDumpDao.deleteBrainDump(brainDump)
    
    suspend fun deleteBrainDumpById(id: Long) = brainDumpDao.deleteBrainDumpById(id)
    
    suspend fun updateBrainDumpArchiveStatus(id: Long, isArchived: Boolean) = 
        brainDumpDao.updateBrainDumpArchiveStatus(id, isArchived)
    
    suspend fun deleteAllArchivedBrainDumps() = brainDumpDao.deleteAllArchivedBrainDumps()
    
    fun getActiveBrainDumpCount(): Flow<Int> = brainDumpDao.getActiveBrainDumpCount()
    
    suspend fun archiveBrainDump(id: Long) {
        updateBrainDumpArchiveStatus(id, true)
    }
    
    suspend fun unarchiveBrainDump(id: Long) {
        updateBrainDumpArchiveStatus(id, false)
    }
}