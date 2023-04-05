package com.torrydo.floatingbubbleview

import android.view.KeyEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

/**
 * override "dispatchKeyEvent" of the floating composable
 *
 * */
@Composable
fun OverrideDispatchKeyEvent(handler: (KeyEvent?) -> Boolean?) {
    DisposableEffect(Unit){
        FloatingComposeView.keyEventHandler = handler
        onDispose {
//            FloatingComposeView.keyEventHandler = null
        }
    }
}