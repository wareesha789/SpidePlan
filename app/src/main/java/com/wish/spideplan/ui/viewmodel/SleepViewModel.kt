package com.wish.spideplan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wish.spideplan.data.model.SleepEntry
import com.wish.spideplan.data.model.SleepQuality
import com.wish.spideplan.data.repository.SleepRepository
import com.wish.spideplan.data.repository.SleepStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.*
data class SleepUiState(
    val bedTime: LocalTime = LocalTime(22, 0), // Default 10 PM
    val wakeTime: LocalTime = LocalTime(7, 0), // Default 7 AM
    val sleepQuality: SleepQuality = SleepQuality.GOOD,
    val notes: String = "",
    val selectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val recentEntries: List<SleepEntry> = emptyList(),
    val weeklyStats: SleepStats? = null,
    val todayEntry: SleepEntry? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEntrySaved: Boolean = false
)

class SleepViewModel(
    private val sleepRepository: SleepRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SleepUiState())
    val uiState: StateFlow<SleepUiState> = _uiState.asStateFlow()
    
    init {
        loadSleepData()
    }
    
    private fun loadSleepData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                // Load today's entry
                val todayEntry = sleepRepository.getTodaySleepEntry()
                
                // Load recent entries
                sleepRepository.getRecentSleepEntries(7).collect { entries ->
                    _uiState.value = _uiState.value.copy(
                        recentEntries = entries,
                        todayEntry = todayEntry,
                        isLoading = false
                    )
                }
                
                // Load weekly stats
                val weeklyStats = sleepRepository.getWeeklySleepStats()
                _uiState.value = _uiState.value.copy(weeklyStats = weeklyStats)
                
                // If there's a today entry, populate the form
                todayEntry?.let { entry ->
                    _uiState.value = _uiState.value.copy(
                        bedTime = entry.bedTime,
                        wakeTime = entry.wakeTime,
                        sleepQuality = entry.sleepQuality,
                        notes = entry.notes
                    )
                }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load sleep data"
                )
            }
        }
    }
    
    fun updateBedTime(time: LocalTime) {
        _uiState.value = _uiState.value.copy(bedTime = time)
    }
    
    fun updateWakeTime(time: LocalTime) {
        _uiState.value = _uiState.value.copy(wakeTime = time)
    }
    
    fun updateSleepQuality(quality: SleepQuality) {
        _uiState.value = _uiState.value.copy(sleepQuality = quality)
    }
    
    fun updateNotes(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }
    
    fun updateSelectedDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        loadEntryForDate(date)
    }
    
    private fun loadEntryForDate(date: LocalDate) {
        viewModelScope.launch {
            try {
                val entry = sleepRepository.getSleepEntryForDate(date)
                if (entry != null) {
                    _uiState.value = _uiState.value.copy(
                        bedTime = entry.bedTime,
                        wakeTime = entry.wakeTime,
                        sleepQuality = entry.sleepQuality,
                        notes = entry.notes
                    )
                } else {
                    // Reset to defaults if no entry exists
                    _uiState.value = _uiState.value.copy(
                        bedTime = LocalTime(22, 0),
                        wakeTime = LocalTime(7, 0),
                        sleepQuality = SleepQuality.GOOD,
                        notes = ""
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to load sleep entry"
                )
            }
        }
    }
    
    fun saveSleepEntry() {
        val currentState = _uiState.value
        
        viewModelScope.launch {
            try {
                _uiState.value = currentState.copy(isLoading = true)
                
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                
                val sleepEntry = SleepEntry(
                    bedTime = currentState.bedTime,
                    wakeTime = currentState.wakeTime,
                    sleepQuality = currentState.sleepQuality,
                    notes = currentState.notes,
                    date = currentState.selectedDate,
                    createdAt = now
                )
                
                sleepRepository.insertSleepEntry(sleepEntry)
                
                _uiState.value = currentState.copy(
                    isLoading = false,
                    isEntrySaved = true,
                    error = null
                )
                
                // Reload data to update stats
                loadSleepData()
                
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to save sleep entry"
                )
            }
        }
    }
    
    fun deleteSleepEntry(entryId: Long) {
        viewModelScope.launch {
            try {
                sleepRepository.deleteSleepEntryById(entryId)
                loadSleepData() // Reload data
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to delete sleep entry"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun resetSavedState() {
        _uiState.value = _uiState.value.copy(isEntrySaved = false)
    }
}