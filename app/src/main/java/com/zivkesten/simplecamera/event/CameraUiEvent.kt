package com.zivkesten.simplecamera.event

import com.zivkesten.simplecamera.camera.controller.model.ImageData

sealed interface CameraUiEvent {
    object FlashButtonClicked : CameraUiEvent
    object ImageCaptured : CameraUiEvent
    class ImageSaved(val image: ImageData) : CameraUiEvent
    class ImageFail(val uri: String, val message: String?) : CameraUiEvent
    object RetakePhoto : CameraUiEvent
    object CloseButtonClicked : CameraUiEvent
    class ThumbnailClicked(val image: ImageData) : CameraUiEvent
    class ThumbnailRowItemClicked(val image: ImageData) : CameraUiEvent
    class PreviewImageViewed(val image: ImageData) : CameraUiEvent
    object ContinueBtnPressed : CameraUiEvent
}
