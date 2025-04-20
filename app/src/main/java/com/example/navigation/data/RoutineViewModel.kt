package com.example.navigation.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RoutineRepository
    val routines: StateFlow<List<RoutineEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).routineDao()
        repository = RoutineRepository(dao)
        routines = repository.routines.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

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