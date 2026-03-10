package com.example.sporttracker.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Описываем структуру цветов
data class AppColors(
    val primaryButton: Brush,
    val testColor: Brush,
    val calendarHighlight: Color,
    val primaryBorder: BorderStroke
    // добавь сюда любые свои переменные
)

// Создаем "хранилище", которое будет прокидывать цвета сквозь код
val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No colors provided")
}

val LightPalette = AppColors(
    primaryButton = Brush.verticalGradient(colors = listOf(Color(0xFFFBAC5D), Color(0xFFF28B31))),
    testColor = Brush.verticalGradient(colors = listOf(Color.Red, Color.Green)),
    calendarHighlight = Color(0xFFFF4081),
    primaryBorder = BorderStroke(width = 4.dp, color = Color(0xFFDCB07A))
)

val DarkPalette = AppColors(
    primaryButton = Brush.verticalGradient(colors = listOf(Color(0xFFFBAC5D), Color(0xFFF28B31))),
    testColor = Brush.verticalGradient(colors = listOf(Color.Red, Color.Green)),
    calendarHighlight = Color(0xFFFF79B0),
    primaryBorder = BorderStroke(width = 4.dp, color = Color(0xFFDCB07A))

)

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}