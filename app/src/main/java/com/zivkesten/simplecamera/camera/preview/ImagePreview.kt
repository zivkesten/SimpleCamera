package com.zivkesten.simplecamera.camera.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ImagePreview(
    modifier: Modifier = Modifier,
    model: Any?,
) {
    Box(
        modifier = modifier.then(
            Modifier
                .background(Color.Black)
        )
    ) {

        // TODO: Coil image
//        GlideImage(
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxSize()
//                .align(Alignment.Center),
//            model = model,
//            contentScale = if (isLandscape()) ContentScale.FillHeight else ContentScale.FillWidth,
//
//        ) {
//            it.placeholder(CircularProgressDrawable(ContextProvider.getApplicationContext()))
//        }

    }
}
