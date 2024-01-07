package com.zivkesten.simplecamera.ui

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun RotateToPosition(
    modifier: Modifier = Modifier,
    initialPosition: Float = 0f,
    animationSpec: AnimationSpec<Float>? = null,
    content: @Composable () -> Unit,
) {
    var position by remember { mutableFloatStateOf(initialPosition) }
    val animation = animateFloatAsState(
        animationSpec = animationSpec ?: spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        targetValue = position,
        label = "Rotate animation",
    )
    Box(
        modifier = Modifier
            .wrapContentSize()
            .then(modifier)
            .rotate(position)
            .rotate(animation.value)
    ) {
        content()
    }

    LaunchedEffect(LocalConfiguration.current.orientation) {
        position = 0f
    }
}
