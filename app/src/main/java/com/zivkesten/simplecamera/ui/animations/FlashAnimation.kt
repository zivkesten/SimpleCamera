package com.zivkesten.simplecamera.ui.animations

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private const val FLASH_ANIMATION_DURATION = 100

@Composable
fun FlashAnimation(
    modifier: Modifier,
    animationDuration: Long? = null
) {
    val alphaValue = remember { mutableStateOf(0.5f) }
    val s = animateFloatAsState(
        targetValue = alphaValue.value,
        animationSpec = tween(
            FLASH_ANIMATION_DURATION,
            easing = FastOutLinearInEasing
        )
    )
    LaunchedEffect(Unit) {
        alphaValue.value = 1f
        delay(animationDuration ?: FLASH_ANIMATION_DURATION.toLong())
        alphaValue.value = 0f
    }

    Box(
        modifier = Modifier
            .alpha(s.value)
            .background(Color.Black)
            .then(modifier)
            .height(200.dp)
    ) {
        LaunchedEffect(Unit) {
            // TODO: Play Sound
//            val soundManager = EntryPointAccessors
//                .fromApplication(
//                    ContextProvider.getApplicationContext(),
//                    ApplicationEntryPoints::class.java
//                ).getSoundManager()
//            soundManager.playSound(SoundManager.CAMERA_SHUTTER)
        }
    }
}
