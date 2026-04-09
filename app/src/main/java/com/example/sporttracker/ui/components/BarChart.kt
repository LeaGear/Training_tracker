package com.example.sporttracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporttracker.ui.theme.AppTheme

@Composable
fun SimpleBarChart(
    data: List<Pair<Int, Int>>,
    defaultTarget: Int
) {
    // Если данных нет, график не рисуем или рисуем заглушку

    val maxReps = data.maxOfOrNull { it.second }?.coerceAtLeast(1) ?: 1
    val scrollState = rememberScrollState()
    val barWidthDp = 40.dp
    val totalWidthDp = barWidthDp * data.size
    // Обертка для фона, которая НЕ скроллится
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(AppTheme.shapes.mainShape)
            .background(Color.DarkGray) // Статичный фон
    ) {
        // Контейнер, который СКРОЛЛИТСЯ
        Column(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(scrollState)
                .padding(horizontal = 4.dp)
        ) {

            Canvas(
                modifier = Modifier
                    .width(totalWidthDp) // Устанавливаем ширину Canvas
                    .height(180.dp) // Устанавливаем высоту Canvas
                    .padding(vertical = 16.dp)
            ) {
                val barWidthPx = barWidthDp.toPx()
                //val maxLogicalValue = maxOf(defaultTarget.toFloat(), data.maxOfOrNull { it.second.toFloat() } ?: 1f)
                val logicalCeiling = if (defaultTarget == 0) {
                    data.maxOfOrNull { it.second.toFloat() } ?: 1f
                } else {
                    defaultTarget.toFloat()
                }

                data.forEachIndexed { index, point ->
                    // Вычисляем высоту (минимум 2 пикселя, чтобы было видно даже 0)
                    val calculatedHeight = (point.second.toFloat() / (logicalCeiling * 1.5f)) * size.height
                    val barHeight = calculatedHeight.coerceAtMost(size.height)
                    //val barHeight = maxLogicalValue * 1.5f
                    drawRoundRect(
                        color = if (point.second >= defaultTarget && defaultTarget > 0)
                            Color(0xFF4CAF50) else Color(0xFFFFA000),
                        topLeft = Offset(
                            x = index * barWidthPx + 4f,
                            y = size.height - barHeight
                        ),
                        size = Size(barWidthPx - 8f, barHeight),
                        cornerRadius = CornerRadius(8.dp.toPx())
                    )
                }
            }
            Row(
                modifier = Modifier.width(barWidthDp * data.size)
            ) {
                data.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .width(barWidthDp)
                            .clip(CircleShape)
                            .background(Color.Red), //С бекграундом должен быть только текущий день
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            style = AppTheme.fonts.montBold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }

        }
    }
}