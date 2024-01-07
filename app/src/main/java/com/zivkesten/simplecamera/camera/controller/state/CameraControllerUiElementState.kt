package com.zivkesten.simplecamera.camera.controller.state

import android.net.Uri
import com.zivkesten.simplecamera.camera.controller.buttons.ShutterButtonState
import com.zivkesten.simplecamera.camera.controller.model.ImageData
import com.zivkesten.simplecamera.presentation.viewmodel.CameraViewModel.Companion.CAMERA
import com.zivkesten.simplecamera.presentation.viewmodel.CameraViewModel.Companion.GALLERY
import com.zivkesten.simplecamera.presentation.viewmodel.CameraViewModel.Companion.PREVIEW
import com.zivkesten.simplecamera.presentation.event.CameraUiEvent
import com.zivkesten.simplecamera.camera.controller.thumbnails.ButtonAnimation
import com.zivkesten.simplecamera.utils.OrientationData
import com.zivkesten.simplecamera.utils.Rotation
import com.zivkesten.simplecamera.utils.initialPosition

data class CameraControllerUiElementState(
    val screen: Int,
    val shutterButtonState: ShutterButtonState = ShutterButtonState.ENABLED,
    val imagesParams: ImagesParams,
    val orientationParams: OrientationParams,
    val onUiEvent: (CameraUiEvent) -> Unit,
) {
    data class OrientationParams(
        val orientationData: OrientationData,
        val sensorOrientation: Rotation,
    )

    data class ImagesParams(
        val imagesTaken: List<ImageData>,
        val selectedThumbnail: Uri?,
        val scrollTo: Uri?,
        val isFlashOn: Boolean,
    )

    data class ThumbnailsDisplayParams(val shouldShow: Boolean, val shouldAnimate: Boolean)

    private val shouldShowEndButton: Boolean
        get() = imagesParams.imagesTaken.isNotEmpty() ||
            screen != CAMERA

    val endButtonAnimation: ButtonAnimation?
        get() = when {
            !shouldShowEndButton -> null
            orientationParams.orientationData.previous == null &&
                imagesParams.imagesTaken.size == 1 -> ButtonAnimation.EnterAnimation
            else -> ButtonAnimation.RotateToPosition(
                orientationParams.orientationData.initialPosition()
            )
        }

    val showCenterButton get() = screen == CAMERA

    val showEndButton get() = screen == CAMERA || screen == PREVIEW

    private val showThumbnails get() = screen == GALLERY

    private val animateThumbnails: Boolean get() = orientationParams.orientationData.previous == null

    val thumbnailsDisplayParams: ThumbnailsDisplayParams
        get() = ThumbnailsDisplayParams(
            shouldShow = showThumbnails,
            shouldAnimate = animateThumbnails
        )
}
