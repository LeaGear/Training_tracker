package com.example.sporttracker.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import com.example.sporttracker.R


val myCustomFontFamily = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_black, FontWeight.Black)
)
// Описываем структуру цветов
data class AppColors(
    val primaryButton: Brush,
    val testColor: Brush,
    val calendarHighlight: Color
// добавь сюда любые свои переменные
)
data class AppShapes(
    val primaryBorder: BorderStroke,
    val mainShape: RoundedCornerShape
)
data class AppFonts(
    val montRegular: TextStyle,
    val montBold: TextStyle,
    val montBlack: TextStyle
)


// Создаем "хранилище", которое будет прокидывать цвета сквозь код
val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No colors provided")
}
val LocalAppShapes = staticCompositionLocalOf<AppShapes> {
    error("No shapes provided")
}
val LocalAppFonts = staticCompositionLocalOf<AppFonts> {
    error("No typography provided")
}
val LightPalette = AppColors(
    primaryButton = Brush.verticalGradient(colors = listOf(Color(0xFFFBAC5D), Color(0xFFF28B31))),
    testColor = Brush.verticalGradient(colors = listOf(Color.Red, Color.Green)),
    calendarHighlight = Color(0xFFFF4081)
)
val DarkPalette = AppColors(
    primaryButton = Brush.verticalGradient(colors = listOf(Color(0xFFFBAC5D), Color(0xFFF28B31))),
    testColor = Brush.verticalGradient(colors = listOf(Color.Red, Color.Green)),
    calendarHighlight = Color(0xFFFF79B0)
)
 val appShapes = AppShapes(
     primaryBorder = BorderStroke(width = 4.dp, color = Color(0xFFFF840B)),//DCB07A
     mainShape = RoundedCornerShape(12.dp) //Shape for most buttons and boxes
 )


val appFonts = AppFonts(
    montRegular = TextStyle(
        fontFamily = myCustomFontFamily,
        fontWeight = FontWeight.Normal
    ),
    montBold = TextStyle(
        fontFamily = myCustomFontFamily,
        fontWeight = FontWeight.Bold
    ),
    montBlack = TextStyle(
        fontFamily = myCustomFontFamily,
        fontWeight = FontWeight.Black
    )
)
object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
    val shapes: AppShapes
        @Composable
        get() = LocalAppShapes.current

    val fonts: AppFonts
        @Composable
        get() = LocalAppFonts.current
}