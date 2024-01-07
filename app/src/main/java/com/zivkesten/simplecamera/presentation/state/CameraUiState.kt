package com.zivkesten.simplecamera.presentation.state

import com.zivkesten.simplecamera.camera.controller.model.ImageData
import com.zivkesten.simplecamera.camera.controller.state.CameraControllerUiElementState

data class CameraUiState(
    val isSavingImage: Boolean = false,
    val cameraUiElementState: CameraControllerUiElementState,
    val isFlashOn: Boolean = false
)
