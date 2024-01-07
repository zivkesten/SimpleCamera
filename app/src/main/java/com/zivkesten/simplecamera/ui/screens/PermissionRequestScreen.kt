package com.zivkesten.simplecamera.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequestScreen(
    coroutineScope: CoroutineScope,
    cameraPermissionState: PermissionState
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)){
        Button(
            modifier = Modifier
                .align(Alignment.Center)
                .height(58.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                coroutineScope.launch {
                    cameraPermissionState.launchPermissionRequest()
                }
            }) {
            Text(
                text = "Request Camera Permission",
                style = TextStyle(
                    fontSize = 30.sp
                )
            )
        }
    }
}