package com.zivkesten.simplecamera.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.LifecycleOwner

class CameraSurfaceHelper(
    val context: Context,
    val lifecycleOwner: LifecycleOwner,
    lensFacing: Int,
    val preview: Preview = Preview.Builder().build()
) {
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    val previewView = PreviewView(context)
}

@Composable
fun rememberCameraSurfaceHelper(context: Context, lifecycleOwner: LifecycleOwner, lensFacingBack: Int) = remember {
    CameraSurfaceHelper(
        context = context,
        lifecycleOwner,
        lensFacingBack
    )
}
