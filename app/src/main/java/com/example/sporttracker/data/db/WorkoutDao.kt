package com.example.sporttracker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.sporttracker.data.db.entities.Exercise
import com.example.sporttracker.data.db.entities.ExerciseSet
import com.example.sporttracker.data.db.entities.Workout
import com.example.sporttracker.data.db.entities.WorkoutWithSets
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    // 1. Получаем ВСЁ разом (самый эффективный способ для UI)
    @Transaction
    @Query("SELECT * FROM workout_history WHERE exerciseName = :name")
    fun getAllWorkoutsWithSets(name: String): Flow<List<WorkoutWithSets>>

    @Transaction
    @Query("SELECT * FROM workout_history WHERE date = :date AND exerciseName = :name")
    fun getWorkoutWithSets(date: Long, name: String): Flow<WorkoutWithSets?>

    // 4. Запросы для логики (suspend)
    @Query("SELECT id FROM workout_history WHERE date = :date AND exerciseName = :name LIMIT 1")
    suspend fun getWorkoutIdOnce(date: Long, name: String): Int?

    @Query("DELETE FROM exercise_sets WHERE id = :setId")
    suspend fun deleteSetById(setId: Int)

    @Query("UPDATE workout_history SET target = :target WHERE date = :date AND exerciseName = :name")
    suspend fun updateTarget(date: Long, name: String, target: Int)

    @Query("SELECT * FROM exercise")
    fun getAllExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercise")
    suspend fun getAllExercisesOnce(): List<Exercise>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercise(exercise: Exercise)


    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Insert
    suspend fun insertWorkout(workout: Workout): Long

    @Insert
    suspend fun insertSet(set: ExerciseSet)

}