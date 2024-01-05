package com.zivkesten.simplecamera.ui.screens

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.zivkesten.simplecamera.camera.CameraSurface
import com.zivkesten.simplecamera.camera.rememberCameraSurfaceHelper
import com.zivkesten.simplecamera.presentation.state.CameraUiState

internal const val CONTROLLER_SIZE: Float = 160f

@Composable
fun MainScreen(
    uiState: CameraUiState,
    imageCapture: ImageCapture
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
        uiState.cameraUiElementState.orientationParams.sensorOrientation,
        imageCapture,
        uiState.isFlashOn,
        cameraSurfaceHelper,
    )

    // Camera controller
    CameraUI(uiState.cameraUiElementState)

    LaunchedEffect(uiState.cameraUiElementState.shutterButtonState){
        Log.d("Zivi", "MainScreen: ${uiState.cameraUiElementState.shutterButtonState}")
    }

}
