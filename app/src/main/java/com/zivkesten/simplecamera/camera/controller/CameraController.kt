package com.zivkesten.simplecamera.camera.controller

import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.zivkesten.simplecamera.camera.controller.buttons.CenterButton
import com.zivkesten.simplecamera.camera.controller.buttons.EndButton
import com.zivkesten.simplecamera.camera.controller.buttons.StartButton
import com.zivkesten.simplecamera.camera.controller.buttons.ContinueButton
import com.zivkesten.simplecamera.camera.controller.thumbnails.ButtonAnimation
import com.zivkesten.simplecamera.ui.animations.EnterAnimation
import com.zivkesten.simplecamera.ui.RotateToPosition
import com.zivkesten.simplecamera.utils.Rotation
import com.zivkesten.simplecamera.camera.controller.state.CameraControllerUiElementState
import com.zivkesten.simplecamera.presentation.event.CameraUiEvent
import com.zivkesten.simplecamera.utils.initialPosition
import com.zivkesten.simplecamera.utils.isLandscape
import com.zivkesten.simplecamera.utils.isPortrait

@Composable
fun CameraController(
    controllerElementState: CameraControllerUiElementState,
    boxSize: Float,
    onTakePhoto: (() -> Unit)? = null,
    topBarContent: @Composable (() -> Unit)? = null,
    thumbnailsContent: @Composable (() -> Unit)? = null,
) {
    Box(Modifier.fillMaxSize()) {
        topBarContent?.invoke()
        thumbnailsContent?.invoke()

        when (controllerElementState.orientationParams.sensorOrientation) {
            Rotation.ROTATION_0 -> {
                if (isLandscape()) {
                    // If configuration is landscape, compose the photo controller in landscape mode
                    OrientationAwareCameraController(
                        controllerElementState,
                        Modifier
                            .align(CenterEnd)
                            .width(boxSize.dp)
                            .fillMaxHeight(),
                        Modifier
                            .align(BottomCenter)
                            .padding(bottom = 32.dp),
                        Modifier.align(Center),
                        Modifier
                            .align(TopCenter)
                            .padding(top = 32.dp),
                        onTakePhoto
                    ) { controllerElementState.onUiEvent(it) }
                } else {
                    // else we compose the photo controller for ROTATION_0
                    OrientationAwareCameraController(
                        controllerElementState,
                        Modifier
                            .align(BottomCenter)
                            .height(boxSize.dp)
                            .fillMaxWidth(),
                        Modifier
                            .align(CenterStart)
                            .padding(start = 32.dp),
                        Modifier.align(Center),
                        Modifier
                            .align(CenterEnd)
                            .padding(end = 32.dp),
                        onTakePhoto
                    ) { controllerElementState.onUiEvent(it) }
                }
            }

            Rotation.ROTATION_180 -> {
                if (isLandscape()) {
                    // If configuration is landscape, compose the photo controller in landscape mode
                    OrientationAwareCameraController(
                        controllerElementState,
                        Modifier
                            .align(CenterEnd)
                            .width(boxSize.dp)
                            .fillMaxHeight(),
                        Modifier
                            .align(BottomCenter)
                            .padding(bottom = 32.dp),
                        Modifier.align(Center),
                        Modifier
                            .align(TopCenter)
                            .padding(top = 32.dp),
                        onTakePhoto
                    ) { controllerElementState.onUiEvent(it) }
                } else {
                    // else we compose the photo controller for ROTATION_180
                    OrientationAwareCameraController(
                        controllerElementState,
                        Modifier
                            .align(TopCenter)
                            .height(boxSize.dp)
                            .fillMaxWidth(),
                        Modifier
                            .align(CenterEnd)
                            .padding(end = 32.dp),
                        Modifier.align(Center),
                        Modifier
                            .align(CenterStart)
                            .padding(start = 32.dp),
                        onTakePhoto
                    ) { controllerElementState.onUiEvent(it) }
                }
            }

            Rotation.ROTATION_90 -> {
                if (isPortrait()) {
                    // If configuration is portrait, compose the photo controller in portrait mode
                    OrientationAwareCameraController(
                        controllerElementState,
                        Modifier
                            .align(BottomCenter)
                            .height(boxSize.dp)
                            .fillMaxWidth(),
                        Modifier
                            .align(CenterStart)
                            .padding(start = 32.dp),
                        Modifier.align(Center),
                        Modifier
                            .align(CenterEnd)
                            .padding(end = 32.dp),
                        onTakePhoto
                    ) { controllerElementState.onUiEvent(it) }
                } else {
                    // else we compose the photo controller for ROTATION_90
                    OrientationAwareCameraController(
                        controllerElementState,
                        Modifier
                            .align(CenterEnd)
                            .width(boxSize.dp)
                            .fillMaxHeight(),
                        Modifier
                            .align(BottomCenter)
                            .padding(bottom = 32.dp),
                        Modifier.align(Center),
                        Modifier
                            .align(TopCenter)
                            .padding(top = 32.dp),
                        onTakePhoto
                    ) { controllerElementState.onUiEvent(it) }
                }
            }

            Rotation.ROTATION_270 -> {
                if (isPortrait()) {
                    // If configuration is portrait, compose the photo controller in portrait mode
                    OrientationAwareCameraController(
                        controllerElementState,
                        Modifier
                            .align(BottomCenter)
                            .height(boxSize.dp)
                            .fillMaxWidth(),
                        Modifier
                            .align(CenterStart)
                            .padding(start = 32.dp),
                        Modifier.align(Center),
                        Modifier
                            .align(CenterEnd)
                            .padding(end = 32.dp),
                        onTakePhoto
                    ) { controllerElementState.onUiEvent(it) }
                } else {
                    // else we compose the photo controller for ROTATION_270
                    OrientationAwareCameraController(
                        controllerElementState,
                        Modifier
                            .align(CenterStart)
                            .width(boxSize.dp)
                            .fillMaxHeight(),
                        Modifier
                            .align(TopCenter)
                            .padding(top = 32.dp),
                        Modifier.align(Center),
                        Modifier
                            .align(BottomCenter)
                            .padding(bottom = 32.dp),
                        onTakePhoto
                    ) { controllerElementState.onUiEvent(it) }
                }
            }
        }
    }
}

@Composable
fun OrientationAwareCameraController(
    uiElementState: CameraControllerUiElementState,
    modifier: Modifier = Modifier,
    startButtonModifier: Modifier = Modifier,
    shutterModifier: Modifier = Modifier,
    endButtonModifier: Modifier = Modifier,
    onTakePhoto: (() -> Unit)? = null,
    onUiEvent: (CameraUiEvent) -> Unit
) {
    ButtonsContainer(
        modifier, content = {
            StartButton(
                startButtonModifier.testTag(START_BUTTON_TAG),
                step = uiElementState.step,
                model = uiElementState.imagesParams.imagesTaken.lastOrNull(),
                badgeValue = uiElementState.imagesParams.imagesTaken.size.toStringOrNullIfZero(),
                initialPosition = uiElementState.orientationParams.orientationData.initialPosition(),
            ) { onUiEvent.invoke(it) }
            CenterButton(
                shutterModifier,
                uiElementState.showCenterButton,
                shutterButtonState = uiElementState.shutterButtonState,
                onTakePhoto = onTakePhoto
            )
            EndButton(
                endButtonModifier,
                uiElementState.endButtonAnimation,
                uiElementState.showEndButton
            ) {
                ContinueButton {
                    onUiEvent(CameraUiEvent.ContinueBtnPressed)
                }
            }
        }
    )
}

@Composable
private fun ButtonsContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier.then(Modifier.background(Color.Black))) { content() }
}

@Composable
fun ButtonAnimation.AsComposable(
    modifier: Modifier,
    content: @Composable () -> Unit
) = when (this) {
    is ButtonAnimation.EnterAnimation -> EnterAnimation(
        modifier, enter = scaleIn + fadeIn()
    ) { content() }

    is ButtonAnimation.RotateToPosition -> RotateToPosition(
        modifier, initialPosition
    ) { content() }

    is ButtonAnimation.None -> Box(modifier) { content() }
}

const val SHUTTER_BUTTON_LOADING_TAG = "SHUTTER_BUTTON_LOADING_TAG"
const val SHUTTER_BUTTON_OVERLAY_TAG = "SHUTTER_BUTTON_OVERLAY_TAG"
const val SHUTTER_BUTTON_TAG = "SHUTTER_BUTTON"
const val END_BUTTON_TAG = "END_BUTTON"
const val START_BUTTON_TAG = "START_BUTTON"

fun Int.toStringOrNullIfZero(): String? = if (this == 0) null else this.toString()

