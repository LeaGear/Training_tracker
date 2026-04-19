package com.example.sporttracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporttracker.R
import com.example.sporttracker.ui.theme.AppTheme

@Composable
fun DeleteConfirmation(
    onConfirm: () -> Unit, // Callback for "Yes"
    onDismiss: () -> Unit,  // Callback for "No" or "Cancel"
    exerciseName: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppTheme.colors.primaryElementColor.copy(alpha = 0.2f), // Glassy
        modifier = Modifier
            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(28.dp)),
        title = {
            Text(
                text = "${stringResource(R.string.delete_title)} '$exerciseName'",
                textAlign = TextAlign.Center,
                style = AppTheme.fonts.montBold,
                fontSize = 20.sp,
                color = AppTheme.colors.settingsBack
            )
        },
        text = {
            Text(
                text = stringResource(R.string.delete_text),
                style = AppTheme.fonts.montBold,
                fontSize = 20.sp,
                color = AppTheme.colors.settingsBack
            )
        },
        dismissButton = {
            Box(
                modifier = Modifier
                    .border(1.dp, Color.White, RoundedCornerShape(28.dp))
            ) {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = stringResource(R.string.btn_cancel),
                        style = AppTheme.fonts.montBold,
                        fontSize = 16.sp,
                        color = AppTheme.colors.settingsBack
                    )
                }
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White.copy(alpha = 0.7f))
                    .border(2.dp, Color.Black, RoundedCornerShape(28.dp))
            ) {
                TextButton(onClick = onConfirm) {
                    Text(
                        text = stringResource(R.string.delete_confirm),
                        style = AppTheme.fonts.montBold,
                        fontSize = 18.sp,
                        color = Color.Black//AppTheme.colors.settingsBack
                    )
                }
            }
        },
    )
}