package com.zivkesten.simplecamera.camera.controller

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zivkesten.simpleCamera.R
import com.zivkesten.simplecamera.camera.controller.state.CameraControllerUiElementState
import com.zivkesten.simplecamera.event.CameraUiEvent
import com.zivkesten.simplecamera.screens.CONTROLLER_SIZE
import com.zivkesten.simplecamera.SlideDownEnterAnimation

typealias OnFlashClicked = () -> Unit
typealias OnExitClicked = () -> Unit

@Composable
fun AnimatedCameraTopBar(
    uiElementState: CameraControllerUiElementState,
    flashContent: @Composable () -> Unit
) {
    SlideDownEnterAnimation(animationDuration = 600) {
        CameraTopBar(
            Modifier.cameraTopBarPadding(
                uiElementState.orientationParams.sensorOrientation,
                CONTROLLER_SIZE
            ),
            onExitClicked = { uiElementState.onUiEvent(CameraUiEvent.CloseButtonClicked) },
            flashContent = flashContent,
        )
    }
}

@Composable
fun CameraTopBar(
    modifier: Modifier = Modifier,
    onExitClicked: OnExitClicked,
    flashContent: @Composable () -> Unit,
) {
    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
        CameraTopBarRow(
            modifier,
            onExitClicked = onExitClicked,
            flashContent = flashContent,
        )
    }
}

@Composable
fun CameraTopBarRow(
    modifier: Modifier = Modifier,
    onExitClicked: OnExitClicked,
    flashContent: @Composable () -> Unit,
) {
    Row(
        Modifier
            .then(modifier)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        ExitButton { onExitClicked() }
        flashContent()
    }
}

@Composable
fun FlashButton(
    isFlashOn: Boolean,
    onFlashClicked: OnFlashClicked
) {
    AnimatedContent(
        targetState = isFlashOn,
        transitionSpec = { scaleIn togetherWith scaleOut() }, label = ""
    ) { flashOn ->
        Image(
            painterResource(
                id = if (flashOn) {
                    R.drawable.flash_on
                } else {
                    R.drawable.flash_off
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() } ,
                    indication = null
                ) {
                    onFlashClicked()
                }
        )
    }
}

@Composable
private fun ExitButton(onClick: () -> Unit) {
    Image(
        painterResource(R.drawable.close_button),
        contentDescription = null,
        modifier = Modifier
            .size(30.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
                onClick = onClick
            )
    )
}
