package com.example.sporttracker.ui.components

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


class WorkoutViewModel(
    private val dao: WorkoutDao,
    application: Application,
) : AndroidViewModel(application){

    private val prefs = application.getSharedPreferences("workout_prefs", Context.MODE_PRIVATE)

    // --- Стейт ---
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    private val _selectedDate = MutableStateFlow(getStartOfDay(System.currentTimeMillis()))
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    private val _exerciseName = MutableStateFlow(prefs.getString("last_exercise", null) ?: "Добавить")
    val exerciseName: StateFlow<String> = _exerciseName.asStateFlow()

    // --- Производные данные ---

    // Форматированная дата — готовая строка для UI
    val formattedDate: StateFlow<String> = _selectedDate
        .map { date ->
            val localDate = Instant
                .ofEpochMilli(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            java.time.format.DateTimeFormatter
                .ofPattern("dd.MM")
                .format(localDate)
        }
        .stateIn(
            scope = viewModelScope,        // живёт пока жив ViewModel
            started = SharingStarted.Eagerly,
            initialValue = ""
        )

    val selectedLocalDate: StateFlow<LocalDate> = _selectedDate
        .map { millis ->
            Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, LocalDate.now())
    val availableExercises: StateFlow<List<Exercise>> = dao.getAllExercises()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val todayWorkout = combine(_selectedDate, _exerciseName) { date, name ->
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
    val allWorkouts: StateFlow<List<WorkoutWithSets>> = _exerciseName
        .flatMapLatest { name -> dao.getAllWorkoutsWithSets(name) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workoutByDate: StateFlow<Map<LocalDate, Pair<Int, Int>>> = allWorkouts
        .map { list ->
            list.associate { wws ->
                val date = java.time.Instant.ofEpochMilli(wws.workout.date)
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate()
                date to Pair(wws.sets.sumOf { it.reps }, wws.workout.target)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())


    // --- Команды ---

    //Изменение выбора упражнения
    fun addExercise(name: String) {
        viewModelScope.launch {
            dao.insertExercise(Exercise(name))
        }
    }
    fun changeExercise(name:String){
        _exerciseName.value = name
        prefs.edit().putString("last_exercise", name).apply()
    }
    // Удаление
    fun removeExercise(name: String) {
        viewModelScope.launch {
            dao.deleteExercise(Exercise(name))
            if (_exerciseName.value == name){
                val remaining = dao.getAllExercisesOnce()
                val newName = remaining.lastOrNull()?.name ?: "Добавить"
                _exerciseName.value = newName
                prefs.edit().putString("last_exercise", newName).apply()
            }
        }
    }

    //Изменение текущей даты
    fun changeDate(localDate: LocalDate) {
        _selectedDate.value = localDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    //Установить цель на день
    fun setTarget(target: Int) {
        viewModelScope.launch {
            val today = _selectedDate.value
            val name = _exerciseName.value

            val workoutId: Int? = dao.getWorkoutIdOnce(today, name)
            if (workoutId == null) {
                dao.insertWorkout(Workout(exerciseName = name, date = today, target = target))
            } else {
                dao.updateTarget(today, name, target)
            }
        }
    }

    //Добавить подход
    fun addSet(repsCount:Int){
        viewModelScope.launch{
            val date = _selectedDate.value
            val name = _exerciseName.value
            val target = todayWorkout.value?.workout?.target ?: 0

            val workoutId = dao.getWorkoutIdOnce(date, name)
                ?: dao.insertWorkout(
                    Workout(exerciseName = name, date = date, target = target)
                ).toInt()

            dao.insertSet(ExerciseSet(parentWorkoutId = workoutId, reps = repsCount))
        }
    }

    //Удалить подход
    fun deleteSetById(setId: Int){
        viewModelScope.launch{
            dao.deleteSetById(setId)
        }
    }


    // Функции для изменения count
    fun setCount(value: Int) {
        _count.value = value.coerceAtLeast(0) // никогда не уйдёт в минус
    }

    fun incrementCount(by: Int) {
        _count.value = (_count.value + by).coerceAtLeast(0)
    }

    fun resetCount() {
        _count.value = 0
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