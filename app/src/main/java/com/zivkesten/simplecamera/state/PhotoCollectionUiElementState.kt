package com.zivkesten.simplecamera.state

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.util.Log
import androidx.camera.core.AspectRatio.RATIO_16_9
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import com.lemonadeinc.lemonade.ui.composable.camera.controller.buttons.ShutterButtonState
import com.lemonadeinc.lemonade.ui.composable.camera.controller.model.ImageData
import com.zivkesten.simplecamera.OrientationData
import com.zivkesten.simplecamera.CameraViewModel
import com.zivkesten.simplecamera.R
import com.zivkesten.simplecamera.Rotation
import com.zivkesten.simplecamera.camera.controller.state.CameraControllerUiElementState
import com.zivkesten.simplecamera.event.CameraUiEvent
import com.zivkesten.simplecamera.toSurfaceOrientation
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.UUID
import java.util.concurrent.Executors

private const val JPG_SUFFIX = ".jpg"
private const val ERROR_CREATING_IMAGE = "Error creating image"
const val ERROR_SAVING_FILE = "Error saving file"

class PhotoCollectionUiElementState(
    private val activity: Activity,
    private val viewModel: CameraViewModel,
    private val onFlowComplete: (List<String>) -> Unit
) {

    val imageCapture: ImageCapture = ImageCapture.Builder().setTargetAspectRatio(RATIO_16_9).build()

    val orientationData: OrientationData get() = viewModel.orientationData

    val sensorOrientation: Rotation
        get() = viewModel.sensorOrientation.value


    var isFlashOn by mutableStateOf(false)


    val uiState: PhotoCollectionUiState get() = viewModel.uiState.value

    private val shouldDisableButton get() = false

    private val shutterButtonState: ShutterButtonState
        get() = when {
            uiState.isSavingImage -> ShutterButtonState.LOADING
            shouldDisableButton -> ShutterButtonState.DISABLED
            else -> ShutterButtonState.ENABLED
        }

    val cameraControllerState get() = CameraControllerUiElementState(
        uiState.step,
        shutterButtonState,
        CameraControllerUiElementState.ImagesParams(
            uiState.takenImages,
            viewModel.imageTakenUri,
            viewModel.scrollTo,
            isFlashOn,
        ),
        CameraControllerUiElementState.OrientationParams(
            orientationData,
            sensorOrientation,
        )
    ) { onUIEvent(it) }

    init {
        observeAttachments()
    }

    fun onUIEvent(event: CameraUiEvent) {
        when (event) {
            // element state events
            is CameraUiEvent.ImageFail -> Unit // TODO: Handle
            is CameraUiEvent.FlashButtonClicked -> isFlashOn = !isFlashOn
            is CameraUiEvent.ImageCaptured -> onImageCaptured()
            is CameraUiEvent.CloseButtonClicked -> {
                // TODO: Handle
                Log.e("Zivi", "close clicked")
            }
            CameraUiEvent.ContinueBtnPressed -> {
                viewModel.onEvent(event)
            }
            // view model events
            is CameraUiEvent.ImageSaved,
            is CameraUiEvent.RetakePhoto,
            is CameraUiEvent.ThumbnailClicked,
            is CameraUiEvent.ThumbnailRowItemClicked,
            is CameraUiEvent.PreviewImageViewed -> viewModel.onEvent(event)
        }
    }

    private fun onImageCaptured() {
        viewModel.setIsSavingImage()
        takePhoto(
            onImageCaptured = { uri, isFlash ->
                onUIEvent(
                    CameraUiEvent.ImageSaved(
                        ImageData(uri, isFlash, false, MutableTransitionState(true))
                    )
                )
            },
            onErrorInvocation = { fileName: String, imageCaptureException: ImageCaptureException ->
                onUIEvent(CameraUiEvent.ImageFail(fileName, imageCaptureException.message))
            }
        )
    }

    fun closeButton(
        viewModel: CameraViewModel,
        action: (CameraViewModel) -> Unit
    ) = action(viewModel)



    private fun observeAttachments() {
        viewModel.viewModelScope.launch {
            viewModel.attachmentsItem.collect { attachments ->
                if (attachments != null) {
                    onFlowComplete(attachments)
                }
            }
        }
    }

    fun generateTempFileName(extension: String): String {
        val id: String = UUID.randomUUID().toString()
        return id + extension
    }


    private fun takePhoto(
        onImageCaptured: (Uri, Boolean) -> Unit,
        onErrorInvocation: ((String, ImageCaptureException) -> Unit)? = null
    ) {
        imageCapture.targetRotation = sensorOrientation.toSurfaceOrientation()
        val fileName = generateTempFileName(JPG_SUFFIX)

        val photoFile: File? = getPhotoFile(activity, fileName, onErrorInvocation)

        if (photoFile != null) {
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
            imageCapture.takePicture(
                outputOptions,
                Executors.newSingleThreadExecutor(),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exception: ImageCaptureException) {
                        onErrorInvocation?.invoke(fileName, exception)
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
            onErrorInvocation?.invoke(
                fileName,
                ImageCaptureException(ImageCapture.ERROR_FILE_IO, ERROR_SAVING_FILE, null)
            )
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun getPhotoFile(
        activity: Activity,
        fileName: String,
        onErrorInvocation: ((String, ImageCaptureException) -> Unit)?
    ): File? {
        fun getOutputDirectory(): File? {

            val mediaDir = activity.externalMediaDirs.firstOrNull()?.let {
                File(it, activity.resources.getString(R.string.app_name)).apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists()) mediaDir else activity.filesDir

            return null
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
}

@Composable
fun rememberPhotoCollectionUiElementState(
    activity: Activity,
    viewModel: CameraViewModel,
    onFlowComplete: (List<String>) -> Unit

) = remember {
    PhotoCollectionUiElementState(
        activity,
        viewModel,
        onFlowComplete
    )
}


tailrec fun Context?.activity(): Activity? = when (this) {
    is Activity -> this
    else -> (this as? ContextWrapper)?.baseContext?.activity()
}