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
    onDateSelected: (LocalDate) -> Unit,
    viewModel: WorkoutViewModel
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

    val allWorkouts by viewModel.allWorkouts.collectAsState()

// Строим Map<LocalDate, Pair<Int,Int>> — total и target для каждой даты
    val workoutByDate = remember(allWorkouts) {
        allWorkouts.associate { wws ->
            val date = java.time.Instant.ofEpochMilli(wws.workout.date)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate()
            date to Pair(
                wws.sets.sumOf { it.reps },
                wws.workout.target
            )
        }
    }

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
                // В dayContent передай данные для этой даты
                dayContent = { day ->
                    val (total, target) = workoutByDate[day.date] ?: Pair(0, 0)
                    DayElement(
                        day = day,
                        isSelected = selectedDate == day.date,
                        onClick = { onDateSelected(it.date) },
                        total = total,
                        target = target
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
fun DayElement(
    day: CalendarDay,
    isSelected: Boolean,
    onClick: (CalendarDay) -> Unit,
    total: Int = 0,
    target: Int = 0){

    val dateBrush = when {
        target == 0 -> null         // нет цели — не красим
        total >= target -> Brush.linearGradient(listOf(Color(0xFF00FF1E), Color(0xFF38A342)))
        total > 0 -> Brush.linearGradient(listOf(Color(0xFF00FFFB), Color(0xFF0051FF)))
        else -> Brush.linearGradient(listOf(Color(0xFFFF0000), Color(0xFFD21856)))          // не начато — красный
    }
    val dateBorder = when{
        target == 0 -> Color.Transparent         // нет цели — не красим
        total >= target -> Color(0xFF00FF6A)
        total > 0 -> Color(0xFF00A2FF)
        else -> Color(0xFFB41717)
    }

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
        //Закрашивание цифры в зависимости от даты
        val textColor = when {
            isSelected -> Color.White
            day.position != DayPosition.MonthDate -> Color.LightGray
            else -> Color.Black
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
                    .background(
                        brush = dateBrush ?: Brush.linearGradient(
                            listOf(
                                Color(0xFFA4C3E0).copy(alpha = 0.4f),
                                Color(0xFFA4C3E0).copy(alpha = 0.4f)
                            )
                        ),
                        shape = CircleShape
                    )
                    .border(2.dp, dateBorder , CircleShape)
            )
        }
        //Дата в ячейке календаря
        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            style = AppTheme.fonts.montBold,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
            //fontWeight = FontWeight.Black
        )
    }
}