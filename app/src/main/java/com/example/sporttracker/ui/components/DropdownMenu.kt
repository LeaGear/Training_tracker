package com.example.sporttracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sporttracker.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    viewModel: WorkoutViewModel, // Передаем ViewModel параметром
    modifier: Modifier = Modifier,
    menuModifier: Modifier = Modifier
){
    var showAddDialog by remember { mutableStateOf(false) }

    val exercises by viewModel.availableExercises.collectAsState()
    val currentExercise by viewModel.exerciseName.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    //Выбор тренировки

    if (showAddDialog) {
        CreateWorkoutDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name->
                viewModel.addExercise(name) // Добавляем в список упражнений
                viewModel.changeExercise(name) // Сразу выбираем его
                showAddDialog = false
            }
        )
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = !expanded},
        modifier = modifier
    ){
        Box(
            modifier = Modifier
                .menuAnchor()
                .fillMaxSize()
                .clip(AppTheme.shapes.mainShape)
                .background(AppTheme.colors.primaryButton)
                .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = currentExercise,
                style = AppTheme.fonts.montBold,
                color = Color.White
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false},
            shape = AppTheme.shapes.mainShape,
            modifier = menuModifier
                //.clip(AppTheme.shapes.mainShape)
                .background(AppTheme.colors.primaryButton)
                //.border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape)
                .padding(10.dp)
        ){
            exercises.forEach { exercise ->
                DropdownMenuItem(
                    text = {
                        Text(
                            exercise.name,
                            style = AppTheme.fonts.montBold,
                            color = Color.White
                        )
                    },
                    trailingIcon = {
                        // Кнопка удаления упражнения из списка
                        IconButton(onClick = { viewModel.removeExercise(exercise.name) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить")
                        }
                    },
                    onClick = {
                        viewModel.changeExercise(exercise.name)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
            DropdownMenuItem(//modifier = Modifier
                //.border(AppTheme.shapes.primaryBorder),
                text = { Text("+ Добавить упражнение",
                    style = AppTheme.fonts.montBold,
                    color = Color.Green)},
                onClick = {
                    showAddDialog = true
                    expanded = false
                }
            )

        }

    }
}