package com.wish.spideplan.data.repository

import com.wish.spideplan.data.database.dao.TaskDao
import com.wish.spideplan.data.model.Task
import com.wish.spideplan.data.model.TaskCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    
    fun getIncompleteTasks(): Flow<List<Task>> = taskDao.getIncompleteTasks()
    
    fun getCompletedTasks(): Flow<List<Task>> = taskDao.getCompletedTasks()
    
    fun getTasksForDate(date: String): Flow<List<Task>> = taskDao.getTasksForDate(date)
    
    fun getTodaysTasks(): Flow<List<Task>> = taskDao.getTodaysTasks()
    
    fun getTasksByCategory(category: TaskCategory): Flow<List<Task>> = 
        taskDao.getTasksByCategory(category)
    
    fun getOverdueTasks(dateTime: LocalDateTime): Flow<List<Task>> = 
        taskDao.getOverdueTasks(dateTime)
    
    suspend fun getTaskById(id: Long): Task? = taskDao.getTaskById(id)
    
    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)
    
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
    
    suspend fun deleteTaskById(id: Long) = taskDao.deleteTaskById(id)
    
    suspend fun updateTaskCompletion(id: Long, isCompleted: Boolean, completedAt: LocalDateTime?) = 
        taskDao.updateTaskCompletion(id, isCompleted, completedAt)
    
    fun getIncompleteTaskCount(): Flow<Int> = taskDao.getIncompleteTaskCount()
    
    fun getTodayCompletedTaskCount(): Flow<Int> = taskDao.getTodayCompletedTaskCount()
    
    suspend fun deleteAllCompletedTasks() = taskDao.deleteAllCompletedTasks()
    
    suspend fun completeTask(taskId: Long) {
        updateTaskCompletion(taskId, true, LocalDateTime.now())
    }
    
    suspend fun uncompleteTask(taskId: Long) {
        updateTaskCompletion(taskId, false, null)
    }
}