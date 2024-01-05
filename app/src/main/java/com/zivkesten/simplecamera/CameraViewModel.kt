package com.zivkesten.simplecamera

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zivkesten.simplecamera.camera.controller.model.ImageData
import com.zivkesten.simplecamera.camera.controller.model.copy
import com.zivkesten.simplecamera.state.PhotoCollectionUiState
import com.zivkesten.simplecamera.event.CameraUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.lang.IllegalStateException
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(): ViewModel() {

    private val TAG: String = "PhotoCollectionViewModel"
    val uiState: MutableStateFlow<PhotoCollectionUiState> = MutableStateFlow(PhotoCollectionUiState(
        0, false, emptyList()
    ))


    val imageTakenUri: Uri? get() = imageList.firstOrNull()?.uri
    var selectedImage: Uri? by mutableStateOf(null)
    var scrollTo: Uri? by mutableStateOf(null)

    private val imageList = mutableListOf<ImageData>()
    private val imagesTimestamps = mutableListOf<Long>()

    private val _attachmentsItem = MutableStateFlow<List<String>?>(null)
    var attachmentsItem: StateFlow<List<String>?> = _attachmentsItem
    var sensorOrientation = MutableStateFlow(Rotation.ROTATION_0)
    var orientationData by mutableStateOf(OrientationData(null, null))


    init {
        viewModelScope.launch {
            sensorOrientation.collect { currentOrientation ->
                orientationData.previous = orientationData.current
                orientationData.current = currentOrientation
            }
        }
    }

    fun onEvent(event: CameraUiEvent) {
        when (event) {
            is CameraUiEvent.RetakePhoto -> retakeImage()
            is CameraUiEvent.ImageSaved -> saveImage(event.image)
            is CameraUiEvent.ImageFail -> Unit// TODO: Handle
            is CameraUiEvent.ThumbnailClicked -> thumbnailClicked(event.image)
            is CameraUiEvent.ThumbnailRowItemClicked -> thumbnailRowItemClicked(event.image)
            is CameraUiEvent.PreviewImageViewed -> setItemSelected(event.image)
            else -> Unit
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
        uiState.mutable().value = uiState.value.copy(
            takenImages = imageList.copy(item.uri)
        )
    }

    fun <T> StateFlow<T>.mutable() = (this as? MutableStateFlow<T>)
        ?: throw IllegalStateException("StateFlow< object must be mutable")

    private fun thumbnailClicked(item: ImageData) {
        selectedImage = item.uri
        clearPreviousOrientationData()
        uiState.mutable().value = uiState.value.copy(
            step = GALLERY,
            takenImages = imageList.copy(
                imageList.firstOrNull()?.uri
            )
        )
    }

    private fun clearPreviousOrientationData() {
        orientationData.previous = null
    }

    private fun retakeImage() {
        imageList.removeLast()
        clearPreviousOrientationData()
        uiState.mutable().value = uiState.value.copy(
            step = CAMERA,
            isSavingImage = false,
            takenImages = imageList.copy(selectedImage)
        )
    }

    private fun saveImage(image: ImageData) {
        imageList.add(image)
        clearPreviousOrientationData()
            uiState.mutable().value = uiState.value.copy(
                step = CAMERA,
                isSavingImage = false,
                takenImages = imageList.copy(selectedImage)
            )


        imagesTimestamps.add(System.currentTimeMillis())
    }

    fun setIsSavingImage() {
        uiState.mutable().value = uiState.value.copy(isSavingImage = true)
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
        val takenImages = imageList.copy(scrollTo) ?: emptyList()

        // Emit a new ui state
        uiState.mutable().value = uiState.value.copy(
            takenImages = takenImages
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
