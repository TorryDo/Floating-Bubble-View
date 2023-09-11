package com.torrydo.testfloatingbubble

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MyCloseCompose() {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(60.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(0.3f))
    )
}