package com.zivkesten.simplecamera

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.Crossfade
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zivkesten.simplecamera.camera.controller.badgeTransition
import com.zivkesten.simplecamera.camera.controller.model.ImageData

const val ANIMATED_THUMBNAIL_TAG = "ANIMATED_THUMBNAIL"
const val NON_ANIMATED_THUMBNAIL_TAG = "NON_ANIMATED_THUMBNAIL"
const val BADGE_TAG = "BADGE_TAG"
const val TRASH_OVERLAY_TAG = "TRASH_OVERLAY_TAG"
typealias OnThumbnailSelected = (ImageData) -> Unit

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Thumbnail(
    modifier: Modifier,
    thumbnailBadgeEnterTransition: EnterTransition = fadeIn(),
    thumbnailBadgeExitTransition: ExitTransition = fadeOut(),
    reloadThumbnailTransition: ContentTransform? = null,
    model: ImageData,
    badgeValue: String? = null,
    onClick: (ImageData) -> Unit = {}
) {
    Box(modifier = Modifier.wrapContentWidth()) {
        if (reloadThumbnailTransition != null) {
            AnimatedContent(
                targetState = model,
                transitionSpec = { reloadThumbnailTransition },
                label = "Thumbnail AnimatedContent"
            ) { image ->
                ThumbnailImage(
                    Modifier
                        .testTag(ANIMATED_THUMBNAIL_TAG)
                        .align(Alignment.BottomCenter)
                        .padding(if (badgeValue == null) 0.dp else 10.dp)
                        .then(modifier),
                    image
                ) {
                    onClick(it)
                }
            }
        } else {
            ThumbnailImage(
                Modifier
                    .testTag(NON_ANIMATED_THUMBNAIL_TAG)
                    .align(Alignment.BottomCenter)
                    .padding(if (badgeValue == null) 0.dp else 10.dp)
                    .then(modifier),
                model
            ) {
                onClick(it)
            }
        }

        if (badgeValue != null) {
            EnterAnimation(
                Modifier.align(Alignment.TopEnd),
                enter = thumbnailBadgeEnterTransition,
                exit = thumbnailBadgeExitTransition
            ) {
                ThumbnailBadge(
                    Modifier.testTag(BADGE_TAG),
                    badgeValue = badgeValue
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
private fun ThumbnailBadge(
    modifier: Modifier,
    badgeValue: String
) {
    Box(
        modifier = modifier.then(
            Modifier
                .background(
                    Color.Black,
                    CircleShape
                )
                .size(25.dp)
        ),
    ) {
        AnimatedContent(
            modifier = Modifier.align(Alignment.Center),
            targetState = badgeValue,
            transitionSpec = { badgeTransition },
            label = "ThumbnailBadge AnimatedContent"
        ) { value ->
            Text(
                modifier = Modifier.padding(bottom = 1.dp),
                text = value,
                color = Color.White,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                ),

                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ThumbnailImage(
    modifier: Modifier,
    model: ImageData,
    onClick: (ImageData) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(70.dp)
            .then(modifier)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
            ) { onClick(model) }
            .clip(RoundedCornerShape(8.dp))
    ) {

        // TODO: coil image 
//        GlideImage(
//            modifier = Modifier.fillMaxSize(),
//            model = model.uri,
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//        )
        TrashOverlay(model)
    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
private fun TrashOverlay(model: ImageData) {
    Crossfade(targetState = model.selected, label = "TrashOverlay CrossFade") { selected ->
        if (selected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(3.dp, Color.White, RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.5f))
                    .semantics { contentDescription = TRASH_OVERLAY_TAG }

            ) {
                EnterAnimation(
                    modifier = Modifier.align(Alignment.Center),
                    enter = scaleIn(tween(durationMillis = 300)),
                ) {
                    // TODO: Trash icon 
//                    Icon(
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .testTag(TRASH_OVERLAY_TAG),
//                        painter = painterResource(id = R.drawable.ic_trash),
//                        tint = Color.White,
//                        contentDescription = ""
//                    )
                }
            }
        }
    }
}

sealed class ButtonAnimation {
    object None : ButtonAnimation()
    object EnterAnimation : ButtonAnimation()
    data class RotateToPosition(val initialPosition: Float) : ButtonAnimation()
}