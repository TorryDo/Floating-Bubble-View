package com.torrydo.testfloatingbubble

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TestComposeView(
    modifier: Modifier = Modifier,
    popBack: () -> Unit = {}
) {

    var num by remember { mutableStateOf(0) }

    val items = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        val temp = mutableListOf<String>()
        for(i in 0..100){
            temp.add(i.toString())
        }
        items.addAll(temp)
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .height(500.dp)
            .background(Color.LightGray)
        ,
    ) {
        Button(onClick = { popBack() }) {
            Text(text = "pop back!")
        }
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