package com.zivkesten.simplecamera.camera.controller

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import com.zivkesten.simplecamera.utils.Rotation
import com.zivkesten.simplecamera.utils.isLandscape
import com.zivkesten.simplecamera.utils.isPortrait

// Camera top bar
internal fun Modifier.cameraTopBarPadding(rotation: Rotation, boxSize: Float) = composed {
    when (rotation) {
        Rotation.ROTATION_0, Rotation.ROTATION_180 ->
            if (isLandscape()) {
                this.then(topBarLandscapeModifier(boxSize))
            } else {
                this.then(
                    portraitModifier
                )
            }

        Rotation.ROTATION_90 -> {
            if (isPortrait()) {
                this.then(portraitModifier)
            } else {
                this.then(topBarLandscapeModifier(boxSize))
            }
        }

        Rotation.ROTATION_270 -> {
            if (isPortrait()) {
                this.then(portraitModifier)
            } else {
                this.then(topBarReverseLandscapeModifier(boxSize))
            }
        }
    }
}

internal fun Modifier.flashAnimationPadding(rotation: Rotation, boxSize: Float) = composed {
    when (rotation) {
        Rotation.ROTATION_0 ->
            if (isLandscape()) {
                this.then(flashLandscapeModifier(boxSize))
            } else {
                this.then(flashPortraitModifier(boxSize))
            }

        Rotation.ROTATION_180 -> if (isLandscape()) {
            this.then(flashLandscapeModifier(boxSize))
        } else {
            this.then(flashReversePortraitModifier(boxSize))
        }

        Rotation.ROTATION_90 -> {
            if (isPortrait()) {
                this.then(flashPortraitModifier(boxSize))
            } else {
                this.then(topBarLandscapeModifier(boxSize))
            }
        }

        Rotation.ROTATION_270 -> {
            if (isPortrait()) {
                this.then(flashPortraitModifier(boxSize))
            } else {
                this.then(flashReverseLandscapeModifier(boxSize))
            }
        }
    }
}

internal fun Modifier.flashPortraitModifier(boxSize: Float) = this.padding(bottom = boxSize.dp)

internal fun Modifier.flashReversePortraitModifier(boxSize: Float) = this.padding(top = boxSize.dp)

internal fun Modifier.flashReverseLandscapeModifier(boxSize: Float) = this.padding(end = boxSize.dp)

internal fun Modifier.flashLandscapeModifier(boxSize: Float) = this.padding(start = boxSize.dp)

private fun Modifier.topBarReverseLandscapeModifier(boxSize: Float) = this
    .padding(
        bottom = 20.dp,
        end = 32.dp,
        start = (boxSize + TOP_BAR_PADDING).dp,
        top = 65.dp
    )

private const val TOP_BAR_PADDING = 32

internal fun Modifier.topBarLandscapeModifier(boxSize: Float) = this
    .padding(
        bottom = 20.dp,
        start = 32.dp,
        end = (boxSize + TOP_BAR_PADDING).dp,
        top = 32.dp
    )

internal val portraitModifier = Modifier
    .padding(
        bottom = 20.dp,
        start = 32.dp,
        end = 32.dp,
        top = 32.dp
    )

// CameraSurface
fun Modifier.cameraSurfacePadding(sensorOrientation: Rotation, boxSize: Float) = composed {
    when (sensorOrientation) {
        Rotation.ROTATION_0 -> {
            if (isLandscape()) {
                Modifier.padding(end = boxSize.dp)
            } else {
                Modifier.padding(bottom = boxSize.dp)
            }
        }

        Rotation.ROTATION_90 -> {
            if (isPortrait()) {
                Modifier.padding(bottom = boxSize.dp)
            } else {
                Modifier.padding(end = boxSize.dp)
            }
        }

        Rotation.ROTATION_180 -> {
            if (isLandscape()) {
                Modifier.padding(end = boxSize.dp)
            } else {
                Modifier.padding(top = boxSize.dp)
            }
        }

        Rotation.ROTATION_270 -> {
            if (isPortrait()) {
                Modifier.padding(bottom = boxSize.dp)
            } else {
                Modifier.padding(start = boxSize.dp)
            }
        }
    }
}

@Composable
fun Rotation.cameraControllerAlignment() = when (this) {
    Rotation.ROTATION_0 -> if (isLandscape()) Alignment.CenterEnd else Alignment.BottomCenter
    Rotation.ROTATION_90 -> if (isPortrait()) Alignment.BottomCenter else Alignment.CenterEnd
    Rotation.ROTATION_180 -> if (isLandscape()) Alignment.CenterEnd else Alignment.TopCenter
    Rotation.ROTATION_270 -> if (isPortrait()) Alignment.BottomCenter else Alignment.CenterStart
}
