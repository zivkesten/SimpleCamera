package com.zivkesten.simplecamera.camera.controller.thumbnails

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.lemonadeinc.lemonade.ui.composable.camera.controller.thumbnails.ThumbnailList
import com.zivkesten.simplecamera.ui.animations.AnimateTo
import com.zivkesten.simplecamera.utils.Rotation
import com.zivkesten.simplecamera.ui.animations.SlideInEnterAnimation
import com.zivkesten.simplecamera.camera.controller.model.ImageData
import com.zivkesten.simplecamera.camera.controller.state.CameraControllerUiElementState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ThumbnailsColumn(
    modifier: Modifier,
    listState: LazyListState,
    thumbnailsDisplayParams: CameraControllerUiElementState.ThumbnailsDisplayParams,
    images: List<ImageData>,
    sensorOrientation: Rotation,
    onThumbnailSelected: OnThumbnailSelected,
) {
    if (thumbnailsDisplayParams.shouldShow) {
        val thumbnailListModifier = Modifier
            .then(modifier)
            .fillMaxHeight()
            .wrapContentWidth()
            .testTag(THUMBNAILS_COLUMN_TAG)
        if (thumbnailsDisplayParams.shouldAnimate) {
            val animateTo = when (sensorOrientation) {
                Rotation.ROTATION_90 -> AnimateTo.Left
                else -> AnimateTo.Right
            }
            SlideInEnterAnimation(thumbnailListModifier.testTag(THUMBNAILS_COLUMN_TAG), animateTo) {
                ThumbnailList(
                    modifier = Modifier.testTag(ANIMATED_COLUMN_TAG),
                    listState,
                    imagesTakes = images.reversed(),
                    onThumbnailSelected = onThumbnailSelected
                )
            }
        } else {
            Box(modifier = thumbnailListModifier.testTag(THUMBNAILS_COLUMN_TAG)) {
                ThumbnailList(
                    modifier = Modifier.align(Alignment.Center).testTag(NON_ANIMATED_COLUMN_TAG),
                    listState,
                    imagesTakes = images.reversed(),
                    onThumbnailSelected = onThumbnailSelected
                )
            }
        }
    }
}

const val THUMBNAILS_COLUMN_TAG = "THUMBNAILS_COLUMN_TAG"
const val ANIMATED_COLUMN_TAG = "ANIMATED_COLUMN_TAG"
const val NON_ANIMATED_COLUMN_TAG = "NON_ANIMATED_COLUMN_TAG"
