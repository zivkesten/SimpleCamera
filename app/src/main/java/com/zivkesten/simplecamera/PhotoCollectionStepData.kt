package com.zivkesten.simplecamera

import java.io.Serializable

data class PhotoCollectionStepData(
    val id: String,
    val uploadData: Map<String, String>?,
) : Serializable
