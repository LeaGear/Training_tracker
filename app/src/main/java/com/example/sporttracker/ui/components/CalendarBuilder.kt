package com.example.sporttracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporttracker.ui.theme.AppTheme
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
    workoutByDate: Map<LocalDate, Pair<Int, Int>>
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
        .fillMaxWidth(),
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
            .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape)
            .background(AppTheme.colors.primaryButton),
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
                    .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape)
                    .background(AppTheme.colors.calendarInProgressEnd.copy(alpha = 0.2f))
            )

            HorizontalCalendar(modifier = Modifier.padding(6.dp),
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
                    Row(modifier = Modifier.fillMaxWidth()) {
                        daysOfWeek.forEach { dayOfWeek ->
                            Text(
                                modifier = Modifier.weight(1f),
                                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).replaceFirstChar { it.uppercase() },
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                style = AppTheme.fonts.montBlack,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
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

    val colors = AppTheme.colors

    val dateBrush = when {
        target == 0 -> Brush.linearGradient(
            listOf(colors.calendarDefaultDay.copy(alpha = 0.4f),
                colors.calendarDefaultDay.copy(alpha = 0.4f))
        )
        total >= target -> Brush.linearGradient(
            listOf(colors.calendarCompletedStart, colors.calendarCompletedEnd)
        )
        total > 0 -> Brush.linearGradient(
            listOf(colors.calendarInProgressStart, colors.calendarInProgressEnd)
        )
        else -> Brush.linearGradient(
            listOf(colors.calendarNotStartedStart, colors.calendarNotStartedEnd)
        )
    }

    val dateBorder = when {
        target == 0    -> Color.Transparent
        total >= target -> colors.calendarCompletedBorder
        total > 0      -> colors.calendarInProgressBorder
        else           -> colors.calendarNotStartedBorder
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
            day.position != DayPosition.MonthDate -> AppTheme.colors.calendarOtherMonths
            else -> MaterialTheme.colorScheme.onSurface
        }

        //Цвет выделенной даты
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = AppTheme.colors.primaryButton, CircleShape)
                    .border(AppTheme.shapes.primaryBorder.copy(width = 2.dp), CircleShape)// Твой оранжевый акцент
            )
        }
        //Цвет остальных дат
        else{
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = dateBrush,
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
        )
    }
}