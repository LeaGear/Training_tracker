package com.example.sporttracker.ui.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporttracker.R
import com.example.sporttracker.ui.components.WorkoutViewModel
import com.example.sporttracker.ui.components.AddSubtractButtons
import com.example.sporttracker.ui.components.CreateWorkoutDialog
import com.example.sporttracker.ui.components.DropdownMenu
import com.example.sporttracker.ui.theme.AppTheme
import com.example.sporttracker.ui.components.RecordButton
import com.example.sporttracker.ui.components.SetTargetWindow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PushUpCounterScreen(viewModel : WorkoutViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current
    val exerciseName by viewModel.exerciseName.collectAsState()
    val date by viewModel.selectedDate.collectAsState()

    val formattedDate = remember(date) {
        val sdf = java.text.SimpleDateFormat("dd.MM", java.util.Locale("ru"))
        sdf.format(java.util.Date(date))
    }
    var showTargetDialog by remember {mutableStateOf(false)}

    var count by remember {mutableIntStateOf(0)}
    var editingValue by remember { mutableStateOf(count.toString()) }
    LaunchedEffect(count) {
        editingValue = count.toString()
    }
    val workoutData by viewModel.todayWorkout.collectAsState()
    val targetInDay = workoutData?.workout?.target ?: 0
    val currentTotal = workoutData?.sets?.sumOf { it.reps } ?: 0
    val currentSets = workoutData?.sets ?: emptyList()

    if (showTargetDialog) {
        SetTargetWindow(
            onDismiss = {showTargetDialog = false},
            onConfirm = {count ->
                viewModel.setTarget(count)
                showTargetDialog = false
            }
        )
    }

    // ГЛАВНЫЙ ЭКРАН (Фон)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(interactionSource = remember { MutableInteractionSource() },
                indication = null){
                focusManager.clearFocus()
            }, // Глубокий темный фон
        contentAlignment = Alignment.Center
    ) {

        DropdownMenu(viewModel = viewModel,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 720.dp)
                .size(width = 300.dp, height = 40.dp),
                //.fillMaxWidth()
                //.height(40.dp)
            menuModifier = Modifier.width(300.dp)
            )


//        //Окно счета
//        Box(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(bottom = 600.dp)
//                .size(180.dp)
//                .clip(CircleShape)
//                .border(border = AppTheme.shapes.primaryBorder,
//                    shape = CircleShape)
//                .background(brush = AppTheme.colors.primaryButton),
//            contentAlignment = Alignment.Center
//        ){
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ){
//                Text(text = "$formattedDate",
//                    style = AppTheme.fonts.montBlack,
//                    fontSize = 30.sp,
//                    color = Color.White)
//                Spacer(modifier = Modifier.padding(8.dp))
//                Text(text = "$count",
//                    style = AppTheme.fonts.montBlack,
//                    fontSize = 55.sp,
//                    color = Color.White)
//            }
//
//        }

        //Окно счета
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 620.dp)
                .size(width = 140.dp,   height = 80.dp)
                .clip(RoundedCornerShape(35.dp))
                .border(border = AppTheme.shapes.primaryBorder,
                    shape = RoundedCornerShape(35.dp))
                .background(brush = AppTheme.colors.primaryButton),
            contentAlignment = Alignment.Center
        ){
            Text(text = "$count",
                style = AppTheme.fonts.montBlack,
                fontSize = 50.sp,
                color = Color.White)
        }

        //Окно с результатом за сегодня
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 520.dp)
                .offset(x = (-120).dp)
                .size(130.dp)
                .clip(CircleShape)
                .border(border = AppTheme.shapes.primaryBorder,
                    shape = CircleShape)
                .background(brush = AppTheme.colors.primaryButton),
            contentAlignment = Alignment.Center
        ){
            Column(
            horizontalAlignment = Alignment.CenterHorizontally
            ){
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Сделано: ",
                    style = AppTheme.fonts.montBold,
                    fontSize = 20.sp,
                    color = Color.White)
                Spacer(modifier = Modifier.padding(3.dp))
                Text("$currentTotal",
                    style = AppTheme.fonts.montBlack,
                    fontSize = 28.sp,
                    color = Color.White)
            }

        }
        //Окно с целью на сегодня
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 520.dp)
                .offset(x = 120.dp)
                .size(130.dp)
                .clip(CircleShape)
                .border(border = AppTheme.shapes.primaryBorder,
                    shape = CircleShape)
                .background(brush = AppTheme.colors.primaryButton)
                .clickable{showTargetDialog = true},
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Цель: ",
                    style = AppTheme.fonts.montBold,
                    fontSize = 20.sp,
                    color = Color.White)
                Spacer(modifier = Modifier.padding(3.dp))
                Text("$targetInDay",
                    style = AppTheme.fonts.montBlack,
                    fontSize = 28.sp,
                    color = Color.White)
            }

        }
        //куржочек с текущей датой
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 520.dp)
                .size(60.dp)
                .clip(CircleShape)
                .border(border = AppTheme.shapes.primaryBorder,
                    shape = CircleShape)
                .background(brush = AppTheme.colors.primaryButton),
            contentAlignment = Alignment.Center
        ){
            Text(text = "$formattedDate",
                style = AppTheme.fonts.montBlack,
                fontSize = 16.sp,
                color = Color.White)
        }
        //Контейнер количества подходов
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 350.dp)
                .size(width = 309.dp, height = 156.dp)
                .clip(AppTheme.shapes.mainShape)
                .background(Color.Transparent)
                .border(4.dp, Color(0xFfB1CBE5), AppTheme.shapes.mainShape)
        ){
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(currentSets){value ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(AppTheme.colors.primaryButton,
                                RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .border(3.dp,
                                Color(0xFfB1CBE5),
                                RoundedCornerShape(8.dp))
                            .combinedClickable(
                                onClick = { Toast.makeText(context, "Удерживайте для удаления",
                                    Toast.LENGTH_SHORT).show()},
                                onLongClick = {viewModel.deleteSetById(value.id)
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)}
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = value.reps.toString(),
                            style = AppTheme.fonts.montBold,
                            fontSize =  if (value.reps > 100) 17.sp else 20.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        //Верхняя кнопка для ручного ввода
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 240.dp) // Ширина в 2 раза больше высоты
                .size(width = 250.dp, height = 80.dp)
                .border(border = AppTheme.shapes.primaryBorder,
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                .clip(
                    RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp,
                        bottomStart = 0.dp, // Скругляем низ
                        bottomEnd = 0.dp
                    )
                )

                .background(brush = AppTheme.colors.primaryButton)
                .padding(8.dp),// Внутренний отступ для текста
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Введите вручную:",
                color = Color.Gray,
                fontSize = 13.sp,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.offset(y = (-25).dp)
            )
            BasicTextField(
                value = editingValue,
                onValueChange = { newValue ->
                    if (newValue.all {it.isDigit()} && newValue.length <= 3){
                        editingValue = newValue
                        if (newValue.isEmpty()){
                            count = 0
                        }
                        else {
                            newValue.toIntOrNull()?.let { parsed ->
                                count = parsed
                            }
                        }
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.fillMaxSize().offset(y = 15.dp)
            )
        }
        //Кнопки + - 10
        AddSubtractButtons(
            leftText = "-10",
            rightText = "+10",
            onLeftClick = {count = maxOf(0,  count - 10)},
            onRightClick = {count += 10},
            modifier = Modifier
                .align(Alignment.BottomCenter) // Прижимаем к центру низа
                .padding(bottom = 190.dp)
        )
        //Кнопки + - 5
        AddSubtractButtons(
            leftText = "-5",
            rightText = "+5",
            onLeftClick = {count = maxOf(0, count - 5)},
            onRightClick = {count += 5},
            modifier = Modifier
                .align(Alignment.BottomCenter) // Прижимаем к центру низа
                .padding(bottom = 140.dp),
        )

        //Кнопка записи - НИЖНЯЯ
        RecordButton(
            mainText = "ЗАПИСАТЬ",
            mainClick =  {viewModel.addSet(exerciseName, count, targetInDay)},
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 78.dp)
                .border(border = AppTheme.shapes.primaryBorder, // Тонкий светлый блик сверху
                    shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp)
                )
                .clip(
                    RoundedCornerShape(
                        bottomStart = 50.dp, // Скругляем низ
                        bottomEnd = 50.dp
                    )
                )

        )




    }
}