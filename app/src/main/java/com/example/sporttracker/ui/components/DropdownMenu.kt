package com.example.sporttracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.sporttracker.R
import com.example.sporttracker.data.db.entities.Exercise
import com.example.sporttracker.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownExerciseMenu(
    exercises: List<Exercise>,
    currentExercise: String,
    onExerciseSelected: (String) -> Unit,
    onExerciseDeleted: (String) -> Unit,
    onExerciseAdded: (String) -> Unit,
    modifier: Modifier = Modifier,
    menuModifier: Modifier = Modifier
){
    var showAddDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    //Выбор тренировки

    if (showAddDialog) {
        CreateWorkoutDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name->
                onExerciseAdded(name)
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
                .background(AppTheme.colors.primaryElementColor)
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
                .background(AppTheme.colors.primaryElementColor)
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
                        IconButton(onClick = {onExerciseDeleted(exercise.name) }) {
                            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.description_delete))
                        }
                    },
                    onClick = {
                        onExerciseSelected(exercise.name)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
            DropdownMenuItem(//modifier = Modifier
                //.border(AppTheme.shapes.primaryBorder),
                text = { Text(
                    stringResource(R.string.btn_add_exercise),
                    style = AppTheme.fonts.montBold,
                    color = AppTheme.colors.calendarInProgressStart)},
                onClick = {
                    showAddDialog = true
                    expanded = false
                }
            )

        }

    }
}