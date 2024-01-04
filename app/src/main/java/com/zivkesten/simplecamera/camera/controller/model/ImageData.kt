package com.lemonadeinc.lemonade.ui.composable.camera.controller.model

import android.net.Uri
import androidx.compose.animation.core.MutableTransitionState

data class ImageData(
    val uri: Uri,
    val isFlash: Boolean,
    var selected: Boolean,
    val state: MutableTransitionState<Boolean>
)

fun List<ImageData>.copy(selected: Uri?): MutableList<ImageData> {
    return this.map { it.copy(selected = selected == it.uri) }.toMutableList()
}
