package com.zivkesten.simplecamera.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.camera.core.AspectRatio
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zivkesten.simplecamera.camera.controller.buttons.ShutterButtonState
import com.zivkesten.simplecamera.camera.controller.model.ImageData
import com.zivkesten.simplecamera.camera.controller.model.copy
import com.zivkesten.simplecamera.camera.controller.state.CameraControllerUiElementState
import com.zivkesten.simplecamera.presentation.event.CameraUiEvent
import com.zivkesten.simplecamera.presentation.state.CameraUiState
import com.zivkesten.simplecamera.usecases.GetTakenPhotoUseCase
import com.zivkesten.simplecamera.utils.OrientationData
import com.zivkesten.simplecamera.utils.Rotation
import com.zivkesten.simplecamera.utils.mutable
import com.zivkesten.simplecamera.utils.toSurfaceOrientation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.net.URI
import java.util.UUID
import java.util.concurrent.Executors
import javax.inject.Inject


const val ERROR_SAVING_FILE = "Error saving file"
private const val JPG_SUFFIX = ".jpg"

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val getTakenPhotoUseCase: GetTakenPhotoUseCase
): ViewModel() {

    private val resolutionSelector = ResolutionSelector.Builder()
        .setAspectRatioStrategy(AspectRatioStrategy(AspectRatio.RATIO_16_9, AspectRatioStrategy.FALLBACK_RULE_AUTO) )
        .build()

    val imageCapture: ImageCapture = ImageCapture.Builder().setResolutionSelector(resolutionSelector).build()

    val imageTakenUri: Uri? get() = imageList.firstOrNull()?.uri
    var selectedImage: Uri? by mutableStateOf(null)
    var scrollTo: Uri? by mutableStateOf(null)

    private val imageList = mutableListOf<ImageData>()
    private val imagesTimestamps = mutableListOf<Long>()

    private val _attachmentsItem = MutableStateFlow<List<String>?>(null)
    var attachmentsItem: StateFlow<List<String>?> = _attachmentsItem
    var sensorOrientation = MutableStateFlow(Rotation.ROTATION_0)
    var orientationData by mutableStateOf(OrientationData(null, null))

    val cameraUiState: StateFlow<CameraUiState> = MutableStateFlow(
        CameraUiState(
            CAMERA, false, CameraControllerUiElementState(
                CAMERA,
                ShutterButtonState.ENABLED,
                CameraControllerUiElementState.ImagesParams(
                    emptyList(),
                    imageTakenUri,
                    scrollTo,
                    false,
                ),
                CameraControllerUiElementState.OrientationParams(
                    orientationData,
                    sensorOrientation.value,
                )
            ) { onEvent(it) }
        )
    )

    init {
        observeAttachments()
        viewModelScope.launch {
            sensorOrientation.collect { currentOrientation ->
                orientationData.previous = orientationData.current
                orientationData.current = currentOrientation
            }
        }
    }

    fun onEvent(event: CameraUiEvent) {
        when (event) {
            is CameraUiEvent.ImageFail -> Unit // TODO: Handle
            is CameraUiEvent.FlashButtonClicked -> toggleFlashState()
            is CameraUiEvent.ImageCaptured -> onImageCaptured()
            is CameraUiEvent.CloseButtonClicked -> {
                // TODO: Handle
                Log.e("Zivi", "close clicked")
            }

            CameraUiEvent.ContinueBtnPressed -> {
                Log.e("Zivi", "continue clicked")
            }
            is CameraUiEvent.RetakePhoto -> retakeImage()
            is CameraUiEvent.ImageSaved -> saveImage(event.image)
            is CameraUiEvent.ImageFail -> Unit// TODO: Handle
            is CameraUiEvent.ThumbnailClicked -> thumbnailClicked(event.image)
            is CameraUiEvent.ThumbnailRowItemClicked -> thumbnailRowItemClicked(event.image)
            is CameraUiEvent.PreviewImageViewed -> setItemSelected(event.image)
            else -> Unit
        }
    }

    private fun toggleFlashState() {
        cameraUiState.mutable().value = cameraUiState.value.copy(
            cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
                imagesParams = cameraUiState.value.cameraUiElementState.imagesParams.copy(
                    isFlashOn = !cameraUiState.value.cameraUiElementState.imagesParams.isFlashOn
                )
            )
        )
    }

    private fun onImageCaptured() {
        cameraUiState.mutable().value = cameraUiState.value.copy(
            cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
                shutterButtonState = ShutterButtonState.LOADING
            )
        )
        setIsSavingImage()
        Log.d("Zivi", "onImageCaptured")
        takePhoto(
            onImageCaptured = { uri, isFlash ->
                Log.d("Zivi", "onImageCaptured1")
                onEvent(
                    CameraUiEvent.ImageSaved(
                        ImageData(uri, isFlash, false, MutableTransitionState(true))
                    )
                )
            },
            onErrorInvocation = { fileName: String, imageCaptureException: ImageCaptureException ->
                onEvent(CameraUiEvent.ImageFail(fileName, imageCaptureException.message))
            }
        )
    }

    private fun takePhoto(
        onImageCaptured: (Uri, Boolean) -> Unit,
        onErrorInvocation: ((String, ImageCaptureException) -> Unit)? = null
    ) {
        // TODO: Handle
        imageCapture.targetRotation = sensorOrientation.value.toSurfaceOrientation()


        val fileName = generateTempFileName()
        val photoFile: File? = getTakenPhotoUseCase.execute(fileName, onErrorInvocation)

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

    fun generateTempFileName(): String {
        val id: String = UUID.randomUUID().toString()
        return id + JPG_SUFFIX
    }

    private fun observeAttachments() {
        viewModelScope.launch {
            attachmentsItem.collect { attachments ->
                if (attachments != null) {
                    //onFlowComplete(attachments)
                    Log.d("Zivi", "Flow complete $attachments")
                }
            }
        }
    }

    private fun thumbnailRowItemClicked(item: ImageData) {
        if (item.uri == selectedImage || scrollTo == item.uri) {
            removeItem(item)
        } else {
            scrollTo = item.uri
        }
    }

    private fun setItemSelected(item: ImageData) {
        selectedImage = item.uri
        cameraUiState.mutable().value = cameraUiState.value.copy(
            cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
                imagesParams = cameraUiState.value.cameraUiElementState.imagesParams.copy(
                    imagesTaken = imageList.copy(item.uri)
                )
            )
        )
    }

    private fun thumbnailClicked(item: ImageData) {
        selectedImage = item.uri
        clearPreviousOrientationData()
        cameraUiState.mutable().value = cameraUiState.value.copy(
            step = GALLERY,
            cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
                imagesParams = cameraUiState.value.cameraUiElementState.imagesParams.copy(
                    imagesTaken = imageList.copy(
                        imageList.firstOrNull()?.uri
                    )
                )
            ),
        )
    }

    private fun clearPreviousOrientationData() {
        orientationData.previous = null
    }

    private fun retakeImage() {
        imageList.removeLast()
        clearPreviousOrientationData()
        cameraUiState.mutable().value = cameraUiState.value.copy(
            step = CAMERA,
            isSavingImage = false,
            cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
                imagesParams = cameraUiState.value.cameraUiElementState.imagesParams.copy(
                    imagesTaken = imageList.copy(selectedImage)
                )
            ),
        )
    }

    private fun saveImage(image: ImageData) {
        Log.d("Zivi", "saveImage")

        imageList.add(image)
        Log.d("Zivi", "imageList updated $imageList")
        clearPreviousOrientationData()
        cameraUiState.mutable().value = cameraUiState.value.copy(
            step = CAMERA,
            isSavingImage = true,
            cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
                shutterButtonState = ShutterButtonState.ENABLED,
                imagesParams = cameraUiState.value.cameraUiElementState.imagesParams.copy(
                    imagesTaken = imageList.copy(selectedImage)
                )
            )
        )


        imagesTimestamps.add(System.currentTimeMillis())
    }

    fun setIsSavingImage() {
        cameraUiState.mutable().value = cameraUiState.value.copy(isSavingImage = true)
    }


    private fun prepareAttachmentList() {
        val attachmentItems = mutableListOf<String>()
        var file: File

        imageList.forEach { imageData ->
            try {
                file = File(URI.create(imageData.uri.toString()))
                attachmentItems.add(file.path)

            } catch (e: Exception) {
                Log.e(TAG, "getAttachmentList:  file error $e")
            }
        }


        _attachmentsItem.value = attachmentItems
    }

    private fun removeItem(removedItem: ImageData) {
        // Remove from map
        imageList.remove(removedItem)
        // Notify scroll to the last item on the list
        scrollTo = imageList.firstOrNull()?.uri

        // Change the selected value in the original map to be the one we scroll to
        val takenImages = imageList.copy(scrollTo)

        // Emit a new ui state
        cameraUiState.mutable().value = cameraUiState.value.copy(
            cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
            imagesParams = cameraUiState.value.cameraUiElementState.imagesParams.copy(
                imagesTaken = takenImages
            )
        )
        )
    }

    companion object {

        val TAG: String = CameraViewModel::class.java.simpleName

        const val CAMERA = 0
        const val PREVIEW = 1
        const val GALLERY = 2
        const val STEP_CONTENT_ERROR_MSG = "STEP_CONTENT is empty or null"
        const val SAVE_PHOTO = "save_photo"
        const val USED_FLASH = "used_flash"
        const val PHOTO_LANDSCAPE = "photo_landscape"
        const val YES = "yes"
        const val NO = "no"
        const val VERTICAL = "vertical"
        const val SHUTTER = "shutter"
        const val ACTION_TYPE = "action_type"
        const val QUESTION_ID = "questionId"
        const val SCRIPT_ID = "script_id"
        const val CONTEXT_CLAIM = "claim"
    }
}
