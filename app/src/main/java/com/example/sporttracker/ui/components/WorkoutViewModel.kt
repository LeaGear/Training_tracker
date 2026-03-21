package com.example.sporttracker.ui.components

import android.app.Application
import android.content.Context
import androidx.compose.remote.creation.first
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import kotlin.collections.emptyList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class WorkoutViewModel(
    private val dao: WorkoutDao,
    application: Application,
) : AndroidViewModel(application){

    private val prefs = application.getSharedPreferences("workout_prefs", Context.MODE_PRIVATE)

    val selectedDate = MutableStateFlow(getStartOfDay(System.currentTimeMillis()))

    val exerciseName = MutableStateFlow(prefs.getString("last_exercise", null) ?: "Добавить")

    val availableExercises: StateFlow<List<Exercise>> = dao.getAllExercises()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val todayWorkout = combine(selectedDate, exerciseName) { date, name ->
        date to name
    }.flatMapLatest { (date, name) ->
        // Вызываем запрос к базе. Room сам вернет новый Flow при изменении данных
        dao.getWorkoutWithSets(date, name)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    @OptIn(ExperimentalCoroutinesApi::class)
    val allWorkouts: StateFlow<List<WorkoutWithSets>> = exerciseName
        .flatMapLatest { name -> dao.getAllWorkoutsWithSets(name) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val setsForSelectedDate = todayWorkout.map { it?.sets ?: emptyList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val targetForSelectedDate = todayWorkout.map { it?.workout?.target ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    fun changeExercise(name:String){
        exerciseName.value = name
        prefs.edit().putString("last_exercise", name).apply()
    }

    fun setTarget(target: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val today = selectedDate.value
            val name = exerciseName.value

            val workoutId: Int? = dao.getWorkoutIdOnce(today, name)
            if (workoutId == null) {
                dao.insertWorkout(Workout(exerciseName = name, date = today, target = target))
            } else {
                dao.updateTarget(today, name, target)
            }
        }
    }

    fun changeDate(newDate: Long){
        selectedDate.value = getStartOfDay(newDate)
    }

    fun addSet(exerciseName: String, repsCount:Int, target: Int){
        viewModelScope.launch(Dispatchers.IO){
            val today = selectedDate.value

            var workoutId = dao.getWorkoutIdOnce(today, exerciseName)

            if (workoutId == null){
                val newWorkout =Workout(
                    exerciseName = exerciseName,
                    date = today,
                    target  = target
                )
                workoutId = dao.insertWorkout(newWorkout).toInt()
            }

            val newSet = ExerciseSet(
                parentWorkoutId = workoutId!!,
                reps = repsCount
            )
            dao.insertSet(newSet)
        }
    }

    fun deleteSetById(setId: Int){
        viewModelScope.launch(Dispatchers.IO){
            dao.deleteSetById(setId)
        }
    }

    fun addExercise(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertExercise(Exercise(name))
        }
    }

    // Удаление
    fun removeExercise(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteExercise(Exercise(name))
            if (exerciseName.value == name){
                val remaining = dao.getAllExercisesOnce()
                val newName = remaining.lastOrNull()?.name ?: "Добавить"
                exerciseName.value = newName
                prefs.edit().putString("last_exercise", newName).apply()
            }
        }
    }
}

class WorkoutViewModelFactory(
    private val dao: WorkoutDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutViewModel(dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}