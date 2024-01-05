package com.zivkesten.simplecamera.camera.controller.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zivkesten.simplecamera.camera.controller.END_BUTTON_TAG

@Preview(showBackground = true)
@Composable
fun ContinueButton(onClick: (() -> Unit)? = null) {
    IconButton(
        onClick = { onClick?.invoke() },
        modifier = Modifier
            .background(Color.Green, CircleShape)
            .size(56.dp)
            .testTag(END_BUTTON_TAG)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            tint = Color.White,
            imageVector = Icons.Default.Check,
            contentDescription = ""
        )
    }
}
