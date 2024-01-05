package com.zivkesten.simplecamera.camera.controller.thumbnails

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.lemonadeinc.lemonade.ui.composable.camera.controller.thumbnails.ThumbnailList
import com.zivkesten.simplecamera.OnThumbnailSelected
import com.zivkesten.simplecamera.SlideUpEnterAnimation
import com.zivkesten.simplecamera.camera.controller.model.ImageData
import com.zivkesten.simplecamera.camera.controller.state.CameraControllerUiElementState

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun ThumbnailsRow(
    modifier: Modifier,
    listState: LazyListState,
    thumbnailsConfig: CameraControllerUiElementState.ThumbnailsDisplayParams,
    images: List<ImageData>,
    onThumbnailClicked: OnThumbnailSelected,
) {
    if (thumbnailsConfig.shouldShow) {
        val thumbnailListModifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .wrapContentHeight()
            .testTag(THUMBNAILS_ROW_TAG)
        if (thumbnailsConfig.shouldAnimate) {
            SlideUpEnterAnimation(modifier = thumbnailListModifier) {
                ThumbnailList(
                    Modifier.testTag(ANIMATED_ROW_TAG),
                    listState,
                    images,
                    onThumbnailSelected = onThumbnailClicked
                )
            }
        } else {
            Box(modifier = thumbnailListModifier) {
                ThumbnailList(
                    Modifier
                        .align(Alignment.Center)
                        .testTag(NON_ANIMATED_ROW_TAG),
                    listState,
                    images,
                    onThumbnailSelected = onThumbnailClicked
                )
            }
        }
    }
}

const val THUMBNAILS_ROW_TAG = "THUMBNAILS_ROW_TAG"
const val ANIMATED_ROW_TAG = "ANIMATED_ROW_TAG"
const val NON_ANIMATED_ROW_TAG = "NON_ANIMATED_ROW_TAG"
