package com.zivkesten.simplecamera.screens

import androidx.camera.core.CameraSelector
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.zivkesten.simplecamera.camera.CameraSurface
import com.zivkesten.simplecamera.camera.rememberCameraSurfaceHelper
import com.zivkesten.simplecamera.state.PhotoCollectionUiElementState
import com.zivkesten.simplecamera.state.PhotoCollectionUiState

internal const val CONTROLLER_SIZE: Float = 160f

@Composable
fun MainScreen(
    uiElementState: PhotoCollectionUiElementState,
) {

    val cameraSurfaceHelper = rememberCameraSurfaceHelper(
        LocalContext.current,
        LocalLifecycleOwner.current,
        CameraSelector.LENS_FACING_BACK
    )

    // -- Using CameraSurface and CameraScreensRouter can be used as camera flow everywhere -- //

    // Camera surface
    CameraSurface(
        modifier = Modifier,
        uiElementState.sensorOrientation,
        uiElementState.imageCapture,
        uiElementState.isFlashOn,
        cameraSurfaceHelper,
    )

    // Camera controller
    CameraUI(uiElementState.cameraControllerState,)
}
