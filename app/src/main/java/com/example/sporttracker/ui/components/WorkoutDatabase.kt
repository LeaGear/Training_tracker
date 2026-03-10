package com.example.sporttracker.ui.components

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Entity(tableName = "workout_history")
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true) val id: Int =0,
    val date: Long,
    val reps: Int,
    val target: Int
)

@Dao
interface WorkoutDao {
    @Insert
    fun insertSet(set: WorkoutSet)

    @Query("SELECT * FROM workout_history WHERE date = :date")
    fun getSetsByDate(date: Long): Flow<List<WorkoutSet>>

    @Query("DELETE FROM workout_history WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT SUM(reps) FROM workout_history WHERE date = :date")
    fun getSumByDate(date: Long): Flow<Int> // Может вернуть null, если записей нет

    //@Query("SELECT target FROM workout_history WHERE date = :date")
    //fun getDayTarget(date: Long)
}

@Database(entities = [WorkoutSet::class], version = 1)
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