package com.zivkesten.simplecamera.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    loaderBaseColor: Color = Color.LightGray,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "angle"
    )

    val gradient = Brush.sweepGradient(
        colors = listOf(
            Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            Color.Transparent,
            loaderBaseColor.copy(alpha = 0.5f),
            loaderBaseColor,
            loaderBaseColor,
        )
    )

    Canvas(
        modifier = modifier.then(
            Modifier
                .size(80.dp)
                .padding(16.dp)
                .rotate(angle)
                .pointerInput(Unit) {}
        )
    ) {
        drawArc(
            color = loaderBaseColor,
            startAngle = 0f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = 25f, cap = StrokeCap.Round)
        )
        drawArc(
            brush = gradient,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            style = Stroke(width = 25f, cap = StrokeCap.Round)
        )
    }
}
