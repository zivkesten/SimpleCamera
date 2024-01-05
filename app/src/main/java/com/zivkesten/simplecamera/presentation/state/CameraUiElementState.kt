package com.zivkesten.simplecamera.presentation.state

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.camera.core.AspectRatio.RATIO_16_9
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.AspectRatioStrategy.FALLBACK_RULE_AUTO
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.zivkesten.simplecamera.camera.controller.buttons.ShutterButtonState
import com.zivkesten.simpleCamera.R
import com.zivkesten.simplecamera.presentation.viewmodel.CameraViewModel
import com.zivkesten.simplecamera.utils.OrientationData
import com.zivkesten.simplecamera.utils.Rotation
import com.zivkesten.simplecamera.camera.controller.model.ImageData
import com.zivkesten.simplecamera.camera.controller.state.CameraControllerUiElementState
import com.zivkesten.simplecamera.presentation.event.CameraUiEvent
import com.zivkesten.simplecamera.utils.toSurfaceOrientation
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.UUID
import java.util.concurrent.Executors


@Deprecated("S")
class CameraUiElementState(
    private val activity: Activity,
    private val viewModel: CameraViewModel,
    private val onFlowComplete: (List<String>) -> Unit
) {









}

@Composable
fun rememberCameraUiElementState(
    activity: Activity,
    viewModel: CameraViewModel,
    onFlowComplete: (List<String>) -> Unit

) = remember {
    CameraUiElementState(
        activity,
        viewModel,
        onFlowComplete
    )
}