package com.example.sporttracker.ui.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporttracker.R
import com.example.sporttracker.ui.components.AddSubtractButtons
import com.example.sporttracker.ui.components.DropdownMenu
import com.example.sporttracker.ui.components.RecordButton
import com.example.sporttracker.ui.components.SetTargetWindow
import com.example.sporttracker.ui.components.WorkoutConstants.LARGE_REPS_THRESHOLD
import com.example.sporttracker.ui.components.WorkoutConstants.MAX_REPS_INPUT_LENGTH
import com.example.sporttracker.ui.components.WorkoutConstants.STEP_LARGE
import com.example.sporttracker.ui.components.WorkoutConstants.STEP_SMALL
import com.example.sporttracker.ui.components.WorkoutViewModel
import com.example.sporttracker.ui.theme.AppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PushUpCounterScreen(viewModel : WorkoutViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    val formattedDate by viewModel.formattedDate.collectAsState()

    var showTargetDialog by remember { mutableStateOf(false) }

    val count by viewModel.count.collectAsState()

    val workoutData by viewModel.todayWorkout.collectAsState()
    val targetInDay = workoutData?.workout?.target ?: 0
    val currentTotal = workoutData?.sets?.sumOf { it.reps } ?: 0
    val currentSets = workoutData?.sets ?: emptyList()

    if (showTargetDialog) {
        SetTargetWindow(
            onDismiss = { showTargetDialog = false },
            onConfirm = { count ->
                viewModel.setTarget(count)
                showTargetDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() }
    ) {
        // Верхняя зона
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            DropdownMenu(
                viewModel = viewModel,
                modifier = Modifier
                    .size(width = 300.dp, height = 40.dp),
                menuModifier = Modifier.width(300.dp)
            )
            Box(
                modifier = Modifier
                    .size(width = 140.dp, height = 80.dp)
                    .clip(RoundedCornerShape(35.dp))
                    .border(
                        border = AppTheme.shapes.primaryBorder,
                        shape = RoundedCornerShape(35.dp)
                    )
                    .background(brush = AppTheme.colors.primaryButton),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    style = AppTheme.fonts.montBlack,
                    fontSize = 50.sp,
                    color = Color.White
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Сделано
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .border(border = AppTheme.shapes.primaryBorder, shape = CircleShape)
                        .background(brush = AppTheme.colors.primaryButton),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.box_makes),
                            style = AppTheme.fonts.montBold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            text = currentTotal.toString(),
                            style = AppTheme.fonts.montBlack,
                            fontSize = 28.sp,
                            color = Color.White
                        )
                    }
                }

                // Дата
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(border = AppTheme.shapes.primaryBorder, shape = CircleShape)
                        .background(brush = AppTheme.colors.primaryButton),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = formattedDate,
                        style = AppTheme.fonts.montBlack,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                // Цель
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .border(border = AppTheme.shapes.primaryBorder, shape = CircleShape)
                        .background(brush = AppTheme.colors.primaryButton)
                        .clickable { showTargetDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.box_target),
                            style = AppTheme.fonts.montBold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            text = targetInDay.toString(),
                            style = AppTheme.fonts.montBlack,
                            fontSize = 28.sp,
                            color = Color.White
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(width = 309.dp, height = 156.dp)
                    .clip(AppTheme.shapes.mainShape)
                    .border(4.dp, Color(0xFfB1CBE5), AppTheme.shapes.mainShape)
            ) {
                val holdToDeleteText = stringResource(R.string.hold_to_delete)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(currentSets) { value ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(3.dp, Color(0xFfB1CBE5), RoundedCornerShape(8.dp))
                                .background(AppTheme.colors.primaryButton)
                                .combinedClickable(
                                    onClick = {
                                        Toast.makeText(
                                            context,
                                            holdToDeleteText,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onLongClick = {
                                        viewModel.deleteSetById(value.id)
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = value.reps.toString(),
                                style = AppTheme.fonts.montBold,
                                fontSize = if (value.reps > LARGE_REPS_THRESHOLD) 17.sp else 20.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

        }

        // Нижняя зона — фиксированная высота
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 250.dp, height = 80.dp)
                    .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                    .border(
                        border = AppTheme.shapes.primaryBorder,
                        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                    )
                    .background(brush = AppTheme.colors.primaryButton)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.hand_entering),
                    color = Color.Gray,
                    fontSize = 13.sp,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 4.dp)
                )
                BasicTextField(
                    value = count.toString(),
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= MAX_REPS_INPUT_LENGTH) {
                            viewModel.setCount(newValue.toIntOrNull() ?: 0)
                        }
                    },
                    textStyle = MaterialTheme.typography.displaySmall.copy(
                        color = Color.White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 38.sp
                    ),
                    cursorBrush = SolidColor(Color.White),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp)
                )
            }

            AddSubtractButtons(
                leftText = "-10",
                rightText = "+10",
                onLeftClick = { viewModel.incrementCount(-STEP_LARGE) },
                onRightClick = { viewModel.incrementCount(STEP_LARGE) }
            )

            AddSubtractButtons(
                leftText = "-5",
                rightText = "+5",
                onLeftClick = { viewModel.incrementCount(-STEP_SMALL) },
                onRightClick = { viewModel.incrementCount(STEP_SMALL) }
            )

            RecordButton(
                mainText = stringResource(R.string.btn_main_save),
                mainClick = { viewModel.addSet(count) },
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                    .border(
                        border = AppTheme.shapes.primaryBorder,
                        shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp)
                    )
            )
        }
    }
}
