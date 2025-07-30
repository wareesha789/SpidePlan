package com.wish.spideplan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wish.spideplan.data.model.Task
import com.wish.spideplan.data.model.TaskCategory
import com.wish.spideplan.data.model.TaskPriority
import com.wish.spideplan.data.model.RecurringType
import com.wish.spideplan.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
data class AddTaskUiState(
    val title: String = "",
    val description: String = "",
    val category: TaskCategory = TaskCategory.PERSONAL,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val scheduledDateTime: LocalDateTime? = null,
    val isRecurring: Boolean = false,
    val recurringType: RecurringType? = null,
    val reminderMinutesBefore: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isTaskSaved: Boolean = false,
    val isEditMode: Boolean = false,
    val editingTaskId: Long? = null
)

class AddTaskViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddTaskUiState())
    val uiState: StateFlow<AddTaskUiState> = _uiState.asStateFlow()
    
    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }
    
    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }
    
    fun updateCategory(category: TaskCategory) {
        _uiState.value = _uiState.value.copy(category = category)
    }
    
    fun updatePriority(priority: TaskPriority) {
        _uiState.value = _uiState.value.copy(priority = priority)
    }
    
    fun updateScheduledDateTime(dateTime: LocalDateTime?) {
        _uiState.value = _uiState.value.copy(scheduledDateTime = dateTime)
    }
    
    fun updateIsRecurring(isRecurring: Boolean) {
        _uiState.value = _uiState.value.copy(
            isRecurring = isRecurring,
            recurringType = if (!isRecurring) null else _uiState.value.recurringType
        )
    }
    
    fun updateRecurringType(recurringType: RecurringType?) {
        _uiState.value = _uiState.value.copy(recurringType = recurringType)
    }
    
    fun updateReminderMinutesBefore(minutes: Int?) {
        _uiState.value = _uiState.value.copy(reminderMinutesBefore = minutes)
    }
    
    fun loadTaskForEdit(taskId: Long) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val task = taskRepository.getTaskById(taskId)
                
                if (task != null) {
                    _uiState.value = _uiState.value.copy(
                        title = task.title,
                        description = task.description,
                        category = task.category,
                        priority = task.priority,
                        scheduledDateTime = task.scheduledDateTime,
                        isRecurring = task.isRecurring,
                        recurringType = task.recurringType,
                        reminderMinutesBefore = task.reminderMinutesBefore,
                        isEditMode = true,
                        editingTaskId = taskId,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Task not found"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load task"
                )
            }
        }
    }
    
    fun saveTask() {
        val currentState = _uiState.value
        
        if (currentState.title.isBlank()) {
            _uiState.value = currentState.copy(error = "Task title cannot be empty")
            return
        }
        
        viewModelScope.launch {
            try {
                _uiState.value = currentState.copy(isLoading = true)
                
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                
                if (currentState.isEditMode && currentState.editingTaskId != null) {
                    // Update existing task
                    val existingTask = taskRepository.getTaskById(currentState.editingTaskId)
                    if (existingTask != null) {
                        val updatedTask = existingTask.copy(
                            title = currentState.title,
                            description = currentState.description,
                            category = currentState.category,
                            priority = currentState.priority,
                            scheduledDateTime = currentState.scheduledDateTime,
                            isRecurring = currentState.isRecurring,
                            recurringType = currentState.recurringType,
                            reminderMinutesBefore = currentState.reminderMinutesBefore,
                            updatedAt = now
                        )
                        taskRepository.updateTask(updatedTask)
                    }
                } else {
                    // Create new task
                    val newTask = Task(
                        title = currentState.title,
                        description = currentState.description,
                        category = currentState.category,
                        priority = currentState.priority,
                        scheduledDateTime = currentState.scheduledDateTime,
                        isRecurring = currentState.isRecurring,
                        recurringType = currentState.recurringType,
                        reminderMinutesBefore = currentState.reminderMinutesBefore,
                        createdAt = now,
                        updatedAt = now
                    )
                    taskRepository.insertTask(newTask)
                }
                
                _uiState.value = currentState.copy(
                    isLoading = false,
                    isTaskSaved = true,
                    error = null
                )
                
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to save task"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun resetForm() {
        _uiState.value = AddTaskUiState()
    }
}