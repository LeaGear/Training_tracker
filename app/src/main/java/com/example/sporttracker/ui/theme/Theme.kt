package com.example.sporttracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


@Composable
fun SportTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkPalette else LightPalette
    val shapes = appShapes
    val fonts = appFonts

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppShapes provides shapes,
        LocalAppFonts provides fonts // Добавляем шрифты
    ){
        MaterialTheme(content = content)
    }
}