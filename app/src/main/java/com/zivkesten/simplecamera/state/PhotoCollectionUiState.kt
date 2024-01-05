package com.zivkesten.simplecamera.state

import com.zivkesten.simplecamera.camera.controller.model.ImageData

data class PhotoCollectionUiState(
    val step: Int = 0,
    val isSavingImage: Boolean = false,
    val takenImages: List<ImageData>,
)
