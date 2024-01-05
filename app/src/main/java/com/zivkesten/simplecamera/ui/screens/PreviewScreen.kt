package com.zivkesten.simplecamera.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zivkesten.simplecamera.camera.controller.CameraController
import com.zivkesten.simplecamera.camera.controller.cameraSurfacePadding
import com.zivkesten.simplecamera.camera.controller.state.CameraControllerUiElementState
import com.zivkesten.simplecamera.camera.preview.ImagePreview
import com.zivkesten.simplecamera.presentation.state.CameraUiState

@Composable
fun PreviewScreen(
    cameraUiState: CameraControllerUiElementState,
    boxSize: Float = 160f
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ImagePreview(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .cameraSurfacePadding(cameraUiState.orientationParams.sensorOrientation, boxSize),
            cameraUiState.imagesParams.selectedThumbnail,
        )
        CameraController(cameraUiState, boxSize)
    }
}
