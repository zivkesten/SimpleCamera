package com.zivkesten.simplecamera.usecases

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import com.zivkesten.simplecamera.utils.Rotation

interface ImageCaptureUseCase {
     fun captureImage(
         imageCapture: ImageCapture,
         sensorOrientation: Rotation,
         onImageCaptured: (Uri, Boolean) -> Unit,
         onError: ((String, ImageCaptureException) -> Unit)?
    )
}
