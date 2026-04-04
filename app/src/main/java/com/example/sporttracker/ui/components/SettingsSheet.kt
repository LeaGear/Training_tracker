package com.example.sporttracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sporttracker.R
import com.example.sporttracker.ui.components.WorkoutConstants.MAX_REPS_INPUT_TARGET_LENGTH
import com.example.sporttracker.ui.theme.AppTheme


@Composable
fun SettingsContent(
    defaultTarget: Int,
    language: String,
    onDefaultTargetChanged: (Int) -> Unit,
    onLanguageChanged: (String) -> Unit,
    onDismiss: () -> Unit
){
    val focusManager = LocalFocusManager.current

    var count by remember { mutableStateOf(0)}

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Setting for set default target for training
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(AppTheme.colors.settingsElement)
                .border(3.dp, AppTheme.colors.settingsBorder, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Box(modifier = Modifier
                    .size(width = 200.dp, height = 48.dp)
                    //.background(Color.Red),
                    ,contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.settings_set_default_target),
                        textAlign = TextAlign.Center,
                        style = AppTheme.fonts.montBold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
                Box(modifier = Modifier
                    .size(width = 88.dp, height = 48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.6f))
                    .border(4.dp, Color.LightGray.copy(alpha = 0.6f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        value = defaultTarget.toString(),
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() } && newValue.length <= MAX_REPS_INPUT_TARGET_LENGTH) {
                                onDefaultTargetChanged(newValue.toIntOrNull() ?: 0)
                            }
                        },
                        textStyle = MaterialTheme.typography.displaySmall.copy(
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp
                        ),
                        cursorBrush = SolidColor(Color.White),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )
                }
            }
        }

        // Language choosing
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(AppTheme.colors.settingsElement)
                .border(3.dp, AppTheme.colors.settingsBorder, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ){
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Box(
                    modifier = Modifier
                        .size(width = 200.dp, height = 48.dp)
                    //.background(Color.Red),
                    , contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.settings_language),
                        textAlign = TextAlign.Center,
                        style = AppTheme.fonts.montBold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .size(width = 88.dp, height = 48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.6f))
                        .border(4.dp, Color.LightGray.copy(alpha = 0.6f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        value = count.toString(),
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() } && newValue.length <= MAX_REPS_INPUT_TARGET_LENGTH) {
                                count = newValue.toIntOrNull() ?: 0
                            }
                        },
                        textStyle = MaterialTheme.typography.displaySmall.copy(
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp
                        ),
                        cursorBrush = SolidColor(Color.White),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}