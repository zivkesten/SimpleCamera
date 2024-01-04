package com.lemonadeinc.lemonade.ui.feature.photocollection.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lemonadeinc.lemonade.R
import com.lemonadeinc.lemonade.ui.feature.photocollection.domain.model.ActionScreenData
import com.lemonadeinc.ui.utils.latoFamily

@Composable
fun PermissionsDeniedScreen(
    content: ActionScreenData?,
    onDismiss: () -> Unit,
    onAction: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.permission_screen_background))
    ) {
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .background(colorResource(id = R.color.lemonade_metal), shape = CircleShape)
                    .align(Alignment.Center)
                    .size(24.dp),
                interactionSource = MutableInteractionSource()
            ) {
                Image(
                    modifier = Modifier.padding(4.dp),
                    painter = painterResource(id = R.drawable.ic_close_metal),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.insufficient_permissions),
                contentDescription = null,
                modifier = Modifier.padding(bottom = 22.dp)
            )
            Text(
                text = content?.title
                    ?: stringResource(id = R.string.camera_insufficient_permissions_title).uppercase(),
                style = TextStyle(
                    color = colorResource(id = R.color.lemonade_night),
                    fontSize = 16.sp,
                    fontFamily = latoFamily,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 15.dp)
            )
            Text(
                text = content?.body
                    ?: stringResource(id = R.string.camera_insufficient_permissions_subtitle).uppercase(),
                style = TextStyle(
                    color = colorResource(id = R.color.lemonade_night),
                    fontSize = 16.sp
                ),
                textAlign = TextAlign.Center
            )
        }
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.lemonade_pink)
            ),
            onClick = onAction,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(58.dp)
        ) {
            Text(
                text = content?.ctaText ?: stringResource(id = R.string.camera_button_cancel),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = latoFamily,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun DeniedPermissionsPreview() {
    PermissionsDeniedScreen(
        ActionScreenData(
            ctaText = "GRANT PERMISSIONS"
        ),
        {}, {}
    )
}
