package com.example.sporttracker.ui.components

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin

val StarShape = GenericShape { size, _ ->
    val center = Offset(size.width / 2f, size.height / 2f)
    val outerRadius = size.width / 2f
    val innerRadius = outerRadius * 0.6f // Adjust for "fatness"
    val numPoints = 5
    val degreesPerStep = Math.toRadians(360.0 / (numPoints * 2)).toFloat()

    // Start at the top (-90 degrees)
    var currentAngle = -Math.PI.toFloat() / 2f

    moveTo(
        center.x + outerRadius * cos(currentAngle),
        center.y + outerRadius * sin(currentAngle)
    )

    for (i in 1 until numPoints * 2) {
        currentAngle += degreesPerStep
        val r = if (i % 2 == 0) outerRadius else innerRadius
        lineTo(
            center.x + r * cos(currentAngle),
            center.y + r * sin(currentAngle)
        )
    }
    close()
}