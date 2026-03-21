package com.example.sporttracker.ui.components

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "workout_history")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int =0,
    val exerciseName: String,
    val date: Long,           // День, когда сделан подход
    val target: Int          // Цель (например, 100)
)

@Entity(tableName = "exercise_sets")
data class ExerciseSet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val parentWorkoutId: Int,
    val reps: Int
)

@Entity(tableName = "exercise")
data class Exercise(
    @PrimaryKey val name: String
)

data class WorkoutWithSets(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentWorkoutId"
    )
    val sets: List<ExerciseSet>
)
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

@Database(entities = [Workout::class, ExerciseSet::class, Exercise::class], version = 1)
abstract class WorkoutDatabase: RoomDatabase(){
    abstract fun workoutDao() : WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: WorkoutDatabase? = null

        fun getDatabase(context : Context) : WorkoutDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "workout_db"
                )
                    .fallbackToDestructiveMigration() // TODO: заменить на миграции перед релизом
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

fun getStartOfDay(timestamp: Long): Long {
    return java.time.Instant.ofEpochMilli(timestamp)
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalDate()
        .atStartOfDay(java.time.ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}