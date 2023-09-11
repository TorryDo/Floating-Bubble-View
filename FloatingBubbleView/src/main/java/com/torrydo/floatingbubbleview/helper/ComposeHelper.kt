package com.torrydo.floatingbubbleview.helper

import android.view.KeyEvent
import androidx.annotation.Discouraged
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.torrydo.floatingbubbleview.MyFloatingComposeView

/**
 * override "dispatchKeyEvent" of the floating composable
 *
 * */
@Deprecated("removed")
@Composable
internal fun OverrideDispatchKeyEvent(handler: (KeyEvent?) -> Boolean?) {
    DisposableEffect(Unit){
        MyFloatingComposeView.keyEventHandler = handler
        onDispose {
//            FloatingComposeView.keyEventHandler = null
        }
    }
}