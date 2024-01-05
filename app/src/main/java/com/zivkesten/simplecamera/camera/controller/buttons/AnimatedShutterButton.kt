package com.lemonadeinc.lemonade.ui.composable.camera.controller.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zivkesten.simplecamera.camera.controller.SHUTTER_BUTTON_OVERLAY_TAG
import com.zivkesten.simplecamera.camera.controller.SHUTTER_BUTTON_TAG
import com.zivkesten.simplecamera.camera.controller.scaleIn

@Preview
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedShutterButton(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    shutterButtonState: ShutterButtonState = ShutterButtonState.ENABLED,
    onClick: (() -> Unit)? = {}
) {
    var scale by remember { mutableStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(100),
        label = "AnimatedShutterButton animatedScale"
    )

    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = scaleIn,
        exit = scaleOut()
    ) {
        Box(
            modifier = Modifier
                .testTag(SHUTTER_BUTTON_TAG)
                .clip(CircleShape)
                .background(Color.Transparent)
                .size(80.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            scale = 0.85f
                            tryAwaitRelease()
                            scale = 1f
                            onClick?.invoke()
                        }
                    )
                }
                .then(modifier),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .border(
                        BorderStroke(3.dp, Color.White),
                        shape = CircleShape
                    )
            ) {
                Box(
                    Modifier
                        .scale(animatedScale)
                        .fillMaxSize()
                        .padding(7.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }

        AnimatedContent(
            targetState = shutterButtonState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "AnimatedShutterButton shutterButtonState"
        ) {shutterButtonState ->
            when (shutterButtonState) {
                ShutterButtonState.DISABLED -> ShutterDisableOverlay()
                ShutterButtonState.LOADING -> {
                    Unit
//                    LoadingIndicator(
//                        Modifier.testTag(SHUTTER_BUTTON_LOADING_TAG)
//                    )
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun ShutterDisableOverlay() {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.Transparent)
            .size(80.dp)
            .background(Color.Black.copy(alpha = 0.5f))
            .pointerInput(Unit) {}
            .testTag(SHUTTER_BUTTON_OVERLAY_TAG)
    )
}

enum class ShutterButtonState {
    ENABLED, DISABLED, LOADING
}

@Preview
@Composable
fun ShutterButtonLoadingPreview() {
    AnimatedShutterButton(shutterButtonState = ShutterButtonState.LOADING)
}

@Preview
@Composable
fun ShutterButtonDisabledPreview() {
    AnimatedShutterButton(shutterButtonState = ShutterButtonState.DISABLED)
}
