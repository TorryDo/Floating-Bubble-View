package com.torrydo.testfloatingbubble

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BubbleCompose() {
    Row(
        modifier = Modifier
            .width(150.dp)
            .height(50.dp)
            .background(Color.White)

    ) {
        Box(modifier = Modifier.fillMaxHeight().weight(1f).padding(5.dp).background(Color.Red))

        Box(modifier = Modifier.fillMaxHeight().weight(2f).padding(5.dp).background(Color.Blue))
    }
}