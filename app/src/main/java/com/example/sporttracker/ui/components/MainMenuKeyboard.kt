package com.example.sporttracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AddSubtractButtons(
    leftText: String,
    rightText: String,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(modifier = modifier
        .width(250.dp)
        .height(48.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val buttons = listOf(leftText to onLeftClick, rightText to onRightClick)

        buttons.forEach { (label, action) ->
            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .border(
                        width = 2.dp,
                        color = Color(0xFFDCB07A),
                        shape = RoundedCornerShape(12.dp)
                    ),
                onClick = action,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFFFBAC5D), Color(0xFFF28B31))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                }
            }
        }
    }
}

@Composable
fun RecordButton(
    mainText: String,
    mainClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .size(width = 250.dp, height = 60.dp)
            .clickable { mainClick() } // Теперь клик работает точно по форме полукруга
            .background(brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFBAC5D), // Чуть светлее сверху
                    Color(0xFFF28B31)  // Чуть темнее снизу
                )
            ))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(mainText, fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ManualInput(){

}