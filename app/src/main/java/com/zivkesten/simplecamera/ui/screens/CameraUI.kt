package com.zivkesten.simplecamera.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.zivkesten.simplecamera.presentation.viewmodel.CameraViewModel.Companion.CAMERA
import com.zivkesten.simplecamera.presentation.viewmodel.CameraViewModel.Companion.GALLERY
import com.zivkesten.simplecamera.presentation.viewmodel.CameraViewModel.Companion.PREVIEW
import com.zivkesten.simplecamera.ui.animations.FlashAnimation
import com.zivkesten.simplecamera.camera.controller.AnimatedCameraTopBar
import com.zivkesten.simplecamera.camera.controller.CameraController
import com.zivkesten.simplecamera.camera.controller.FlashButton
import com.zivkesten.simplecamera.camera.controller.flashAnimationPadding
import com.zivkesten.simplecamera.camera.controller.state.CameraControllerUiElementState
import com.zivkesten.simplecamera.presentation.event.CameraUiEvent
import kotlinx.coroutines.delay

@Composable
fun CameraUI(
    cameraUiState: CameraControllerUiElementState,
) {
    AnimatedContent(
        targetState = cameraUiState.screen,
        transitionSpec = { fadeIn() togetherWith fadeOut(animationSpec = tween(delayMillis = 200)) },
        label = CONTROLLER_ANIMATED_CONTENT
    ) { targetScreen ->
        when (targetScreen) {
            PREVIEW -> Preview(cameraUiState)
            GALLERY -> Gallery(cameraUiState)
            CAMERA -> Camera(
                cameraUiState,
                topBarContent = {
                    AnimatedCameraTopBar(
                        cameraUiState,
                        flashContent = {
                            FlashButton(cameraUiState.imagesParams.isFlashOn) {
                                cameraUiState.onUiEvent(CameraUiEvent.FlashButtonClicked)
                            }
                        }
                    )
                },
            )

            else -> Log.e(TAG, "UnsupportedState $targetScreen")
        }
    }
}

@Composable
fun Preview(cameraUiState: CameraControllerUiElementState) {
    PreviewScreen(cameraUiState, boxSize = CONTROLLER_SIZE)
}

@Composable
fun Gallery(uiElementState: CameraControllerUiElementState) {
    GalleryScreen(uiElementState, boxSize = CONTROLLER_SIZE)
}

@Composable
fun Camera(
    uiElementState: CameraControllerUiElementState,
    topBarContent: @Composable () -> Unit
) {
    val showFlashAnimation = remember { mutableStateOf(false) }

    if (showFlashAnimation.value) {
        FlashAnimation(
            Modifier.flashAnimationPadding(
                uiElementState.orientationParams.sensorOrientation, CONTROLLER_SIZE
            ),
            FLASH_ANIMATION_DURATION
        )
        LaunchedEffect(Unit) {
            delay(FLASH_ANIMATION_DELAY)
            showFlashAnimation.value = false
        }
    }

    CameraController(
        uiElementState,
        boxSize = CONTROLLER_SIZE,
        topBarContent = topBarContent,
        onTakePhoto = {
            showFlashAnimation.value = true
            uiElementState.onUiEvent(CameraUiEvent.ImageCaptured)
        }
    )
}

private const val TAG: String = "CameraUI"
private const val FLASH_ANIMATION_DURATION = 100L
private const val FLASH_ANIMATION_DELAY = 150L
private const val CONTROLLER_ANIMATED_CONTENT = "CONTROLLER_ANIMATED_CONTENT"
