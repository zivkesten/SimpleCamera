package com.zivkesten.simplecamera.ui

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import coil.compose.rememberImagePainter
import com.zivkesten.simpleCamera.R

@Composable
fun CoilImageComponent(imageUrl: Any?) {
    val context = LocalContext.current
    val myDrawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_launcher_background)

    Image(
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                // Optional: Add image transformations
                placeholder(myDrawable)
                error(Color.Red)
            }
        ),
        contentDescription = "Coil Image",
        modifier = Modifier.fillMaxSize()
    )
}