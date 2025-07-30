package com.wish.spideplan.data.database.dao

import androidx.room.*
import com.wish.spideplan.data.model.Task
import com.wish.spideplan.data.model.TaskCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface TaskDao {
    
    @Query("SELECT * FROM tasks ORDER BY scheduledDateTime ASC")
    fun getAllTasks(): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY scheduledDateTime ASC")
    fun getIncompleteTasks(): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY completedAt DESC")
    fun getCompletedTasks(): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE DATE(scheduledDateTime) = DATE(:date) ORDER BY scheduledDateTime ASC")
    fun getTasksForDate(date: String): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE DATE(scheduledDateTime) = DATE('now', 'localtime') ORDER BY scheduledDateTime ASC")
    fun getTodaysTasks(): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE category = :category ORDER BY scheduledDateTime ASC")
    fun getTasksByCategory(category: TaskCategory): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND scheduledDateTime <= :dateTime ORDER BY scheduledDateTime ASC")
    fun getOverdueTasks(dateTime: LocalDateTime): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): Task?
    
    @Insert
    suspend fun insertTask(task: Task): Long
    
    @Update
    suspend fun updateTask(task: Task)
    
    @Delete
    suspend fun deleteTask(task: Task)
    
    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)
    
    @Query("UPDATE tasks SET isCompleted = :isCompleted, completedAt = :completedAt WHERE id = :id")
    suspend fun updateTaskCompletion(id: Long, isCompleted: Boolean, completedAt: LocalDateTime?)
    
    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 0")
    fun getIncompleteTaskCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1 AND DATE(completedAt) = DATE('now', 'localtime')")
    fun getTodayCompletedTaskCount(): Flow<Int>
    
    @Query("DELETE FROM tasks WHERE isCompleted = 1")
    suspend fun deleteAllCompletedTasks()
}