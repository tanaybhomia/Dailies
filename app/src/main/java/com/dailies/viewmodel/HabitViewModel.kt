package com.dailies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dailies.data.Habit
import com.dailies.data.HabitRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class HabitViewModel(private val repository: HabitRepository) : ViewModel() {

    val allHabits: StateFlow<List<Habit>> = repository.allHabits
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insert(habit: Habit) = viewModelScope.launch {
        repository.insert(habit)
    }

    fun update(habit: Habit) = viewModelScope.launch {
        repository.update(habit)
    }

    fun delete(habit: Habit) = viewModelScope.launch {
        repository.delete(habit)
    }

    fun markHabitCompleted(habit: Habit) {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val newCompletions = habit.currentCompletions + 1
        var newStreak = habit.streak
        var lastCompletedDate = habit.lastCompletedDate
        
        // Logic for streak calculation
        if (habit.lastCompletedDate >= today) {
            // Already completed today partially or fully
        } else {
            // Check if streak is broken (last completed before yesterday)
            val yesterday = today - 86400000L
            if (habit.lastCompletedDate < yesterday && habit.lastCompletedDate > 0) {
                newStreak = 0 // Reset streak
            }
        }
        
        if (newCompletions == habit.targetCompletions) {
            lastCompletedDate = System.currentTimeMillis()
            if (habit.lastCompletedDate < today) {
                newStreak++
            }
        }

        val updatedHabit = habit.copy(
            currentCompletions = if (newCompletions > habit.targetCompletions) habit.targetCompletions else newCompletions,
            totalCompletions = habit.totalCompletions + 1,
            streak = newStreak,
            lastCompletedDate = lastCompletedDate
        )
        update(updatedHabit)
    }
    
    fun resetDailyProgress() = viewModelScope.launch {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        
        allHabits.value.forEach { habit ->
            if (habit.lastCompletedDate < today && habit.currentCompletions > 0) {
                 update(habit.copy(currentCompletions = 0))
            }
        }
    }
}

class HabitViewModelFactory(private val repository: HabitRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
