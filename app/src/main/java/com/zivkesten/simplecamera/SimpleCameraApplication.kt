package com.zivkesten.simplecamera

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SimpleCameraApplication: Application() {
    companion object {
        private var instance: SimpleCameraApplication? = null

        fun applicationContext() : Context? {
            return instance?.applicationContext
        }
    }
}