package com.torrydo.testfloatingbubble

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TestComposeView(
    modifier: Modifier = Modifier,
    popBack: () -> Unit = {}
) {

    var num by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
//        Log.d("<>", "in launched effect")
        while (true) {
            delay(1000)
            num++
        }
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .height(300.dp)
            .background(
                Color.Blue
            )
            .clickable {
                popBack()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Hello ${num}", color = Color.White)
    }
}