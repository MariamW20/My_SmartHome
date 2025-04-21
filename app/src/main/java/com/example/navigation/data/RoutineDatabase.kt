package com.example.navigation.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoutineEntity::class], version = 1)
abstract class RoutineDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao

    companion object {
        @Volatile
        private var INSTANCE: RoutineDatabase? = null

        fun getDatabase(context: Context): RoutineDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoutineDatabase::class.java,
                    "routine_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


