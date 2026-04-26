package com.example.sporttracker.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun SportTrackerTheme(
    themeMode: String = "SYSTEM",
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode){
        "LIGHT"-> false
        "DARK"-> true
        else -> isSystemInDarkTheme()
    }
    val colors = if (isDark) DarkPalette else LightPalette
    val shapes = appShapes
    val fonts = appFonts

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppShapes provides shapes,
        LocalAppFonts provides fonts
    ) {
        // Ми використовуємо MaterialTheme як обгортку для базових системних компонентів,
        // але основний контент з фоном розміщуємо всередині
        MaterialTheme {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = colors.backgroundDrawable),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                content()
            }
        }
    }
}