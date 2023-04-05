package com.torrydo.testfloatingbubble

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class FloatingBubbleReceiver : BroadcastReceiver() {

    companion object {
        var hide_bubble_function: (() -> Unit)? = null
        var stop_bubble_function: (() -> Unit)? = null

        var isEnabled = true
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "ACTION_HIDE" -> {
                isEnabled = !isEnabled
                hide_bubble_function?.invoke()
            }
            "ACTION_STOP" -> {
                stop_bubble_function?.invoke()
            }
        }
    }
}