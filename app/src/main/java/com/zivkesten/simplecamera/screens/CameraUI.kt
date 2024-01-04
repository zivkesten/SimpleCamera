package com.zivkesten.simplecamera.screens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.lemonadeinc.lemonade.ui.composable.camera.controller.flashAnimationPadding
import com.lemonadeinc.lemonade.ui.feature.photocollection.presentation.component.information.FlashAnimation
import com.lemonadeinc.lemonade.ui.feature.photocollection.presentation.screens.PhotoCollectionGalleryScreen
import com.zivkesten.simplecamera.CameraViewModel.Companion.CAMERA
import com.zivkesten.simplecamera.CameraViewModel.Companion.GALLERY
import com.zivkesten.simplecamera.CameraViewModel.Companion.PREVIEW
import com.zivkesten.simplecamera.camera.controller.AnimatedCameraTopBar
import com.zivkesten.simplecamera.camera.controller.CameraController
import com.zivkesten.simplecamera.camera.controller.FlashButton
import com.zivkesten.simplecamera.camera.controller.state.CameraControllerUiElementState
import com.zivkesten.simplecamera.event.CameraUiEvent
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CameraUI(
    uiElementState: CameraControllerUiElementState,
) {
    AnimatedContent(
        targetState = uiElementState.step,
        transitionSpec = { fadeIn() with fadeOut(animationSpec = tween(delayMillis = 200)) },
        label = CONTROLLER_ANIMATED_CONTENT
    ) { targetScreen ->
        when (targetScreen) {
            PREVIEW -> Preview(uiElementState)
            GALLERY -> Gallery(uiElementState)
            CAMERA -> Camera(
                uiElementState,
                topBarContent = {
                    AnimatedCameraTopBar(
                        uiElementState,
                        flashContent = {
                            FlashButton(uiElementState.imagesParams.isFlashOn) {
                                uiElementState.onUiEvent(CameraUiEvent.FlashButtonClicked)
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
fun Preview(uiElementState: CameraControllerUiElementState) {
    PhotoCollectionPreviewScreen(uiElementState, boxSize = CONTROLLER_SIZE)
}

@Composable
fun Gallery(uiElementState: CameraControllerUiElementState) {
    PhotoCollectionGalleryScreen(uiElementState, boxSize = CONTROLLER_SIZE)
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
const val PHOTO_LIMIT_WARNING_TAG = "PHOTO_LIMIT_WARNING_TAG"
