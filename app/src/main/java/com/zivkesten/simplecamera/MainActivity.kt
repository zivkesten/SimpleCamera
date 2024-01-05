package com.zivkesten.simplecamera

import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.zivkesten.simplecamera.screens.MainScreen
import com.zivkesten.simplecamera.state.rememberPhotoCollectionUiElementState
import com.zivkesten.simplecamera.ui.theme.SimpleCameraTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: CameraViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            SimpleCameraTheme {
                // A surface container using the 'background' color from the theme
                val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
                val coroutineScope = rememberCoroutineScope()

                if (cameraPermissionState.status.isGranted) {
                    MainScreen(
                        rememberPhotoCollectionUiElementState(
                            this,
                            viewModel = viewModel,
                            onFlowComplete = {
                                Log.d("Zivi", "finish")
                            }
                        ),
                    )
                } else {
                    // Show a UI element that requests the permission
                    // For example, a Button that when clicked calls cameraPermissionState.launchPermissionRequest
                    Button(onClick = {
                        coroutineScope.launch {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    }) {
                        Text("Request Camera Permission")
                    }
                }
            }
        }
    }

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

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
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

