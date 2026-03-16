package com.example.sporttracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporttracker.R
import com.example.sporttracker.ui.theme.AppTheme
import com.example.sporttracker.ui.theme.SportTrackerTheme
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SetTrackerCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(50) }
    val endMonth = remember { currentMonth.plusMonths(50) }
    val daysOfWeek = remember { daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY) }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    Column(modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // Заголовок месяца (например: Март 2026)
        val visibleMonth = state.firstVisibleMonth.yearMonth
        val monthName = visibleMonth.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))

        //Месяц и год для заголовка
        val monthYearText = "${monthName.replaceFirstChar { it.uppercase() }} ${visibleMonth.year}"

        //Заголовок календаря с информацией про год и месяц
        Box(modifier = Modifier
            .size(height = 40.dp, width = 180.dp)
            .clip(AppTheme.shapes.mainShape)
            .background(AppTheme.colors.primaryButton, AppTheme.shapes.mainShape)
            .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = monthYearText,
                style = AppTheme.fonts.montBold,
                fontSize = 20.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        //Задний фон календаря, пока НЕУДАЧНЫЙ вариант
        Box(
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .clip(AppTheme.shapes.mainShape)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFBAC5D).copy(alpha = 0.1f),
                                Color(0xFFF28B31).copy(alpha = 0.1f)
                            )
                        )
                    )
                    .blur(20.dp)
                    .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape)
            )

            HorizontalCalendar(
                state = state,
                dayContent = { day ->
                    DayElement(
                        day = day,
                        isSelected = selectedDate == day.date,
                        onClick = { onDateSelected(it.date) }
                    )
                },
                //Верхняя часть калнедаря гле показаны дни недели
                monthHeader = {
                    Row(modifier = Modifier.fillMaxWidth().offset(y = 6.dp)) {
                        daysOfWeek.forEach { dayOfWeek ->
                            Text(
                                modifier = Modifier.weight(1f),
                                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).replaceFirstChar { it.uppercase() },
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                style = AppTheme.fonts.montBlack,
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            )
        }

    }
}

@Composable
fun DayElement(day: CalendarDay, isSelected: Boolean, onClick: (CalendarDay) -> Unit) {
    //Обьект кнопки даты
    Box(
        modifier = Modifier
            .aspectRatio(1f) // Квадратные ячейки
            .padding(4.dp)
            .clip(CircleShape)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        //Закрашывание цифры в зависиомсти от даты
        val textColor = when {
            isSelected -> Color.White
            day.position != DayPosition.MonthDate -> Color.LightGray
            else -> Color.Black
        }
        //Цвет ячейки в зависимости от результата за день

        val dateResult = when{
            isSelected -> Color.Green //Вот тут логика Тотал больше таргета
            else -> Color.Red
        }

        //Цвет выделенной даты
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = AppTheme.colors.primaryButton, CircleShape)
                    .border(width = 2.dp, color = Color(0xFFFF840B), CircleShape)// Твой оранжевый акцент
            )
        }
        //Цвет остальных дат
        else{
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFA4C3E0).copy(alpha = 0.4f), CircleShape)
                    //.border(width = 2.dp, color = Color(0xFFFF840B), CircleShape)// Твой оранжевый акцент
            )
        }
        //Дата в ячейке календаря
        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            style = AppTheme.fonts.montBold,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
        )
    }
}