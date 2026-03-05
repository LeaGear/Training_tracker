package com.example.sporttracker

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

import com.example.sporttracker.ui.components.AddSubtractButtons
import com.example.sporttracker.ui.components.RecordButton


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: TrainingViewModel = viewModel()
            // Обернем в тему, чтобы текст выглядел правильно
            MaterialTheme {
                PushUpCounterScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PushUpCounterScreen(viewModel : TrainingViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    val MyCustomFontFamily = FontFamily(
        Font(R.font.montserrat_regular, FontWeight.Normal),
        Font(R.font.montserrat_bold, FontWeight.Bold),
        Font(R.font.montserrat_black, FontWeight.Black)
    )
    // Логика даты для ключа
    val countKey = "count_${LocalDate.now()}"

    var total = viewModel.todayTotal
    var countNow = viewModel.currentInputValue

    var editingValue by remember { mutableStateOf(countNow.toString()) }
    LaunchedEffect(countNow) {
        editingValue = countNow.toString()
    }


    // ГЛАВНЫЙ ЭКРАН (Фон)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB8D0E7))
            .clickable(interactionSource = remember { MutableInteractionSource() },
                indication = null){
                focusManager.clearFocus()
            }, // Глубокий темный фон
        contentAlignment = Alignment.Center
    ) {

        //Окно счета
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 570.dp)
                .size(240.dp)
                .clip(CircleShape)
                .background(Color(0xFFFA9D4A)),
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "$countNow",
                    fontFamily = MyCustomFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 55.sp,
                    color = Color.White)
                Spacer(modifier = Modifier.padding(10.dp))
                Text("Today total: $total",
                    fontFamily = MyCustomFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White)
            }

        }
        //Контейнер количества подходов
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 380.dp)
                .size(width = 309.dp, height = 156.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFCADCED))
                .border(3.dp, Color(0xFfB1CBE5), RoundedCornerShape(10.dp))
        ){
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(viewModel.sets){ index, value ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(Color(0xFFCADCED), RoundedCornerShape(8.dp))
                            .border(3.dp,
                                Color(0xFfB1CBE5),
                                RoundedCornerShape(8.dp))
                            .combinedClickable(
                                onClick = { Toast.makeText(context, "Удерживайте для удаления", Toast.LENGTH_SHORT).show()},
                                onLongClick = {viewModel.removeSetAt(index)
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)}
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = value.toString(),
                            fontFamily = MyCustomFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        //Верхняя кнопка для очистки ввода
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 320.dp)
                .offset(x = 115.dp)
                .size(30.dp)
                .clip(CircleShape)
                .border(width = 2.dp,
                    color = Color.Red,
                    shape = CircleShape)
                .clickable{viewModel.currentNull()},
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.clear), // Твоя картинка из res/drawable
                contentDescription = "Описание для слабовидящих",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Важно: обрезает фото, чтобы оно заполнило весь Box без искажений
            )
        }

        //Верхняя кнопка для ручного ввода
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 250.dp) // Ширина в 2 раза больше высоты
                .size(width = 250.dp, height = 80.dp)
                .border(width = 4.dp,
                    color = Color(0xFFDCB07A),
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                .clip(
                    RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp,
                        bottomStart = 0.dp, // Скругляем низ
                        bottomEnd = 0.dp
                    )
                )

                .background(brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFBAC5D), // Чуть светлее сверху
                        Color(0xFFF28B31)  // Чуть темнее снизу
                    )
                )
                )
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
                    if (newValue.all {it.isDigit()} && newValue.length <= 4){
                        editingValue = newValue
                        if (newValue.isEmpty()){
                            viewModel.currentNull()
                        }
                        else {
                            newValue.toIntOrNull()?.let { parsed ->
                                viewModel.currentActual(parsed)
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
            onLeftClick = {viewModel.changeInput(-10)},
            onRightClick = {viewModel.changeInput(10)},
            modifier = Modifier
                .align(Alignment.BottomCenter) // Прижимаем к центру низа
                .padding(bottom = 200.dp)
        )
        //Кнопки + - 5
        AddSubtractButtons(
            leftText = "-5",
            rightText = "+5",
            onLeftClick = {viewModel.changeInput(-5)},
            onRightClick = {viewModel.changeInput(5)},
            modifier = Modifier
                .align(Alignment.BottomCenter) // Прижимаем к центру низа
                .padding(bottom = 150.dp),
        )

        //Кнопка записи - НИЖНЯЯ
        RecordButton(
            mainText = "ЗАПИСАТЬ",
            mainClick =  {viewModel.recordSet()},
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 88.dp)
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.3f), // Тонкий светлый блик сверху
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

@Composable
fun SetsGrid(sets : List<Int>){

}















