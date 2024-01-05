package com.zivkesten.simplecamera.ui.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@ExperimentalAnimationApi
@Composable
fun SlideUpEnterAnimation(
    modifier: Modifier = Modifier,
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
        enter = fadeIn() + slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight }, /* Start from bottom */
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )
    ) { content() }
}
