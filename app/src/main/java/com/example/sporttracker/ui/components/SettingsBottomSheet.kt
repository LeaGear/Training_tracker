package com.example.sporttracker.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import com.example.sporttracker.ui.theme.AppTheme

/**
 * Shared settings bottom sheet used by both PushUpCounterScreen and HistoryCalendarScreen.
 * "Dumb" component — no ViewModel dependency, only data and callbacks.
 * State is read in the parent screen where ViewModel already exists.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBottomSheet(
    defaultTarget: Int,
    selectedTheme: String,
    selectedLanguage: String,
    onDefaultTargetChanged: (Int) -> Unit,
    onThemeChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = AppTheme.colors.settingsBack.copy(alpha = 0.7f)
    ) {
        SettingsContent(
            defaultTarget = defaultTarget,
            onDefaultTargetChanged = onDefaultTargetChanged,
            selectedTheme = selectedTheme,
            onThemeChange = onThemeChange,
            selectedLanguage = selectedLanguage,
            onLanguageChange = onLanguageChange,
            onDismiss = onDismiss
        )
    }
}