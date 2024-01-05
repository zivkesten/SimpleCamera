package com.zivkesten.simplecamera.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.IllegalStateException

fun <T> StateFlow<T>.mutable() = (this as? MutableStateFlow<T>)
    ?: throw IllegalStateException("StateFlow< object must be mutable")