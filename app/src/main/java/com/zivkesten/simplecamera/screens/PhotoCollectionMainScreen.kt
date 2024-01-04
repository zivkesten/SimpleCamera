package com.zivkesten.simplecamera.screens

import androidx.camera.core.CameraSelector
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.zivkesten.simplecamera.CameraSurface
import com.zivkesten.simplecamera.rememberCameraSurfaceHelper
import com.zivkesten.simplecamera.state.PhotoCollectionUiElementState
import com.zivkesten.simplecamera.state.PhotoCollectionUiState

internal const val CONTROLLER_SIZE: Float = 160f
private const val PHOTO_COLLECTION_ANALYTICS_ATTRIBUTE = "photo_collection"
private const val CAMERA_ANALYTICS_ATTRIBUTE = "camera"

@Composable
fun PhotoCollectionMainScreen(
    uiState: PhotoCollectionUiState,
    uiElementState: PhotoCollectionUiElementState,
) {
//    BackHandler(uiElementState.enableOnBackPressed) {
//        if (uiElementState.enableOnBackPressed) {
//            uiElementState.onUIEvent(CameraUiEvent.CloseButtonClicked)
//        }
//    }
//
//    var permissionState by remember {
//        mutableStateOf(PermissionState.ALLOWED)
//    }
//    val launcher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isPermissionGranted ->
//        reportPermissionResult(isPermissionGranted)
////        permissionState = PermissionUtil.getPermissionState(
////            uiElementState.activity,
////            Manifest.permission.CAMERA
////        )
//        uiElementState.permissionState = permissionState
//    }
//    HandlePermissions(launcher, uiElementState)
//
//    when (uiElementState.permissionState) {
//        PermissionState.ALLOWED -> MainScreen(uiElementState, uiState)
//        PermissionState.INITIAL -> DeniedPermissionScreen(uiElementState = uiElementState, launcher)
//        PermissionState.DENIED -> {
//
//        }
//
//        else -> Unit
//    }
}


//@Composable
//private fun HandlePermissions(
//    launcher: ManagedActivityResultLauncher<String, Boolean>,
//    uiElementState: PhotoCollectionUiElementState
//) {
//    val lifecycleOwner = LocalLifecycleOwner.current
//    DisposableEffect(lifecycleOwner) {
//        val observer = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_START) {
//                PermissionRequestManager.persistPermissionRequest(Manifest.permission.CAMERA)
//                launcher.launch(Manifest.permission.CAMERA)
//                uiElementState.reportPermissionsRequest(PHOTO_COLLECTION_ANALYTICS_ATTRIBUTE)
//            }
//        }
//        lifecycleOwner.lifecycle.addObserver(observer)
//
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(observer)
//        }
//    }
//}

@Composable
private fun MainScreen(
    uiElementState: PhotoCollectionUiElementState,
    uiState: PhotoCollectionUiState
) {
    val coroutineScope = rememberCoroutineScope()

    val cameraSurfaceHelper = rememberCameraSurfaceHelper(
        LocalContext.current,
        LocalLifecycleOwner.current,
        CameraSelector.LENS_FACING_BACK
    )

    // -- Using CameraSurface and CameraScreensRouter can be used as camera flow everywhere -- //

    // Camera surface
    CameraSurface(
        modifier = Modifier,
        uiElementState.sensorOrientation,
        uiElementState.imageCapture,
        uiElementState.isFlashOn,
        cameraSurfaceHelper,
    )

    // Camera controller
    CameraUI(
        uiElementState.cameraControllerState,
    )


}
