package com.zivkesten.simplecamera.camera.controller.buttons

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zivkesten.simpleCamera.R
import com.zivkesten.simplecamera.camera.controller.AsComposable
import com.zivkesten.simplecamera.camera.controller.model.ImageData
import com.zivkesten.simplecamera.camera.controller.scaleIn
import com.zivkesten.simplecamera.camera.controller.thumbnails.ButtonAnimation
import com.zivkesten.simplecamera.presentation.event.CameraUiEvent
import com.zivkesten.simplecamera.presentation.viewmodel.CameraViewModel
import com.zivkesten.simplecamera.ui.RotateToPosition
import com.zivkesten.simplecamera.ui.animations.EnterAnimation

@Composable
fun EndButton(
    modifier: Modifier = Modifier,
    animation: ButtonAnimation? = null,
    visible: Boolean,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = scaleIn,
        exit = scaleOut()
    ) {
        animation?.AsComposable(Modifier) { content() }
    }
}

@Composable
fun CenterButton(
    modifier: Modifier,
    visible: Boolean,
    shutterButtonState: ShutterButtonState,
    onTakePhoto: (() -> Unit)? = null
) {
    AnimatedShutterButton(
        modifier,
        visible,
        shutterButtonState
    ) {
        onTakePhoto?.invoke()
    }
}

@Composable
fun StartButton(
    modifier: Modifier = Modifier,
    model: ImageData? = null,
    badgeValue: String? = null,
    initialPosition: Float? = null,
    step: Int,
    onClick: (CameraUiEvent) -> Unit
) {
    when (step) {
        CameraViewModel.CAMERA -> CameraControllerThumbnail(
            modifier,
            model = model,
            badgeValue = badgeValue,
            initialPosition
        ) {
            onClick(CameraUiEvent.ThumbnailClicked(it))
        }

        CameraViewModel.PREVIEW -> EnterAnimation(
            modifier,
            enter = scaleIn + fadeIn(),
        ) {
            RotatableButton(
                Modifier,
                R.drawable.flash_off,
                initialPosition ?: 0f,
            ) {
                onClick(CameraUiEvent.RetakePhoto)
            }
        }

        CameraViewModel.GALLERY -> EnterAnimation(
            modifier,
            enter = scaleIn + fadeIn(),
        ) {
            RotatableButton(
                Modifier,
                R.drawable.flash_on,
                initialPosition ?: 0f,
            ) {
                onClick(CameraUiEvent.RetakePhoto)
            }
        }
    }
}

@Composable
private fun RotatableButton(
    modifier: Modifier,
    @DrawableRes icon: Int,
    initialPosition: Float,
    onClick: (() -> Unit)? = null
) {
    RotateToPosition(
        Modifier
            .wrapContentSize()
            .clip(CircleShape)
            .then(modifier),
        initialPosition,
    ) {
        IconButton(
            onClick = { onClick?.invoke() },
            modifier = Modifier
                .background(Color.Black, CircleShape)
                .size(56.dp)
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                tint = Color.White,
                painter = painterResource(icon),
                contentDescription = ""
            )
        }
    }
}
