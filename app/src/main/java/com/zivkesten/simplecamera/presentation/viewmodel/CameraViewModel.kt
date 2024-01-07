package com.zivkesten.simplecamera.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCaptureException
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
import com.zivkesten.simplecamera.usecases.ImageCaptureUseCase
import com.zivkesten.simplecamera.utils.OrientationData
import com.zivkesten.simplecamera.utils.Rotation
import com.zivkesten.simplecamera.utils.mutable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    val imageCaptureUseCase: ImageCaptureUseCase
): ViewModel() {

    private val _capturedImages = MutableStateFlow<List<String>?>(null)
    var capturedImages: StateFlow<List<String>?> = _capturedImages

    private var scrollTo: Uri? by mutableStateOf(null)

    private val imageList = mutableListOf<ImageData>()
    private val imagesTimestamps = mutableListOf<Long>()

    var sensorOrientation = MutableStateFlow(Rotation.ROTATION_0)

    val cameraUiState: StateFlow<CameraUiState> = MutableStateFlow(
        CameraUiState(
            false, CameraControllerUiElementState(
                CAMERA,
                ShutterButtonState.ENABLED,
                CameraControllerUiElementState.ImagesParams(
                    imageList,
                    imageList.firstOrNull()?.uri,
                    scrollTo,
                    false,
                ),
                CameraControllerUiElementState.OrientationParams(
                    OrientationData(null, null),
                    sensorOrientation.value,
                )
            ) { onEvent(it) }
        )
    )
    init {
        viewModelScope.launch {
            sensorOrientation.collect { currentOrientation ->
                cameraUiState.mutable().value = cameraUiState.value.copy(
                    cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
                        orientationParams = cameraUiState.value.cameraUiElementState.orientationParams.copy(
                            orientationData = cameraUiState.value.cameraUiElementState.orientationParams.orientationData.copy(
                                previous = cameraUiState.value.cameraUiElementState.orientationParams.orientationData.current,
                                current = currentOrientation
                            )
                        )
                    )
                )
            }
        }
    }

    private fun onEvent(event: CameraUiEvent) {
        when (event) {
            is CameraUiEvent.ImageFail -> {
                // TODO:
                Log.e("Zivi", "ImageFail")
            }
            is CameraUiEvent.CloseButtonClicked -> {
                // TODO:
                Log.e("Zivi", "close clicked")
            }

            CameraUiEvent.ContinueBtnPressed -> {
                // TODO:
                Log.e("Zivi", "continue clicked")
            }


            is CameraUiEvent.FlashButtonClicked -> toggleFlashState()
            is CameraUiEvent.ImageCaptured -> onImageCaptured()
            is CameraUiEvent.RetakePhoto -> retakeImage()
            is CameraUiEvent.ImageSaved -> saveImage(event.image)
            is CameraUiEvent.ThumbnailClicked -> thumbnailClicked(event.image)
            is CameraUiEvent.ThumbnailRowItemClicked -> thumbnailRowItemClicked(event.image)
            is CameraUiEvent.PreviewImageViewed -> setItemSelected(event.image)
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
        imageCaptureUseCase.captureImage(
            sensorOrientation.value,
            onImageCaptured = { uri, isFlash ->
                onEvent(
                    CameraUiEvent.ImageSaved(
                        ImageData(uri, isFlash, false, MutableTransitionState(true))
                    )
                )
            },
            onError = { fileName: String, imageCaptureException: ImageCaptureException ->
                onEvent(CameraUiEvent.ImageFail(fileName, imageCaptureException.message))
            }
        )
    }

    private fun thumbnailRowItemClicked(item: ImageData) {
        val selectedImage = cameraUiState.value.cameraUiElementState.imagesParams.selectedThumbnail
        if (item.uri == selectedImage || scrollTo == item.uri) {
            removeItem(item)
        } else {
            cameraUiState.mutable().value = cameraUiState.value.copy(
                cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
                    imagesParams = cameraUiState.value.cameraUiElementState.imagesParams.copy(
                        scrollTo = item.uri
                    )
                )
            )
        }
    }

    private fun setItemSelected(item: ImageData) {
        Log.d("Zivi", "setItemSelected ${item.uri}")
        cameraUiState.mutable().value = cameraUiState.value.copy(
            cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
                imagesParams = cameraUiState.value.cameraUiElementState.imagesParams.copy(
                    imagesTaken = imageList.copy(item.uri),
                    selectedThumbnail = item.uri
                )
            )
        )
    }

    private fun thumbnailClicked(item: ImageData) {
        Log.d("Zivi", "thumbnailClicked: $item")
        clearPreviousOrientationData()
        cameraUiState.mutable().value = cameraUiState.value.copy(
            cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
                screen = GALLERY,
                imagesParams = cameraUiState.value.cameraUiElementState.imagesParams.copy(
                    imagesTaken = imageList.copy(
                        imageList.firstOrNull()?.uri
                    ), selectedThumbnail = item.uri
                )
            ),
        )
    }

    private fun clearPreviousOrientationData() {
        cameraUiState.mutable().value = cameraUiState.value.copy(
            isSavingImage = true,
            cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
                shutterButtonState = ShutterButtonState.ENABLED,
                orientationParams = cameraUiState.value.cameraUiElementState.orientationParams.copy(
                    orientationData = cameraUiState.value.cameraUiElementState.orientationParams.orientationData.copy(
                        previous = null
                    )
                )
            )
        )
    }

    private fun retakeImage() {
        clearPreviousOrientationData()
        cameraUiState.mutable().value = cameraUiState.value.copy(
            isSavingImage = false,
            cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
                screen = CAMERA,
                imagesParams = cameraUiState.value.cameraUiElementState.imagesParams.copy(
                    imagesTaken = imageList.copy(cameraUiState.value.cameraUiElementState.imagesParams.selectedThumbnail)
                )
            ),
        )
    }

    private fun saveImage(image: ImageData) {
        imageList.add(image)
        clearPreviousOrientationData()
        cameraUiState.mutable().value = cameraUiState.value.copy(
            isSavingImage = true,
            cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
                shutterButtonState = ShutterButtonState.ENABLED,
                imagesParams = cameraUiState.value.cameraUiElementState.imagesParams.copy(
                    imagesTaken = imageList.copy(cameraUiState.value.cameraUiElementState.imagesParams.selectedThumbnail)
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


        _capturedImages.value = attachmentItems
    }

    private fun removeItem(removedItem: ImageData) {
        // Remove from map
        imageList.remove(removedItem)
        // Emit a new ui state
        cameraUiState.mutable().value = cameraUiState.value.copy(
            cameraUiElementState = cameraUiState.value.cameraUiElementState.copy(
                imagesParams = cameraUiState.value.cameraUiElementState.imagesParams.copy(
                    imagesTaken = imageList.copy(scrollTo),
                    scrollTo = imageList.firstOrNull()?.uri
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
