package com.example.sporttracker.data.db.entities

import androidx.room.Embedded
import androidx.room.Relation


data class WorkoutWithSets(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentWorkoutId"
    )
    val sets: List<ExerciseSet>
)