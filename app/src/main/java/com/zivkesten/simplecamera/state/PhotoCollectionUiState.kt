package com.zivkesten.simplecamera.state

import com.lemonadeinc.lemonade.ui.composable.camera.controller.model.ImageData

data class PhotoCollectionUiState(
    val step: Int = 0,
    val isSavingImage: Boolean = false,
    val takenImages: List<ImageData>,
)
