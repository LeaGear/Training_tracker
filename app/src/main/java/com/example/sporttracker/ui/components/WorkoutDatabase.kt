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
    @Query("SELECT * FROM workout_history WHERE date = :date AND exerciseName = :name")
    fun getWorkoutWithSets(date: Long, name: String): Flow<WorkoutWithSets?>

    // 2. Если тебе нужно только число Цели (Target)
    @Query("SELECT target FROM workout_history WHERE date = :date AND exerciseName = :name")
    fun getTarget(date: Long, name: String): Flow<Int?>

    // 3. А вот с Суммой (Total) есть важный нюанс!
    // В твоей таблице Workout есть колонка total, но она "статична".
    // Если ты хочешь реальную сумму всех подходов из таблицы ExerciseSet:
    @Query("SELECT SUM(reps) FROM exercise_sets WHERE parentWorkoutId = :workoutId")
    fun getSumOfReps(workoutId: Int): Flow<Int?>

    // 4. Запросы для логики (suspend)
    @Query("SELECT id FROM workout_history WHERE date = :date AND exerciseName = :name LIMIT 1")
    fun getWorkoutIdOnce(date: Long, name: String): Int?

    @Query("DELETE FROM exercise_sets WHERE id = :setId")
    fun deleteSetById(setId: Int)

    @Query("SELECT * FROM exercise")
    fun getAllExercises(): Flow<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertExercise(exercise: Exercise)

    @Delete
    fun deleteExercise(exercise: Exercise)
    @Insert
    fun insertWorkout(workout: Workout): Long

    @Insert
    fun insertSet(set: ExerciseSet)
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
                    .fallbackToDestructiveMigration() // ДОБАВЬ ЭТУ СТРОКУ
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

fun getStartOfDay(timestamp: Long): Long {
    val calendar = java.util.Calendar.getInstance()
    calendar.timeInMillis = timestamp
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
    calendar.set(java.util.Calendar.MINUTE, 0)
    calendar.set(java.util.Calendar.SECOND, 0)
    calendar.set(java.util.Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}