package com.zivkesten.simplecamera.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zivkesten.simplecamera.camera.controller.CameraController
import com.lemonadeinc.lemonade.ui.composable.camera.controller.cameraSurfacePadding
import com.zivkesten.simplecamera.camera.controller.state.CameraControllerUiElementState
import com.zivkesten.simplecamera.camera.preview.ImagePreview

@Composable
fun PhotoCollectionPreviewScreen(
    uiElementState: CameraControllerUiElementState,
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
                .cameraSurfacePadding(uiElementState.orientationParams.sensorOrientation, boxSize),
            uiElementState.imagesParams.selectedThumbnail,
        )
        CameraController(uiElementState, boxSize)
    }
}