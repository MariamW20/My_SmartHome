package com.example.navigation.data

import kotlinx.coroutines.flow.Flow

class RoutineRepository(private val routineDao: RoutineDao) {
    val routines: Flow<List<RoutineEntity>> = routineDao.getAllRoutines()

    suspend fun insert(routine: RoutineEntity) {
        routineDao.insert(routine)
    }

    suspend fun update(routine: RoutineEntity) {
        routineDao.update(routine)
    }

    suspend fun delete(routine: RoutineEntity) {
        routineDao.delete(routine)
    }
}