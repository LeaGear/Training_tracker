package com.example.sporttracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporttracker.R
import com.example.sporttracker.ui.components.WorkoutViewModel
import com.example.sporttracker.setTarget
import com.example.sporttracker.ui.components.CreateWorkoutDialog
import com.example.sporttracker.ui.components.DropdownMenu
import com.example.sporttracker.ui.theme.AppTheme
import com.example.sporttracker.ui.components.SetTrackerCalendar
import com.example.sporttracker.ui.theme.myCustomFontFamily

import java.time.Instant
import java.time.ZoneId

@Composable
fun HistoryCalendarScreen(viewModel: WorkoutViewModel){
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedDateMillis by viewModel.selectedDate.collectAsState()
    val selectedLocalDate = Instant.ofEpochMilli(selectedDateMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val sets by viewModel.setsForSelectedDate.collectAsState(initial = emptyList())
    val currentTarget by  viewModel.targetForSelectedDate.collectAsState(initial = "")

    val exerciseName by remember {mutableStateOf("Push")}
    val currentTotal = sets.sumOf { it.reps }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        CreateWorkoutDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name -> // Исправлено: принимаем 3 параметра
                viewModel.changeExercise(name)
                // Если у тебя во ViewModel есть метод сохранения цели:
                // viewModel.saveNewGoal(name, target)
                showDialog = false
            }
        )
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        SetTrackerCalendar(
            selectedDate = selectedLocalDate, // Теперь передаем LocalDate
            onDateSelected = { localDate ->
                val millis = localDate.atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
                viewModel.changeDate(millis)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Кнопка выбора тренировки
//        Button(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(40.dp),
//            onClick = {setTarget(currentTotal, 1.0f)},
//            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
//            contentPadding = PaddingValues(0.dp),
//            shape = AppTheme.shapes.mainShape
//        ){
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(AppTheme.colors.primaryButton, AppTheme.shapes.mainShape)
//                    .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape),
//                contentAlignment = Alignment.Center
//            ){
//              Text(exerciseName)
//            }
//        }
        DropdownMenu(viewModel = viewModel,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Здесь можно добавить список подходов (LazyColumn) ниже календаря
        //Табличка количества за день и цели за день с возможностью изменения
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ){
            //Количество всего за день(не изменяемое)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(AppTheme.shapes.mainShape)
                    .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape)
                    .background(AppTheme.colors.primaryButton),
                contentAlignment = Alignment.Center
            ){
                Text(modifier = Modifier.offset(y = (-15).dp),
                    text = "Сделано:",
                    fontFamily = myCustomFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color.White
                )
                Text(modifier = Modifier.offset(y = 15.dp),
                    text = "$currentTotal",
                    fontFamily = myCustomFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color.White
                )
            }
            //Цель на день(изменяемое)
            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                onClick = { showDialog = true},
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = AppTheme.shapes.mainShape
            ){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.primaryButton)
                        .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape)
                        .clip(AppTheme.shapes.mainShape),
                    contentAlignment = Alignment.Center
                ){
                    Text(modifier = Modifier.offset(y = (-15).dp),
                        text = "Цель:",
                        fontFamily = myCustomFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Color.White
                    )
                    Text(modifier = Modifier.offset(y = 15.dp),
                        text = "$currentTarget",
                        fontFamily = myCustomFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Color.White
                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Список упражнений за этот день
        LazyColumn {
            items(sets) { workoutSet ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Text("Сделано", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(" -> ", color = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${workoutSet.reps} повт.")
                    }
                }
            }
        }
    }
}
