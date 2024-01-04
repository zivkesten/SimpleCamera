package com.lemonadeinc.lemonade.ui.composable.camera.controller.thumbnails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lemonadeinc.lemonade.ui.composable.OnThumbnailSelected
import com.lemonadeinc.lemonade.ui.composable.Thumbnail
import com.lemonadeinc.lemonade.ui.composable.camera.controller.model.ImageData
import com.lemonadeinc.lemonade.ui.composable.camera.controller.thumbnailExitAnimation
import com.lemonadeinc.lemonade.utils.android.isPortrait
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun ThumbnailList(
    modifier: Modifier,
    listState: LazyListState,
    imagesTakes: List<ImageData>,
    onThumbnailSelected: OnThumbnailSelected,
) {
    Box(modifier) {
        if (isPortrait()) {
            HorizontalThumbnails(
                Modifier.align(Alignment.Center),
                listState,
                imagesTakes,
                onThumbnailSelected
            )
        } else {
            VerticalThumbnails(
                Modifier.align(Alignment.Center),
                listState,
                imagesTakes,
                onThumbnailSelected
            )
        }
    }
}

@Composable
private fun HorizontalThumbnails(
    modifier: Modifier,
    state: LazyListState,
    imagesTakes: List<ImageData>,
    onThumbnailSelected: OnThumbnailSelected
) {
    ThumbnailsListSideEffects(imagesTakes, onThumbnailSelected)
    LazyRow(
        reverseLayout = true,
        state = state,
        modifier = modifier
    ) {
        item {
            Spacer(modifier = Modifier.width(32.dp))
        }
        items(imagesTakes) { image ->
            AnimatedThumbnail(image, onThumbnailSelected)
        }
    }
}

@Composable
private fun VerticalThumbnails(
    modifier: Modifier,
    state: LazyListState,
    imagesTakes: List<ImageData>,
    onThumbnailSelected: OnThumbnailSelected
) {
    ThumbnailsListSideEffects(imagesTakes, onThumbnailSelected)
    LazyColumn(
        reverseLayout = true,
        state = state,
        modifier = modifier
    ) {
        items(imagesTakes) { image ->
            AnimatedThumbnail(image, onThumbnailSelected)
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ThumbnailsListSideEffects(
    imagesTakes: List<ImageData>,
    onThumbnailSelected: OnThumbnailSelected
) {
    LaunchedEffect(imagesTakes) {
        snapshotFlow {
            imagesTakes.firstOrNull { it.state.notAnimating() && it.state.invisible() }
        }.filterNotNull().collect(onThumbnailSelected)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimatedThumbnail(
    image: ImageData,
    onThumbnailSelected: OnThumbnailSelected
) {
    AnimatedVisibility(
        visibleState = image.state,
        exit = thumbnailExitAnimation
    ) {
        Thumbnail(
            model = image,
            modifier = Modifier
                .size(70.dp)
                .padding(horizontal = 4.dp)
                .padding(vertical = 4.dp)
        ) {
            if (image.selected) {
                animateItemOut(image)
            } else {
                onThumbnailSelected(image)
            }
        }
    }
}

private fun animateItemOut(item: ImageData) {
    item.state.targetState = false
}

fun MutableTransitionState<Boolean>.notAnimating() = isIdle

fun MutableTransitionState<Boolean>.invisible() = !targetState
