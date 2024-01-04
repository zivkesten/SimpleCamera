package com.zivkesten.simplecamera

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun EnterAnimation(
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    fadeIn: Boolean = false,
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
        modifier = modifier,
        visibleState = state,
        enter = if (fadeIn) fadeIn() + enter else enter,
        exit = if (fadeIn) fadeOut() + exit else exit
    ) { content() }
}
