package com.wish.spideplan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wish.spideplan.data.model.Quote
import com.wish.spideplan.data.model.Task
import com.wish.spideplan.data.model.QuoteCategory
import com.wish.spideplan.data.repository.TaskRepository
import com.wish.spideplan.data.repository.QuoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
data class HomeUiState(
    val todaysTasks: List<Task> = emptyList(),
    val overdueTasks: List<Task> = emptyList(),
    val dailyQuote: Quote? = null,
    val completedTasksToday: Int = 0,
    val totalTasksToday: Int = 0,
    val greeting: String = "",
    val isLoading: Boolean = true,
    val error: String? = null
)

class HomeViewModel(
    private val taskRepository: TaskRepository,
    private val quoteRepository: QuoteRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadHomeData()
    }
    
    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // Initialize quotes if needed
                quoteRepository.initializeDefaultQuotes()
                
                // Load daily quote
                val dailyQuote = quoteRepository.getRandomQuote()
                
                // Get current time for overdue check
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                
                // Combine flows for today's tasks and completed count
                combine(
                    taskRepository.getTodaysTasks(),
                    taskRepository.getTodayCompletedTaskCount(),
                    taskRepository.getOverdueTasks(now)
                ) { todaysTasks, completedCount, overdueTasks ->
                    _uiState.value = _uiState.value.copy(
                        todaysTasks = todaysTasks,
                        overdueTasks = overdueTasks,
                        dailyQuote = dailyQuote,
                        completedTasksToday = completedCount,
                        totalTasksToday = todaysTasks.size,
                        greeting = getGreeting(),
                        isLoading = false,
                        error = null
                    )
                }.collect { }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "An unexpected error occurred"
                )
            }
        }
    }
    
    fun completeTask(taskId: Long) {
        viewModelScope.launch {
            try {
                taskRepository.completeTask(taskId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to complete task"
                )
            }
        }
    }
    
    fun uncompleteTask(taskId: Long) {
        viewModelScope.launch {
            try {
                taskRepository.uncompleteTask(taskId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to uncomplete task"
                )
            }
        }
    }
    
    fun refreshQuote() {
        viewModelScope.launch {
            try {
                val newQuote = quoteRepository.getRandomQuote()
                _uiState.value = _uiState.value.copy(dailyQuote = newQuote)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to load new quote"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    private fun getGreeting(): String {
        val hour = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour
        return when (hour) {
            in 0..11 -> "Good morning, Spider!"
            in 12..17 -> "Good afternoon, Spider!"
            else -> "Good evening, Spider!"
        }
    }
}