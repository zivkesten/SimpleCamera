package com.zivkesten.simplecamera.usecases

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import com.zivkesten.simplecamera.utils.Rotation

interface ImageCaptureUseCase {

    val imageCapture: ImageCapture

    fun captureImage(
        sensorOrientation: Rotation,
        onImageCaptured: (Uri, Boolean) -> Unit,
        onError: ((String, ImageCaptureException) -> Unit)?
    )
}
