package com.zivkesten.simplecamera.utils

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController

fun hideStatusBar(activity: Activity) {
    val decorView = activity.window.decorView
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val controller = decorView.windowInsetsController
        controller!!.hide(WindowInsets.Type.statusBars())
        controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}

fun hideNavigationBar(activity: Activity) {
    // Get the window object.
    val window = activity.window

    // Get the WindowInsetsController object.
    var insetsController: WindowInsetsController? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        insetsController = window.insetsController
        insetsController!!.hide(WindowInsets.Type.navigationBars())
    }
}

