package com.example.sporttracker

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sporttracker.ui.components.WorkoutDatabase
import com.example.sporttracker.ui.components.WorkoutSet
import com.example.sporttracker.ui.components.getStartOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch


class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = WorkoutDatabase.getDatabase(application).workoutDao()

    // 2. Поток данных для экрана истории (за ВЫБРАННЫЙ день)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _selectedDate = MutableStateFlow(getStartOfDay(System.currentTimeMillis()))

    // 1. Поток данных для главного экрана (только за СЕГОДНЯ)
    val todaySets: Flow<List<WorkoutSet>> = dao.getSetsByDate(getStartOfDay(System.currentTimeMillis()))
    // Автоматически обновляемый список подходов для выбранной даты
    @OptIn(ExperimentalCoroutinesApi::class)
    val setsForSelectedDate: Flow<List<WorkoutSet>> = _selectedDate.flatMapLatest { date ->
        dao.getSetsByDate(date)
    }

    // 3. Запись (сразу в базу)
    fun recordSet(reps: Int){
        viewModelScope.launch(Dispatchers.IO){
            dao.insertSet(WorkoutSet(
                date = getStartOfDay((System.currentTimeMillis())),
                reps = reps
            ))
        }
    }
    fun deleteSetById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id) // Тот самый запрос DELETE FROM ... WHERE id = :id
        }
    }

    //All times in one day
    fun totalInDay(date: Long): Flow<Int>{
        return dao.getSumByDate(getStartOfDay(date))
    }


    // Метод для смены даты (вызывается при клике в календаре)
    fun onDateSelected(newDate: Long) {
        _selectedDate.value = getStartOfDay(newDate)
    }
}