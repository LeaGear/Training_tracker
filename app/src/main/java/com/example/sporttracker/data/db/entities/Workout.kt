package com.example.sporttracker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_history")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int =0,
    val exerciseName: String,
    val date: Long,           // День, когда сделан подход
    val target: Int          // Цель (например, 100)
)