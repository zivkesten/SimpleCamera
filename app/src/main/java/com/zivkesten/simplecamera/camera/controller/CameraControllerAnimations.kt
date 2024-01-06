package com.zivkesten.simplecamera.camera.controller

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.ui.Alignment

private const val SCALE_IN_ANIMATION_DURATION = 400
private const val FADE_OUT_ANIMATION_DURATION = 300

private const val SCALE_IN_DEFAULT_ANIMATION_DURATION = 300
private const val FADE_OUT_DEFAULT_ANIMATION_DURATION = 900

const val THUMBNAIL_FADE_ANIMATION_DURATION = 200
const val THUMBNAIL_SHRINK_ANIMATION_DELAY = 300

val scaleIn = scaleIn(
    initialScale = 0.0f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow,
    )
)

val thumbnailExitAnimation = fadeOut(tween(THUMBNAIL_FADE_ANIMATION_DURATION)) +
    shrinkOut(
        animationSpec = tween(delayMillis = THUMBNAIL_SHRINK_ANIMATION_DELAY),
        shrinkTowards = Alignment.CenterEnd
    )

val defaultTransition = scaleIn(
    animationSpec = tween(SCALE_IN_DEFAULT_ANIMATION_DURATION)
) togetherWith fadeOut(animationSpec = tween(FADE_OUT_DEFAULT_ANIMATION_DURATION))

val badgeTransition = scaleIn(
    animationSpec = tween(SCALE_IN_ANIMATION_DURATION)
) togetherWith fadeOut(animationSpec = tween(FADE_OUT_ANIMATION_DURATION))
