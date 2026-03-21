package com.example.sporttracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.sporttracker.R
import com.example.sporttracker.ui.components.WorkoutConstants.MAX_EXERCISE_NAME_LENGTH
import com.example.sporttracker.ui.components.WorkoutConstants.MAX_REPS_INPUT_TARGET_LENGTH
import com.example.sporttracker.ui.theme.AppTheme

@Composable
fun CreateWorkoutDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_new_workout_title), style = AppTheme.fonts.montBold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {  if (it.length <= MAX_EXERCISE_NAME_LENGTH) name = it },
                    label = { Text(stringResource(R.string.dialog_new_workout_label)) }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primaryAccent)
            ) {
                Text(stringResource(R.string.btn_create))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.btn_cancel)) }
        }
    )
}


@Composable
fun SetTargetWindow(
    onDismiss: () -> Unit,
    onConfirm: (count: Int) -> Unit
){
    var count by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_set_target_title), style = AppTheme.fonts.montBold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = count,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= MAX_REPS_INPUT_TARGET_LENGTH) {
                            count = newValue
                        } },
                    label = { Text(stringResource(R.string.dialog_set_target_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (count.isNotBlank()) {
                        val number = count.toIntOrNull()
                        if (number != null) {  // проверяем что введено корректное число
                            onConfirm(number)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primaryAccent)
            ) {
                Text(stringResource(R.string.btn_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.btn_cancel)) }
        }
    )
}