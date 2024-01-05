package com.zivkesten.simplecamera.camera.controller.buttons

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zivkesten.simplecamera.ui.RotateToPosition
import com.zivkesten.simplecamera.camera.controller.thumbnails.Thumbnail
import com.zivkesten.simplecamera.camera.controller.defaultTransition
import com.zivkesten.simplecamera.camera.controller.model.ImageData
import com.zivkesten.simplecamera.camera.controller.scaleIn

@Composable
fun CameraControllerThumbnail(
    modifier: Modifier,
    model: ImageData? = null,
    badgeValue: String? = null,
    initialPosition: Float? = 0f,
    onClick: ((ImageData) -> Unit)? = null
) {
    Log.d("Zivi", "CameraControllerThumbnail: $model")
    if (model != null) {
        RotateToPosition(
            modifier = modifier,
            initialPosition ?: 0f,
        ) {
            Thumbnail(
                Modifier,
                scaleIn,
                reloadThumbnailTransition = defaultTransition,
                model = model.apply { selected = false },
                badgeValue = badgeValue,
            ) {
                onClick?.invoke(it)
            }
        }
    }
}
