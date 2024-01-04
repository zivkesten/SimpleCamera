package com.lemonadeinc.lemonade.ui.composable.camera.controller.model

import java.io.Serializable

data class PhotoLimitWarningData(
    val title: String,
    val subtitle: String?,
    val limit: Int
) : Serializable
