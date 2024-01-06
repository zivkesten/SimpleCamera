package com.zivkesten.simplecamera.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter

@Composable
fun CoilImageComponent(
    modifier: Modifier = Modifier,
    imageUrl: Any?,
    contentScale: ContentScale? = null,
    contentDescription: String? = "Coil Image",
) {
    Image(
        modifier = modifier.then(Modifier.fillMaxSize()),
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                // Optional: Add image transformations
                //placeholder(myDrawable)
                //error(Color.Red)
            }
        ),
        contentScale = contentScale ?: ContentScale.Crop,
        contentDescription = contentDescription,
    )
}