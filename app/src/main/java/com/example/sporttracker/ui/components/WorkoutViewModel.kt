package com.example.sporttracker.ui.components

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = WorkoutDatabase.getDatabase(application).workoutDao()


    // 1. Поток данных для главного экрана (только за СЕГОДНЯ)
    val todaySets: Flow<List<WorkoutSet>> = dao.getSetsByDate(getStartOfDay(System.currentTimeMillis()))
    // Автоматически обновляемый список подходов для выбранной даты
    val selectedDateMillis = MutableStateFlow(
        LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    // Сеты подгружаются автоматически при смене даты
    val setsForSelectedDate = _selectedDate.flatMapLatest { date ->
        val millis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        dao.getSetsByDate(millis)
    }

    // 3. Запись (сразу в базу)
    fun recordSet(reps: Int){
        viewModelScope.launch(Dispatchers.IO){
            dao.insertSet(WorkoutSet(
                date = getStartOfDay((System.currentTimeMillis())),
                reps = reps,
                target = 500
            ))
        }
    }
    fun deleteSetById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id) // Тот самый запрос DELETE FROM ... WHERE id = :id
        }
    }

    //All times in one day
    fun totalInDay(date: Long): Flow<Int> {
        return dao.getSumByDate(getStartOfDay(date))
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }
}