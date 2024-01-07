package com.zivkesten.simplecamera.ui.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun SlideInEnterAnimation(
    modifier: Modifier = Modifier,
    animateTo: AnimateTo = AnimateTo.Left,
    content: @Composable () -> Unit
) {
    // For Compose screens we will define entrance animation here since the legacy
    // Methods don't work with ComponentActivity
    val state = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = state,
        modifier,
        enter = fadeIn() + slideInHorizontally(
            initialOffsetX = { fullWidth ->
                when (animateTo) {
                    AnimateTo.Left -> fullWidth
                    AnimateTo.Right -> -fullWidth
                }
            },
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )
    ) { content() }
}

enum class AnimateTo {
    Left,
    Right
}
