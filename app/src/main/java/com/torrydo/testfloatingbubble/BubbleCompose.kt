package com.torrydo.testfloatingbubble

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun BubbleCompose() {

    val context = LocalContext.current

    var isPlay by remember { mutableStateOf(true) }
    var index by remember { mutableStateOf(0) }

    val songs = listOf(
        "See tình",
        "Shape of me",
        "Noi nay co anh",
    )

    Row(
        modifier = Modifier
            .height(50.dp)
            .clip(shape = RoundedCornerShape(100.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            isPlay = isPlay.not()
        }) {
            Icon(
                imageVector = if (isPlay) Icons.Default.PlayArrow else Icons.Default.Pause,
                contentDescription = "play arrow"
            )
        }

        IconButton(onClick = {
            if (index >= songs.size - 1) {
                index = 0
            } else {
                ++index
            }
        }) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "play arrow",
                tint = Color.Red.copy(0.8f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 10.dp, end = 20.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            Toast
                                .makeText(context, "Long click: Copied \"${songs[index]}\"", Toast.LENGTH_LONG)
                                .show()
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = songs[index],
                fontWeight = FontWeight.Bold,
            )
        }


    }
}