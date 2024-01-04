package com.lemonadeinc.lemonade.ui.composable.camera.controller.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lemonadeinc.lemonade.R
import com.zivkesten.simplecamera.camera.controller.END_BUTTON_TAG

@Preview(showBackground = true)
@Composable
fun ContinueButton(onClick: (() -> Unit)? = null) {
    IconButton(
        onClick = { onClick?.invoke() },
        modifier = Modifier
            .background(colorResource(id = R.color.lemonade_pink), CircleShape)
            .size(56.dp)
            .testTag(END_BUTTON_TAG)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            tint = Color.White,
            painter = painterResource(R.drawable.ic_check_small),
            contentDescription = ""
        )
    }
}
