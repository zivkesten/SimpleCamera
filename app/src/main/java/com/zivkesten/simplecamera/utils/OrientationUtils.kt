package com.zivkesten.simplecamera.utils

import android.content.res.Configuration
import android.view.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

data class OrientationData(var current: Rotation?, var previous: Rotation?)

@Suppress("MagicNumber", "ComplexCondition", "CyclomaticComplexMethod")
fun getDeviceOrientation(pitch: Double, tilt: Double): String {
    return if (pitch in -25.0..25.0 && tilt in -45.0..45.0) {
        Orientation.FACE_UP.value
    } else if (((pitch < -145.0 && pitch > -180.0) || pitch in 145.0..180.0) && tilt in -45.0..45.0) {
        Orientation.FACE_DOWN.value
    } else if (pitch in 25.0..155.0 && tilt in -45.0..45.0) {
        Orientation.PORTRAIT.value
    } else if (pitch < -25.0 && pitch > -155.0 && tilt in -45.0..45.0) {
        Orientation.PORTRAIT_UPSIDE_DOWN.value
    } else if (tilt in 45.0..90.0) {
        Orientation.LANDSCAPE_RIGHT.value
    } else if (tilt < -45.0 && tilt > -90.0) {
        Orientation.LANDSCAPE_LEFT.value
    } else {
        Orientation.UNKNOWN.value
    }
}

enum class Orientation(val value: String) {
    PORTRAIT("portrait"),
    PORTRAIT_UPSIDE_DOWN("portrait upside down"),
    LANDSCAPE_LEFT("landscape left"),
    LANDSCAPE_RIGHT("landscape right"),
    FACE_UP("face up"),
    FACE_DOWN("face down"),
    UNKNOWN("unknown")
}

enum class Rotation {
    ROTATION_0, ROTATION_90, ROTATION_180, ROTATION_270
}

fun Rotation.toSurfaceOrientation() = when (this) {
    Rotation.ROTATION_0 -> Surface.ROTATION_0
    Rotation.ROTATION_90 -> Surface.ROTATION_90
    Rotation.ROTATION_180 -> Surface.ROTATION_180
    Rotation.ROTATION_270 -> Surface.ROTATION_270
}

fun Int.toSensorOrientation() = when (this) {
    Surface.ROTATION_0 -> Rotation.ROTATION_0
    Surface.ROTATION_90 -> Rotation.ROTATION_90
    Surface.ROTATION_180 -> Rotation.ROTATION_180
    Surface.ROTATION_270 -> Rotation.ROTATION_270
    else -> Rotation.ROTATION_0
}

private const val ROTATE_RIGHT = 90f
private const val ROTATE_LEFT = -90f
private const val ROTATE_UPSIDE_DOWN = 180f

val rotationMap = mapOf(

    // When     current   and  previous     then    return
    //             |               |                  |
    //             V               |                  |
    //                             |                  |
    //          PORTRAIT           V                  V
    Pair(Rotation.ROTATION_0, Rotation.ROTATION_90) to ROTATE_RIGHT,
    Pair(Rotation.ROTATION_0, Rotation.ROTATION_270) to ROTATE_LEFT,
    Pair(Rotation.ROTATION_0, Rotation.ROTATION_180) to ROTATE_UPSIDE_DOWN,

    //          LANDSCAPE
    Pair(Rotation.ROTATION_90, Rotation.ROTATION_0) to ROTATE_LEFT,
    Pair(Rotation.ROTATION_90, Rotation.ROTATION_270) to ROTATE_UPSIDE_DOWN,
    Pair(Rotation.ROTATION_90, Rotation.ROTATION_180) to ROTATE_RIGHT,

    //         LANDSCAPE REVERSED
    Pair(Rotation.ROTATION_270, Rotation.ROTATION_90) to ROTATE_UPSIDE_DOWN,
    Pair(Rotation.ROTATION_270, Rotation.ROTATION_0) to ROTATE_RIGHT,
    Pair(Rotation.ROTATION_270, Rotation.ROTATION_180) to ROTATE_LEFT,

    //          PORTRAIT UPSIDE DOWN
    Pair(Rotation.ROTATION_180, Rotation.ROTATION_90) to ROTATE_LEFT,
    Pair(Rotation.ROTATION_180, Rotation.ROTATION_270) to ROTATE_RIGHT,
    Pair(Rotation.ROTATION_180, Rotation.ROTATION_0) to ROTATE_UPSIDE_DOWN
)

fun OrientationData.initialPosition() =
    rotationMap.getOrDefault(Pair(current, previous), 0f)

@Composable
fun isLandscape() = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

@Composable
fun isPortrait() = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
