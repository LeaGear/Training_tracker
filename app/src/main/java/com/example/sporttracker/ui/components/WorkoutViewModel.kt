package com.example.sporttracker.ui.components

import android.app.Application
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import kotlin.collections.emptyList

class WorkoutViewModel(private val dao: WorkoutDao) : ViewModel(){

    private val _selectedDate = MutableStateFlow(getStartOfDay(System.currentTimeMillis()))

    val exerciseName = MutableStateFlow("Заглушка")

    val availableExercises: StateFlow<List<Exercise>> = dao.getAllExercises()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val setsForSelectedDate: Flow<List<ExerciseSet>> = combine(
        _selectedDate,
        exerciseName
    ){date, name -> date to name }
        .flatMapLatest {(date, name) ->
            dao.getWorkoutWithSets(date, name).map{ it?.sets ?: emptyList()}
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val targetForSelectedDate: Flow<Int> = combine(
        _selectedDate,
        exerciseName
    ) { date, name -> date to name }
        .flatMapLatest { (date, name) ->
            // Ищем запись о тренировке за конкретную дату и упражнение
            dao.getWorkoutWithSets(date, name).map { workoutWithSets ->
                // Если тренировка найдена — берем её цель, иначе 0
                workoutWithSets?.workout?.target ?: 0
            }
        }
    // В WorkoutViewModel.kt
    @OptIn(ExperimentalCoroutinesApi::class)
    val todayWorkout = combine(_selectedDate, exerciseName) { date, name ->
        date to name
    }.flatMapLatest { (date, name) ->
        // Вызываем запрос к базе. Room сам вернет новый Flow при изменении данных
        dao.getWorkoutWithSets(date, name)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    fun changeExercise(name:String){
        exerciseName.value = name
    }

    fun changeDate(newDate: Long){
        _selectedDate.value = getStartOfDay(newDate)
    }

    fun getTodayWorkout(name: String): StateFlow<WorkoutWithSets?>{
        val today = getStartOfDay(System.currentTimeMillis())
        return dao.getWorkoutWithSets(today, name)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
        )

    }

    fun addSet(exerciseName: String, repsCount:Int, target: Int){
        viewModelScope.launch(Dispatchers.IO){
            val today = getStartOfDay(System.currentTimeMillis())

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
        }
    }
}

class WorkoutViewModelFactory(private val dao: WorkoutDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}