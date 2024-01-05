package com.zivkesten.simplecamera.presentation.state

import com.zivkesten.simplecamera.camera.controller.model.ImageData

data class CameraUiState(
    val step: Int = 0,
    val isSavingImage: Boolean = false,
    val takenImages: List<ImageData>,
)
