package com.zivkesten.simplecamera.usecases

import android.content.Context
import android.net.Uri
import android.view.Surface
import androidx.camera.core.AspectRatio
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import com.zivkesten.simplecamera.utils.Rotation
import java.io.File
import java.io.IOException
import java.util.UUID
import java.util.concurrent.Executors

private const val ERROR_CREATING_IMAGE = "Error creating image"

const val ERROR_SAVING_FILE = "Error saving file"
private const val JPG_SUFFIX = ".jpg"

class ImageCaptureUseCaseImpl(
    private val context: Context,
) : ImageCaptureUseCase {

    override val imageCapture: ImageCapture = ImageCapture.Builder().setResolutionSelector(
        ResolutionSelector.Builder()
        .setAspectRatioStrategy(AspectRatioStrategy(AspectRatio.RATIO_16_9, AspectRatioStrategy.FALLBACK_RULE_AUTO) )
        .build()).build()

    override fun captureImage(
        sensorOrientation: Rotation,
        onImageCaptured: (Uri, Boolean) -> Unit,
        onError: ((String, ImageCaptureException) -> Unit)?
    ) {
        imageCapture.targetRotation = sensorOrientation.toSurfaceOrientation()

        val fileName = generateTempFileName()
        val imageFile = getPhotoFile(fileName, onError)

        if (imageFile != null) {
            val outputOptions = ImageCapture.OutputFileOptions.Builder(imageFile).build()
            imageCapture.takePicture(
                outputOptions,
                Executors.newSingleThreadExecutor(),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exception: ImageCaptureException) {
                        onError?.invoke(fileName, exception)
                    }

                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        outputFileResults.savedUri?.let {
                            onImageCaptured(
                                it,
                                imageCapture.flashMode == ImageCapture.FLASH_MODE_ON
                            )
                        }
                    }
                }
            )
        } else {
            onError?.invoke(
                fileName,
                ImageCaptureException(ImageCapture.ERROR_FILE_IO, ERROR_SAVING_FILE, null)
            )
        }
    }

    private fun getPhotoFile(
        fileName: String,
        onErrorInvocation: ((String, ImageCaptureException) -> Unit)?
    ): File? {
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

    private fun getOutputDirectory(): File? {
        // TODO: Move to MediaStore
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, "simpleCamera").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
    }


    private fun generateTempFileName(): String {
        val id: String = UUID.randomUUID().toString()
        return id + JPG_SUFFIX
    }

    private fun Rotation.toSurfaceOrientation() = when (this) {
        Rotation.ROTATION_0 -> Surface.ROTATION_0
        Rotation.ROTATION_90 -> Surface.ROTATION_90
        Rotation.ROTATION_180 -> Surface.ROTATION_180
        Rotation.ROTATION_270 -> Surface.ROTATION_270
    }
}
