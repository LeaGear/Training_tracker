package com.example.sporttracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporttracker.R
import com.example.sporttracker.ui.components.DropdownExerciseMenu
import com.example.sporttracker.ui.components.SetTargetWindow
import com.example.sporttracker.ui.components.SetTrackerCalendar
import com.example.sporttracker.ui.components.SimpleBarChart
import com.example.sporttracker.ui.theme.AppTheme
import com.example.sporttracker.ui.viewmodel.WorkoutViewModel

@Composable
fun HistoryCalendarScreen(viewModel: WorkoutViewModel){
    var showTargetDialog by remember { mutableStateOf(false) }


    val defaultTarget by viewModel.defaultTarget.collectAsState()
    val exercises by viewModel.availableExercises.collectAsState()
    val currentExercise by viewModel.exerciseName.collectAsState()
    val allWorkouts by viewModel.workoutByDate.collectAsState()
    val selectedLocalDate by viewModel.selectedLocalDate.collectAsState()
    val workoutData by viewModel.todayWorkout.collectAsState()
    val sets = workoutData?.sets ?: emptyList()
    val tmTarget = workoutData?.workout?.target ?: 0
    val target = if(tmTarget == 0) defaultTarget else tmTarget
    val currentMonth by viewModel.currentMonthData.collectAsState()
    //val target by viewModel.currentTarget.collectAsState()
    val currentTotal = sets.sumOf { it.reps }

    if (showTargetDialog) {
        SetTargetWindow(
            onDismiss = {showTargetDialog = false},
            onConfirm = {count ->
                viewModel.setTarget(count)
                showTargetDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {

        SetTrackerCalendar(
            selectedDate = selectedLocalDate,
            onDateSelected = { localDate -> viewModel.changeDate(localDate)},
            workoutByDate = allWorkouts
        )

        DropdownExerciseMenu(
            exercises = exercises,
            currentExercise = currentExercise,
            onExerciseSelected = { viewModel.changeExercise(it) },
            onExerciseDeleted = { viewModel.removeExercise(it) },
            onExerciseAdded = {
                viewModel.addExercise(it)
                viewModel.changeExercise(it)
            },
            modifier = Modifier
                .width(380.dp)
                .height(40.dp),
            menuModifier = Modifier.width(380.dp)
        )

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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(R.string.box_makes),
                        style = AppTheme.fonts.montBold,
                        fontSize = 26.sp,
                        color = Color.White
                    )
                    Text(
                        text = currentTotal.toString(),
                        style = AppTheme.fonts.montBold,
                        fontSize = 26.sp,
                        color = Color.White
                    )
                }
            }
            //Цель на день(изменяемое)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(AppTheme.shapes.mainShape)
                    .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape)
                    .background(AppTheme.colors.primaryButton)
                    .clickable{showTargetDialog = true},
                contentAlignment = Alignment.Center
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ){
                    Text(
                        text = stringResource(R.string.box_target),
                        style = AppTheme.fonts.montBold,
                        fontSize = 26.sp,
                        color = Color.White
                    )
                    Text(
                        text = target.toString(),
                        style = AppTheme.fonts.montBold,
                        fontSize = 26.sp,
                        color = Color.White
                    )
                }

            }
        }

        SimpleBarChart(currentMonth, defaultTarget)

        // Список упражнений за этот день
//        LazyColumn {
//            items(sets) { workoutSet ->
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp)
//                        .clip(AppTheme.shapes.mainShape)
//                        .background(AppTheme.colors.primaryButton)
//                        .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape)
//                        .padding(vertical = 12.dp), // <-- внутренний отступ для текста
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "${stringResource(R.string.str_for_reps)} -> ${workoutSet.reps}",
//                        style = AppTheme.fonts.montBold,
//                        fontSize = 26.sp,
//                        color = Color.White)
//                    }
//                }
//            }
        }
    }
