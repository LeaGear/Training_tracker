package com.example.sporttracker

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class TrainingViewModel : ViewModel() {
    val sets = mutableStateListOf<Int>()

    var currentInputValue by mutableStateOf(0)
        private set

    var todayTotal by mutableStateOf(0)
        private set

    fun currentNull(){
        currentInputValue = 0
    }
    fun currentActual(actual : Int){
        currentInputValue = actual
    }
    fun changeInput(delta : Int){
        val newValue = currentInputValue + delta
        currentInputValue = if (newValue < 0) 0 else newValue
    }

    fun recordSet(){
        sets.add(currentInputValue)
        todayTotal += currentInputValue
        currentInputValue = 0
    }
    fun removeSetAt(index : Int){
        if (index in sets.indices){
            todayTotal -= sets[index]
            sets.removeAt(index)
        }
    }
}