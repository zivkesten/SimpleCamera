package com.zivkesten.simplecamera.usecases

import androidx.camera.core.ImageCaptureException
import java.io.File

interface GetTakenPhotoUseCase {
    fun execute(
        fileName: String,
        onError: ((String, ImageCaptureException) -> Unit)? = null
    ): File?
}
