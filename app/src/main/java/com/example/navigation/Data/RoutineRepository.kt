package com.example.navigation.data

import kotlinx.coroutines.flow.Flow

class RoutineRepository(private val dao: RoutineDao) {
    val routines: Flow<List<RoutineEntity>> = dao.getAll()

    suspend fun insert(routine: RoutineEntity) = dao.insert(routine)
    suspend fun update(routine: RoutineEntity) = dao.update(routine)
    suspend fun delete(routine: RoutineEntity) = dao.delete(routine)
}
