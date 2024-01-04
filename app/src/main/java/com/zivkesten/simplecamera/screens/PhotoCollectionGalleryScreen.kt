package com.lemonadeinc.lemonade.ui.feature.photocollection.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import com.lemonadeinc.common.utils.extension.reverseIndexFrom
import com.zivkesten.simplecamera.camera.controller.CameraController
import com.lemonadeinc.lemonade.ui.composable.camera.controller.CameraControllerAlignment
import com.lemonadeinc.lemonade.ui.composable.camera.controller.cameraSurfacePadding
import com.lemonadeinc.lemonade.ui.composable.camera.controller.model.ImageData
import com.zivkesten.simplecamera.camera.controller.state.CameraControllerUiElementState
import com.lemonadeinc.lemonade.ui.composable.camera.controller.thumbnails.ThumbnailsColumn
import com.lemonadeinc.lemonade.ui.composable.camera.controller.thumbnails.ThumbnailsRow
import com.lemonadeinc.lemonade.ui.composable.camera.preview.ImagePreview
import com.zivkesten.simplecamera.event.CameraUiEvent
import com.lemonadeinc.lemonade.utils.android.Rotation
import com.lemonadeinc.lemonade.utils.android.isLandscape
import com.lemonadeinc.lemonade.utils.android.isPortrait
import com.zivkesten.simplecamera.screens.CONTROLLER_SIZE
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoCollectionGalleryScreen(
    uiElementState: CameraControllerUiElementState,
    boxSize: Float,
) {
    val pagerState = rememberPagerState {
        uiElementState.imagesParams.imagesTaken.size
    }
    val listState = rememberLazyListState()
    var images by remember { mutableStateOf(uiElementState.imagesParams.imagesTaken) }

    LaunchedEffect(pagerState.currentPage) {
        listState.animateScrollToItem(
            when (uiElementState.orientationParams.sensorOrientation) {
                Rotation.ROTATION_0, Rotation.ROTATION_180 ->
                    pagerState.currentPage

                Rotation.ROTATION_270, Rotation.ROTATION_90 ->
                    pagerState.currentPage.reverseIndexFrom(
                        images.size
                    )
            }
        )
    }

    LaunchedEffect(uiElementState.imagesParams.scrollTo) {
        // We remember those values in the pager scope function to use it later
        // In the pager's snapshotFlow
        images = uiElementState.imagesParams.imagesTaken
        val scrollToPageIndex = images.indexOfFirst {
            it.uri == uiElementState.imagesParams.scrollTo
        }
        pagerState.animateScrollToPage(scrollToPageIndex)
    }

    LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the targetPage
        // This is the page index the animation will land on
        snapshotFlow { pagerState.currentPage }.collect { page ->
            // If we have a scrollTo value, we need to notify the view model which image is viewed
            uiElementState.onUiEvent(CameraUiEvent.PreviewImageViewed(images[page]))
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

        val params = ThumbnailsListParams(
            Modifier,
            uiElementState,
            boxSize,
            listState,
        )
        OrientationAwareSlidingPagerImage(
            uiElementState,
            boxSize,
            pagerState
        )
        CameraController(
            uiElementState, CONTROLLER_SIZE, thumbnailsContent = {
                when (uiElementState.orientationParams.sensorOrientation) {
                    Rotation.ROTATION_0, Rotation.ROTATION_180 -> {
                        if (isPortrait()) {
                            ThumbnailsRowWithAlignment(
                                params.apply {
                                    modifier = Modifier.align(
                                        uiElementState
                                            .orientationParams
                                            .sensorOrientation
                                            .CameraControllerAlignment()
                                    )
                                },
                            )
                        } else {
                            ThumbnailColumnWithAlignment(
                                params.apply { modifier = Modifier.align(Alignment.CenterEnd) },
                            )
                        }
                    }

                    Rotation.ROTATION_90, Rotation.ROTATION_270 -> {
                        if (isLandscape()) {
                            ThumbnailColumnWithAlignment(
                                params.apply {
                                    modifier = Modifier.align(
                                        uiElementState
                                            .orientationParams
                                            .sensorOrientation
                                            .CameraControllerAlignment()
                                    )
                                },
                            )
                        } else {
                            ThumbnailsRowWithAlignment(
                                params.apply { modifier = Modifier.align(Alignment.BottomCenter) },
                            )
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OrientationAwareSlidingPagerImage(
    uiElementState: CameraControllerUiElementState,
    boxSize: Float,
    pagerState: PagerState
) {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .cameraSurfacePadding(
                uiElementState.orientationParams.sensorOrientation,
                boxSize
            )
    ) {
        SlidingPreviewImage(pagerState, uiElementState.imagesParams.imagesTaken)
    }
}

@Composable
private fun ThumbnailColumnWithAlignment(params: ThumbnailsListParams) {
    ThumbnailsColumn(
        Modifier.Companion
            .then(params.modifier)
            .cameraSurfacePadding(
                params.uiElementState.orientationParams.sensorOrientation,
                params.boxSize
            ),
        params.listState,
        params.uiElementState.thumbnailsDisplayParams,
        params.uiElementState.imagesParams.imagesTaken,
        params.uiElementState.orientationParams.sensorOrientation,
        onThumbnailSelected = {
            params.uiElementState.onUiEvent(CameraUiEvent.ThumbnailRowItemClicked(it))
        }
    )
}

@Composable
private fun ThumbnailsRowWithAlignment(params: ThumbnailsListParams) {
    ThumbnailsRow(
        Modifier
            .then(params.modifier)
            .cameraSurfacePadding(
                params.uiElementState.orientationParams.sensorOrientation,
                params.boxSize
            ),
        params.listState,
        params.uiElementState.thumbnailsDisplayParams,
        params.uiElementState.imagesParams.imagesTaken,
        onThumbnailClicked = {
            params.uiElementState.onUiEvent(CameraUiEvent.ThumbnailRowItemClicked(it))
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlidingPreviewImage(
    pagerState: PagerState,
    images: List<ImageData>
) {
    HorizontalPager(
        reverseLayout = true,
        state = pagerState
    ) { pageIndex ->
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = (
                        (pagerState.currentPage - pageIndex) + pagerState
                            .currentPageOffsetFraction
                        ).absoluteValue

                    // We animate the scale, between 70% and 100%
                    scaleX = lerp(
                        start = 0.7f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )

                    scaleY = lerp(
                        start = 0.7f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {
            ImagePreview(Modifier.fillMaxSize(), images[pageIndex].uri.path)
        }
    }
}

data class ThumbnailsListParams(
    var modifier: Modifier,
    val uiElementState: CameraControllerUiElementState,
    val boxSize: Float,
    val listState: LazyListState,
)
