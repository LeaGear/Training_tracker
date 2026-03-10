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
import com.example.sporttracker.ui.theme.AppTheme
import com.example.sporttracker.ui.components.SetTrackerCalendar

@Composable
fun HistoryCalendarScreen(viewModel: WorkoutViewModel){
    val sets by viewModel.setsForSelectedDate.collectAsState(initial = emptyList())
    val selectedDate by viewModel.selectedDate.collectAsState()
    val total = sets.sumOf { it.reps }
    //val target by viewModel.
    val myCustomFontFamily = FontFamily(
        Font(R.font.montserrat_regular, FontWeight.Normal),
        Font(R.font.montserrat_bold, FontWeight.Bold),
        Font(R.font.montserrat_black, FontWeight.Black)
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // 2. ПЕРЕДАЕМ СУЩЕСТВУЮЩУЮ VIEWMODEL (не создаем новую!)

        SetTrackerCalendar(
            selectedDate = selectedDate,
            onDateSelected = { newDate ->
                viewModel.onDateSelected(newDate)
            }
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
                    .clip(RoundedCornerShape(12.dp))
                    .border(border = AppTheme.colors.primaryBorder,
                        shape = RoundedCornerShape(12.dp))
                    .background(brush = AppTheme.colors.primaryButton),
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
                    text = "$total",
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
                onClick = {setTarget(total, 1.0f)},
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(12.dp)
            ){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = AppTheme.colors.primaryButton)
                        .border(border = AppTheme.colors.primaryBorder,
                            shape = RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp)),
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
                        text = "$total",
                        fontFamily = myCustomFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Color.White
                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
