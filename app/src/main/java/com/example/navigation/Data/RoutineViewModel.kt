package com.example.navigation.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RoutineRepository

    // Initialize repository first
    init {
        val db = AppDatabase.getDatabase(application)
        repository = RoutineRepository(db.routineDao())
    }

    // Now that the repository is initialized, you can assign routines
    val routines = repository.routines
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Functions to interact with the repository
    fun insertRoutine(routine: RoutineEntity) = viewModelScope.launch {
        repository.insert(routine)
    }

    fun updateRoutine(routine: RoutineEntity) = viewModelScope.launch {
        repository.update(routine)
    }

    fun deleteRoutine(routine: RoutineEntity) = viewModelScope.launch {
        repository.delete(routine)
    }
}
