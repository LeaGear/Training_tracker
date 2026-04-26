package com.example.sporttracker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_sets")
data class ExerciseSet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val parentWorkoutId: Int,
    val reps: Int
)
//TODO: If i want deploy this program, need to change parentWorkoutId and add cascade delete now program saving all data
