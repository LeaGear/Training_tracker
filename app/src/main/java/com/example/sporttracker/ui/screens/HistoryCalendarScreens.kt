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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.sporttracker.ui.components.SetTargetWindow
import com.example.sporttracker.ui.theme.AppTheme
import com.example.sporttracker.ui.components.SetTrackerCalendar
import com.example.sporttracker.ui.theme.myCustomFontFamily

import java.time.Instant
import java.time.ZoneId

@Composable
fun HistoryCalendarScreen(viewModel: WorkoutViewModel){
    val selectedDateMillis by viewModel.selectedDate.collectAsState()
    val selectedLocalDate = Instant.ofEpochMilli(selectedDateMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    var showTargetDialog by remember { mutableStateOf(false) }
    val sets by viewModel.setsForSelectedDate.collectAsState(initial = emptyList())
    val currentTarget by  viewModel.targetForSelectedDate.collectAsState(initial = "")

    val exerciseName by remember {mutableStateOf("Push")}
    val currentTotal = sets.sumOf { it.reps }
    var showDialog by remember { mutableStateOf(false) }

    if (showTargetDialog) {
        SetTargetWindow(
            onDismiss = {showTargetDialog = false},
            onConfirm = {count ->
                viewModel.setTarget(count)
                showTargetDialog = false
            }
        )
    }


    if (showDialog) {
        CreateWorkoutDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name ->
                viewModel.changeExercise(name)
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
            },
            viewModel
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenu(viewModel = viewModel,
            modifier = Modifier
                .width(380.dp)
                .height(40.dp),
            menuModifier = Modifier.width(380.dp)
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
                onClick = { showTargetDialog = true},
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(AppTheme.shapes.mainShape)
                        .background(AppTheme.colors.primaryButton)
                        .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape)
                        .padding(vertical = 12.dp), // <-- внутренний отступ для текста
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                    text = "Сделано -> ${workoutSet.reps}",
                    fontFamily = myCustomFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = Color.White)
                    }
                }
            }
        }
    }
