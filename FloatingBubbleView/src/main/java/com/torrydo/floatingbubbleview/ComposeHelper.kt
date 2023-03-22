package com.torrydo.floatingbubbleview

import android.view.KeyEvent

/**
 * override "dispatchKeyEvent" of the floating composable
 *
 * */
fun overrideDispatchKeyEvent(handler: (KeyEvent?) -> Boolean?) {
    FloatingComposeView.keyEventHandler = handler
}