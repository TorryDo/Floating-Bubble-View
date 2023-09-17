package com.torrydo.testfloatingbubble

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestComposeView(
    modifier: Modifier = Modifier,
    popBack: () -> Unit = {}
) {

    var num by remember { mutableStateOf(0) }

    var txt by remember {
        mutableStateOf("")
    }

    val items = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        val temp = mutableListOf<String>()
        for (i in 0..100) {
            temp.add(i.toString())
        }
        items.addAll(temp)
    }

    Column(modifier = Modifier) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .height(500.dp)
                .background(Color.LightGray),
        ) {
            Button(onClick = { popBack() }) {
                Text(text = "pop back!")
            }

            TextField(value = txt, onValueChange = {txt = it})

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(items) { index, item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = item.toString(), fontSize = 18.sp)
                    }
                }
            }
        }
    }

}