package com.example.sporttracker.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporttracker.ui.theme.AppTheme
import kotlinx.datetime.Month
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SimpleBarChart(
    data: List<Pair<Int, Int>>,
    defaultTarget: Int,
    selectedDate: Pair<Int,Int>,
    language: String
) {
    // Если данных нет, график не рисуем или рисуем заглушку
// 1. Достаем плотность экрана в начале Composable функции
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    val barWidthDp = 30.dp
    val totalWidthDp = barWidthDp * data.size
    val selectedDay = selectedDate.first
    val selectedMonth = selectedDate.second
    val monthForChart = Month.of(selectedMonth)
        .getDisplayName(TextStyle.FULL_STANDALONE, Locale(language))
        .replaceFirstChar { it.uppercase() }
    val todayIndex = selectedDay - 1
    val completedDayStart = AppTheme.colors.calendarCompletedEnd // Dark Green
    val completedDayEnd = AppTheme.colors.calendarCompletedStart // Light Green
    val progressDayStart = AppTheme.colors.calendarInProgressStart
    val progressDayEnd = AppTheme.colors.calendarInProgressEnd

    // Используем LaunchedEffect, чтобы скролл сработал при запуске экрана
    LaunchedEffect(key1 = todayIndex) {
        if (data.isNotEmpty()) {
            // Вычисляем ширину одного столбика в пикселях
            val barWidthPx = with (density){barWidthDp.toPx()}

            // Вычисляем целевую позицию:
            // (Центр нужного столбика) - (Середина экрана / 2)
            val targetScroll = todayIndex * barWidthPx

            val centerOffset = (scrollState.viewportSize / 2)
            // Плавно скроллим
            scrollState.animateScrollTo((targetScroll - centerOffset + barWidthPx / 2).toInt())
        }
    }

    // Обертка для фона, которая НЕ скроллится
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(AppTheme.shapes.mainShape)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(AppTheme.colors.primaryElementColor)
                .border(AppTheme.shapes.primaryBorder, AppTheme.shapes.mainShape)
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ){
            Box(
                modifier = Modifier
                    .width(totalWidthDp)
                    .weight(0.1f)
                ,contentAlignment = Alignment.Center
            ){
                Text(
                    text = monthForChart,
                    style = AppTheme.fonts.montBold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
            // Контейнер, который СКРОЛЛИТСЯ
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.9f)
                    .horizontalScroll(scrollState)
                    .padding(4.dp)
            ) {


                Canvas(
                    modifier = Modifier
                        .width(totalWidthDp) // Устанавливаем ширину Canvas
                        .weight(0.85f) // Устанавливаем высоту Canvas
                        .padding(vertical = 4.dp)
                ) {

                    val barWidthPx = barWidthDp.toPx()
                    val logicalCeiling = if (defaultTarget == 0) {
                        data.maxOfOrNull { it.second.toFloat() } ?: 1f
                    } else {
                        defaultTarget.toFloat()
                    }
                    val targetLineY = size.height - (defaultTarget.toFloat()/ (logicalCeiling * 1.5f)) * size.height

                    data.forEachIndexed { index, point ->
                        // Вычисляем высоту (минимум 2 пикселя, чтобы было видно даже 0)
                        val calculatedHeight =
                            (point.second.toFloat() / (logicalCeiling * 1.5f)) * size.height
                        val barHeight = calculatedHeight.coerceAtMost(size.height)

                        val barBrush = if (point.second >= defaultTarget && defaultTarget > 0) {
                            Brush.linearGradient(
                                colors = listOf(completedDayStart, completedDayEnd),
                                start = Offset(0f, barHeight),
                                end = Offset(0f, size.height - barHeight)
                            ) } else {
                            Brush.linearGradient(
                                colors = listOf(progressDayStart, progressDayEnd),
                                start = Offset(0f, barHeight),
                                end = Offset(0f, size.height)
                            )
                            }
                        drawRoundRect(
                            brush = barBrush,
                            topLeft = Offset(
                                x = index * barWidthPx + 4f,
                                y = size.height - barHeight
                            ),
                            size = Size(barWidthPx - 8f, barHeight),
                            cornerRadius = CornerRadius(8.dp.toPx())
                        )
                    }
                    drawLine(
                        color = Color.White.copy(alpha = 0.6f),
                        start = Offset(x = 0f, y = targetLineY),
                        end = Offset(x = size.width, y = targetLineY),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(10f, 10f),
                            phase = 0f
                        )
                    )
                    drawContext.canvas.nativeCanvas.drawText(
                        "Цель: $defaultTarget",
                        10f,
                        targetLineY - 10f, // Чуть выше линии
                        Paint().apply {
                            color = android.graphics.Color.WHITE
                            textSize = 12.sp.toPx()
                            isAntiAlias = true
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .width(barWidthDp * data.size)
                        .weight(0.15f)
                ) {
                    data.forEachIndexed { index, _ ->
                        Box(
                            modifier = Modifier
                                .size(barWidthDp)
                                .clip(CircleShape)
                                .background(
                                    if (index == todayIndex) {
                                        Brush.linearGradient(listOf(progressDayStart, progressDayEnd))
                                    } else {
                                        SolidColor(Color.Transparent)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                style = AppTheme.fonts.montBold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}