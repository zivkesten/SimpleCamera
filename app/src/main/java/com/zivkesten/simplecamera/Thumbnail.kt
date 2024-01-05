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
                interactionSource = MutableInteractionSource(),
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

fun ButtonAnimation.name() = when (this) {
    ButtonAnimation.None -> "None"
    ButtonAnimation.EnterAnimation -> "EnterAnimation"
    is ButtonAnimation.RotateToPosition -> "RotateToPosition"
}

//
//
//implementation("androidx.core:core-ktx:1.12.0")
//implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
//implementation("androidx.activity:activity-compose:1.8.2")
//implementation(platform("androidx.compose:compose-bom:2023.08.00"))
//implementation("androidx.compose.ui:ui")
//implementation("androidx.compose.ui:ui-graphics")
//implementation("androidx.compose.ui:ui-tooling-preview")
//implementation("androidx.compose.material3:material3")
//implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")
//implementation("androidx.camera:camera-core:1.3.1")
//implementation("androidx.fragment:fragment-ktx:1.6.2")
//implementation("androidx.camera:camera-view:1.3.1")
//implementation("androidx.camera:camera-lifecycle:1.3.1")
//
//// Hilt for Dependency Injection
//implementation("com.google.dagger:hilt-android:2.50")
//kapt("com.google.dagger:hilt-compiler:2.50")
//
//// For instrumentation tests
//androidTestImplementation("com.google.dagger:hilt-android-testing:2.50")
//kaptAndroidTest("com.google.dagger:hilt-compiler:2.50")
//
//// For local unit tests
//testImplementation("com.google.dagger:hilt-android-testing:2.50")
//kaptTest("com.google.dagger:hilt-compiler:2.50")
//
//testImplementation("junit:junit:4.13.2")
//androidTestImplementation("androidx.test.ext:junit:1.1.5")
//androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
//androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//debugImplementation("androidx.compose.ui:ui-tooling")
//debugImplementation("androidx.compose.ui:ui-test-manifest")
