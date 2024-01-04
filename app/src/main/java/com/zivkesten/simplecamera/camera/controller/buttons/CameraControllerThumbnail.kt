package com.lemonadeinc.lemonade.ui.composable.camera.controller.buttons

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lemonadeinc.lemonade.ui.composable.Thumbnail
import com.lemonadeinc.lemonade.ui.composable.camera.controller.defaultTransition
import com.lemonadeinc.lemonade.ui.composable.camera.controller.model.ImageData
import com.lemonadeinc.lemonade.ui.composable.camera.controller.scaleIn
import com.lemonadeinc.ui.wrapper.RotateToPosition

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CameraControllerThumbnail(
    modifier: Modifier,
    model: ImageData? = null,
    badgeValue: String? = null,
    initialPosition: Float? = 0f,
    onClick: ((ImageData) -> Unit)? = null
) {
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
