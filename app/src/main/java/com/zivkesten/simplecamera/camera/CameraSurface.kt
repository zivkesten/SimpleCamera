package com.zivkesten.simplecamera.camera

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.zivkesten.simplecamera.camera.controller.cameraSurfacePadding
import com.zivkesten.simplecamera.Rotation
import com.zivkesten.simplecamera.screens.CONTROLLER_SIZE
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraSurface(
    modifier: Modifier,
    sensorOrientation: Rotation,
    imageCapture: ImageCapture,
    isFlashOn: Boolean,
    cameraSurfaceHelper: CameraSurfaceHelper,
) {
    LaunchedEffect(Unit) {
        val cameraProvider = cameraSurfaceHelper.context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            cameraSurfaceHelper.lifecycleOwner,
            cameraSurfaceHelper.cameraSelector,
            cameraSurfaceHelper.preview,
            imageCapture,
        )
        cameraSurfaceHelper.preview.setSurfaceProvider(cameraSurfaceHelper.previewView.surfaceProvider)
    }

    imageCapture.flashMode = if (isFlashOn) FLASH_MODE_ON else FLASH_MODE_OFF

    Box(
        modifier = Modifier
            .fillMaxSize()
            .cameraSurfacePadding(sensorOrientation, CONTROLLER_SIZE)
            .then(modifier),
        contentAlignment = Alignment.TopCenter
    ) {
        AndroidView({ cameraSurfaceHelper.previewView }, modifier = Modifier.fillMaxSize())
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }
