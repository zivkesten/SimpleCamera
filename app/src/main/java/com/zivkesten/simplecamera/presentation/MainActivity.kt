package com.zivkesten.simplecamera.presentation

import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.view.WindowCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.zivkesten.simplecamera.presentation.state.rememberCameraUiElementState
import com.zivkesten.simplecamera.presentation.viewmodel.CameraViewModel
import com.zivkesten.simplecamera.ui.screens.MainScreen
import com.zivkesten.simplecamera.ui.screens.PermissionRequestScreen
import com.zivkesten.simplecamera.ui.theme.SimpleCameraTheme
import com.zivkesten.simplecamera.utils.hideSystemUI
import com.zivkesten.simplecamera.utils.onGlobalLayout
import com.zivkesten.simplecamera.utils.toSensorOrientation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: CameraViewModel by viewModels()

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }
                val rotation = when (orientation) {
                    in Q1_RANGE -> Surface.ROTATION_270
                    in Q2_RANGE -> Surface.ROTATION_180
                    in Q3_RANGE -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                viewModel.sensorOrientation.value = rotation.toSensorOrientation()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            SimpleCameraTheme {
                val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
                val coroutineScope = rememberCoroutineScope()
                val uiState = viewModel.cameraUiState
                if (cameraPermissionState.status.isGranted) {
//                    val uiElementState = rememberCameraUiElementState(
//                        this,
//                        viewModel = viewModel,
//                        onFlowComplete = {
//                            Log.d("Zivi", "finish")
//                        }
//                    )
                    MainScreen(uiState.collectAsState().value, viewModel.imageCapture)
                } else {
                    PermissionRequestScreen(coroutineScope, cameraPermissionState)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onResume() {
        super.onResume()
        onGlobalLayout { hideSystemUI() }
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }



    companion object {
        private val Q1_RANGE = 45 until 135
        private val Q2_RANGE = 135 until 225
        private val Q3_RANGE = 225 until 315

    }
}

