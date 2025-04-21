package com.example.navigation.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val routineDao = RoutineDatabase.getDatabase(application).routineDao()

    //expose routines as a reactive stream
    val routines: StateFlow<List<RoutineEntity>> = routineDao
        .getAllRoutines()
        .map { it.sortedBy { routine -> routine.id } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun insertRoutine(routine: RoutineEntity) {
        viewModelScope.launch {
            routineDao.insertRoutine(routine)
        }
    }

    fun updateRoutine(routine: RoutineEntity) {
        viewModelScope.launch {
            routineDao.updateRoutine(routine)
        }
    }

    fun deleteRoutine(routine: RoutineEntity) {
        viewModelScope.launch {
            routineDao.deleteRoutine(routine)
        }
    }
}
