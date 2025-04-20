package com.example.navigation.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val time: String, // Stored as formatted string like "8:00 AM"
    val recurrence: String // E.g., "Week days", "Weekend"
)