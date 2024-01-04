package com.zivkesten.simplecamera

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

private const val DEFAULT_ANIMATION_DURATION = 300

@Composable
fun SlideDownEnterAnimation(
    modifier: Modifier = Modifier,
    animationDuration: Int? = null,
    animationDelay: Int? = null,
    content: @Composable () -> Unit
) {
    val state = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }
    AnimatedVisibility(
        modifier = modifier,
        visibleState = state,
        enter = fadeIn() + slideInVertically(
            initialOffsetY = /* Start from bottom */ { -100 },
            animationSpec = tween(
                durationMillis = animationDuration ?: DEFAULT_ANIMATION_DURATION,
                delayMillis = animationDelay ?: 0,
                easing = FastOutSlowInEasing
            )
        )
    ) { content() }
}
