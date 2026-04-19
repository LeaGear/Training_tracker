package com.example.sporttracker.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var showDeleteDialog by remember {mutableStateOf(false)}
    var selectedExercise by remember { mutableStateOf("null") }
    var expanded by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = expanded, label = "MenuTransition")

    val scale by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 500, easing = FastOutSlowInEasing)
        },
        label = "Scale"
    ) { isExpanded ->
        if (isExpanded) 1f else 0.8f
    }
    val alpha by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 400, easing = LinearOutSlowInEasing)
        },
        label = "Alpha"
    ) { isExpanded ->
        if (isExpanded) 1f else 0f
    }
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
    if (showDeleteDialog){
        DeleteConfirmation(
            onConfirm = {
                onExerciseDeleted(selectedExercise)
                showDeleteDialog = false
            },
            onDismiss = {
                showDeleteDialog = false
            },
            exerciseName = selectedExercise
        )
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = !expanded},
        modifier = modifier
    ){
        //Box on main screen or calendar where containing current exercise name
        Box(
            modifier = Modifier
                .menuAnchor()
                .fillMaxSize()
                .clip(AppTheme.shapes.mainShape)
                .background(AppTheme.colors.primaryElementColor)
                .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape),
            contentAlignment = Alignment.Center
        ){
            //Name of current exercise
            Text(
                text = currentExercise,
                style = AppTheme.fonts.montBold,
                fontSize = 18.sp,
                color = Color.White
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false},
            shape = AppTheme.shapes.mainShape,
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = menuModifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                    transformOrigin = TransformOrigin(0.5f, 0.5f) // Origin at top-right (near gear icon)
                }
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            AppTheme.colors.settingsBack.copy(alpha = 0.9f),
                            Color.White.copy(alpha = 0.8f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.7f),
                            Color.Transparent
                        )
                    ),
                    AppTheme.shapes.mainShape
                )
                .padding(10.dp)
        ){
            exercises.forEach { exercise ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(AppTheme.shapes.mainShape)
                        .background(AppTheme.colors.dropMenuElement)
                        .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape)
                ){
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = exercise.name,
                                style = AppTheme.fonts.montBold,
                                fontSize = 16.sp,
                                color = AppTheme.colors.settingsBack
                            )
                        },
                        trailingIcon = {
                            // Кнопка удаления упражнения из списка
                            IconButton(onClick = {
                                selectedExercise = exercise.name
                                showDeleteDialog = true
                            }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete_confirm)
                                )
                            }
                        },
                        onClick = {
                            onExerciseSelected(exercise.name)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(AppTheme.shapes.mainShape)
                    .background(AppTheme.colors.dropMenuElement)
                    .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape)
            ) {
                DropdownMenuItem(//modifier = Modifier
                    //.border(AppTheme.shapes.primaryBorder),
                    text = {
                        Text(
                            stringResource(R.string.btn_add_exercise),
                            style = AppTheme.fonts.montBold,
                            fontSize = 14.sp,
                            color = AppTheme.colors.settingsBack
                        )
                    },
                    onClick = {
                        showAddDialog = true
                        expanded = false
                    }
                )
            }

        }

    }
}