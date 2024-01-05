package com.zivkesten.simplecamera.usecases

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import com.zivkesten.simplecamera.presentation.viewmodel.ERROR_SAVING_FILE
import java.io.File
import java.io.IOException
import java.util.UUID

private const val ERROR_CREATING_IMAGE = "Error creating image"

class GetTakenPhotoUseCaseImpl(
    private val context: Context,
) : GetTakenPhotoUseCase {

    override fun execute(
        fileName: String,
        onError: ((String, ImageCaptureException) -> Unit)?
    ): File? {
        return getPhotoFile(
            context = context,
            fileName,
            onErrorInvocation = onError
        )
    }

    @Suppress("TooGenericExceptionCaught")
    private fun getPhotoFile(
        context: Context,
        fileName: String,
        onErrorInvocation: ((String, ImageCaptureException) -> Unit)?
    ): File? {
        fun getOutputDirectory(): File? {
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, "simpleCamera").apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
        }
        var file: File? = null
        try {
            file = File(
                getOutputDirectory(),
                fileName
            )
        } catch (e: IOException) {
            onErrorInvocation?.invoke(
                fileName,
                ImageCaptureException(ImageCapture.ERROR_FILE_IO, ERROR_CREATING_IMAGE, e)
            )
        } catch (e: Exception) {
            onErrorInvocation?.invoke(
                fileName,
                ImageCaptureException(ImageCapture.ERROR_FILE_IO, ERROR_SAVING_FILE, e)
            )
        }
        return file
    }




    // Other private methods as necessary
}
