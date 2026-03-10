package com.example.sporttracker.ui.components

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporttracker.R
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
    onDateSelected: (LocalDate) -> Unit
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(50) }
    val endMonth = remember { currentMonth.plusMonths(50) }
    val daysOfWeek = remember { daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY) }
    val myCustomFontFamily = FontFamily(
        Font(R.font.montserrat_regular, FontWeight.Normal),
        Font(R.font.montserrat_bold, FontWeight.Bold),
        Font(R.font.montserrat_black, FontWeight.Black)
    )
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
        val text = "${monthName.replaceFirstChar { it.uppercase() }} ${visibleMonth.year}"
        Box(modifier = Modifier
            .size(height = 40.dp, width = 180.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = AppTheme.colors.primaryButton,
                shape = RoundedCornerShape(20.dp)
            )
            .border(border = AppTheme.colors.primaryBorder, shape = RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = text,
                fontFamily = myCustomFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }


        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                DayElement(
                    day = day,
                    isSelected = selectedDate == day.date,
                    onClick = { onDateSelected(it.date) }
                )
            },
            monthHeader = {
                Row(modifier = Modifier.fillMaxWidth()) {
                    daysOfWeek.forEach { dayOfWeek ->
                        Text(
                            modifier = Modifier.weight(1f),
                            text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun DayElement(day: CalendarDay, isSelected: Boolean, onClick: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // Квадратные ячейки
            .padding(4.dp)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        val textColor = when {
            isSelected -> Color.White
            day.position != DayPosition.MonthDate -> Color.LightGray
            else -> Color.Black
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFF9800), CircleShape) // Твой оранжевый акцент
            )
        }

        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}