package com.example.sporttracker.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sporttracker.R


val myCustomFontFamily = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_black, FontWeight.Black)
)
// Описываем структуру цветов
data class AppColors(
    //val primaryElementColor: Brush,
    val primaryElementColor: Color,
    val calendarHighlight: Color,
    val primaryAccent: Color,
    val repsBorder: Color,
    val testBackColor: Color,

    // Календарь — статус дня
    val calendarCompletedStart: Color,    // выполнено — градиент начало
    val calendarCompletedEnd: Color,      // выполнено — градиент конец
    val calendarCompletedBorder: Color,

    val calendarInProgressStart: Color,   // в процессе — градиент начало
    val calendarInProgressEnd: Color,     // в процессе — градиент конец
    val calendarInProgressBorder: Color,

    val calendarNotStartedStart: Color,   // не начато — градиент начало
    val calendarNotStartedEnd: Color,     // не начато — градиент конец
    val calendarNotStartedBorder: Color,

    val calendarDefaultDay: Color,        // обычный день без цели
    val calendarSelectedBorder: Color,    // рамка выбранного дня
    val calendarOtherMonths: Color,

    val settingsBack: Color,
    val settingsElement: Brush,
    val settingsBorder: Color
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
//    primaryElementColor = Brush.verticalGradient(
//        colors = listOf(Color(0xFFFBAC5D).copy(alpha = 0.5f), Color(0xFFF28B31).copy(alpha = 0.5f))
//    ),
    primaryElementColor = Color(0xFFFF5E00).copy(alpha = 0.6f),
    calendarHighlight = Color(0xFFFF4081),
    primaryAccent = Color(0xFFFF840B),
    repsBorder = Color(0xFFB1CBE5),
    testBackColor = Color(0xFFF27735).copy(alpha = 0.4f),

    calendarCompletedStart = Color(0xFF00FF1E),
    calendarCompletedEnd = Color(0xFF38A342),
    calendarCompletedBorder = Color(0xFF00FF6A),

    calendarInProgressStart = Color(0xFF00FFFB),
    calendarInProgressEnd = Color(0xFF0051FF),
    calendarInProgressBorder = Color(0xFF00A2FF),

    calendarNotStartedStart = Color(0xFFFF0000),
    calendarNotStartedEnd = Color(0xFFD21856),
    calendarNotStartedBorder = Color(0xFFB41717),

    calendarDefaultDay = Color(0xFFA4C3E0),
    calendarSelectedBorder = Color(0xFFFF840B),
    calendarOtherMonths = Color(0xFF8D8D8D),

    settingsBack = Color(0xFFE0F4F6),
    settingsElement = Brush.verticalGradient(
        colors = listOf(Color(0xFF00A8B5), Color(0xFF007A85))
    ),
    settingsBorder = Color(0xFF007A85)
)
val DarkPalette = AppColors(
    //primaryElementColor = Brush.verticalGradient(colors = listOf(Color(0xFFFBAC5D), Color(0xFFF28B31))),
    primaryElementColor = Color(0xFFFF5E00),
    calendarHighlight = Color(0xFFFF79B0),
    primaryAccent = Color(0xFFFF840B),
    repsBorder = Color(0xFFB1CBE5),
    testBackColor = Color(0xFFF27735).copy(alpha = 0.4f),

    calendarCompletedStart = Color(0xFF00CC18),
    calendarCompletedEnd = Color(0xFF2D8A36),
    calendarCompletedBorder = Color(0xFF00CC55),

    calendarInProgressStart = Color(0xFF00E5E0),
    calendarInProgressEnd = Color(0xFF0044CC),
    calendarInProgressBorder = Color(0xFF0088CC),

    calendarNotStartedStart = Color(0xFFCC0000),
    calendarNotStartedEnd = Color(0xFFAA1445),
    calendarNotStartedBorder = Color(0xFF991212),

    calendarDefaultDay = Color(0xFF5C7A96),
    calendarSelectedBorder = Color(0xFFFF840B),
    calendarOtherMonths = Color(0xFF8D8D8D),

    settingsBack = Color(0xFF0D3B40),
    settingsElement = Brush.verticalGradient(
        colors = listOf(Color(0xFF00C2D1), Color(0xFF009BAA))
    ),
    settingsBorder = Color(0xFF009BAA)
)
 val appShapes = AppShapes(
     primaryBorder = BorderStroke(width = 4.dp, color = Color(0xFFFF840B).copy(alpha = 0.3f)),//DCB07A
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