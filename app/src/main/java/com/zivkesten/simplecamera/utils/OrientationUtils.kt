package com.zivkesten.simplecamera.utils

import android.content.res.Configuration
import android.view.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

data class OrientationData(var current: Rotation?, var previous: Rotation?)

enum class Rotation {
    ROTATION_0, ROTATION_90, ROTATION_180, ROTATION_270
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
