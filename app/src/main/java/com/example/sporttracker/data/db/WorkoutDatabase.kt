package com.example.sporttracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sporttracker.BuildConfig
import com.example.sporttracker.data.db.entities.Exercise
import com.example.sporttracker.data.db.entities.ExerciseSet
import com.example.sporttracker.data.db.entities.Workout


@Database(entities = [Workout::class, ExerciseSet::class, Exercise::class], version = 1)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: WorkoutDatabase? = null

        fun getDatabase(context: Context): WorkoutDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "workout_db"
                ).apply {
                    if (BuildConfig.DEBUG) {
                        fallbackToDestructiveMigration()
                    } // TODO: заменить на миграции перед релизом
                }.build()
                INSTANCE = instance
                instance
            }
        }
    }
}
